// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import java.io.{ File, FileWriter, PrintWriter }

import org.nlogo.api.{ Argument, Command, Context }
import org.nlogo.core.Syntax._
import org.nlogo.fileformat.{ LabLoader, LabSaver }
import org.nlogo.headless.Main
import org.nlogo.lab.gui.Supervisor
import org.nlogo.nvm.LabInterface.Settings
import org.nlogo.window.GUIWorkspace
import org.nlogo.workspace.AbstractWorkspace

object CreateExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType, BooleanType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (!args(1).getBooleanValue &&
        BehaviorSpaceExtension.experimentType(args(0).getString, context) != ExperimentType.None)
      return BehaviorSpaceExtension.nameError(s"""An experiment already exists with the name "${args(0).getString}".""",
                                              context)
    if (args(0).getString.isEmpty)
      return BehaviorSpaceExtension.nameError("Experiment name cannot be empty.", context)

    if (BehaviorSpaceExtension.experiments.contains(args(0).getString))
      BehaviorSpaceExtension.experiments(args(0).getString) = new ExperimentData()
    else
      BehaviorSpaceExtension.experiments += ((args(0).getString, new ExperimentData()))

    BehaviorSpaceExtension.experiments(args(0).getString).name = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(args(0).getString))
      BehaviorSpaceExtension.savedExperiments -= args(0).getString
  }
}

object DeleteExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (!BehaviorSpaceExtension.validateForEditing(args(0).getString, context)) return

    BehaviorSpaceExtension.experiments -= args(0).getString
    
    if (BehaviorSpaceExtension.savedExperiments.contains(args(0).getString))
      BehaviorSpaceExtension.savedExperiments -= args(0).getString
  }
}

object RunExperiment extends Command {
  override def getSyntax = {
    commandSyntax()
  }

  def perform(args: Array[Argument], context: Context) {
    val protocol = BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get
      case ExperimentType.Code =>
        if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
          BehaviorSpaceExtension.savedExperiments(BehaviorSpaceExtension.currentExperiment)
        else
          BehaviorSpaceExtension.protocolFromData(BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment))
      case _ => return BehaviorSpaceExtension.nameError(
        s"""No experiment exists with the name "${BehaviorSpaceExtension.currentExperiment}".""", context)
    }

    javax.swing.SwingUtilities.invokeLater(() => {
      if (BehaviorSpaceExtension.experimentStack.contains(protocol.name)) {
        return BehaviorSpaceExtension.nameError("Cannot run an experiment recursively.", context)
      }

      BehaviorSpaceExtension.experimentStack += protocol.name

      if (context.workspace.isHeadless) {
        val out = new java.io.PrintWriter("bsext_temp.xml")

        out.write(LabSaver.save(List(protocol)))

        out.close()

        val file = Some(new File("bsext_temp.xml"))

        val table =
          if (protocol.runOptions.table.trim.isEmpty) None
          else Some(new PrintWriter(new FileWriter(protocol.runOptions.table.trim)))
        val spreadsheet =
          if (protocol.runOptions.spreadsheet.trim.isEmpty) None
          else Some(new PrintWriter(new FileWriter(protocol.runOptions.spreadsheet.trim)))
        val stats =
          if (protocol.runOptions.stats.trim.isEmpty) None
          else Some((new PrintWriter(new FileWriter(protocol.runOptions.stats.trim)), protocol.runOptions.stats.trim))
        val lists =
          if (protocol.runOptions.lists.trim.isEmpty) None
          else Some((new PrintWriter(new FileWriter(protocol.runOptions.lists.trim)), protocol.runOptions.lists.trim))

        Main.runExperiment(new Settings(context.workspace.getModelPath, None, file, table, spreadsheet, stats, lists,
                                        None, protocol.runOptions.threadCount, false,
                                        protocol.runOptions.updatePlotsAndMonitors))

        file.get.delete()
      }

      else {
        Supervisor.runFromExtension(protocol, context.workspace.asInstanceOf[AbstractWorkspace], (protocol) => {
          if (BehaviorSpaceExtension.savedExperiments.contains(protocol.name)) {
            if (protocol.runsCompleted == 0)
              BehaviorSpaceExtension.savedExperiments -= protocol.name
            else
              BehaviorSpaceExtension.savedExperiments(protocol.name) = protocol
          }

          else if (protocol.runsCompleted != 0)
            BehaviorSpaceExtension.savedExperiments += ((protocol.name, protocol))

          BehaviorSpaceExtension.experimentStack -= protocol.name
        })
      }
    })
  }
}

object RenameExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return
    if (BehaviorSpaceExtension.experimentType(args(0).getString, context) != ExperimentType.None)
      return BehaviorSpaceExtension.nameError(s"""No experiment exists with the name "${args(0).getString}".""", context)
    if (args(0).getString.isEmpty)
      return BehaviorSpaceExtension.nameError("Experiment name cannot be empty.", context)

    val data = BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment)

    data.name = args(0).getString

    BehaviorSpaceExtension.experiments -= BehaviorSpaceExtension.currentExperiment
    BehaviorSpaceExtension.experiments += ((args(0).getString, data))

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment)) {
      val protocol = BehaviorSpaceExtension.savedExperiments(BehaviorSpaceExtension.currentExperiment)

      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
      BehaviorSpaceExtension.savedExperiments += ((args(0).getString, protocol.copy(name = args(0).getString)))
    }
  }
}

object DuplicateExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.experimentType(args(0).getString, context) != ExperimentType.None)
      return BehaviorSpaceExtension.nameError(s"""No experiment exists with the name "${args(0).getString}".""", context)
    if (args(0).getString.isEmpty)
      return BehaviorSpaceExtension.nameError("Experiment name cannot be empty.", context)

    val data = BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        BehaviorSpaceExtension.dataFromProtocol(context.workspace.getBehaviorSpaceExperiments.
                                                find(x => x.name == BehaviorSpaceExtension.currentExperiment).get)
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment)
    }

    data.name = args(0).getString

    BehaviorSpaceExtension.experiments += ((args(0).getString, data))
  }
}

object ImportExperiments extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    try {
      for (protocol <- new LabLoader(context.workspace.asInstanceOf[AbstractWorkspace].compiler.utilities)
                                    (scala.io.Source.fromFile(args(0).getString).mkString, true,
                                     scala.collection.mutable.Set[String]()))
      {
        if (BehaviorSpaceExtension.experimentType(protocol.name, context) != ExperimentType.None)
          BehaviorSpaceExtension.nameError(s"""No experiment exists with the name "${protocol.name}".""", context)
        else
          BehaviorSpaceExtension.experiments += ((protocol.name, BehaviorSpaceExtension.dataFromProtocol(protocol)))
      }
    } catch {
      case e: org.xml.sax.SAXParseException => {
        if (!context.workspace.isHeadless) {
          javax.swing.JOptionPane.showMessageDialog(context.workspace.asInstanceOf[GUIWorkspace].getFrame,
                                                    s"""Invalid format in "${args(0).getString}".""",
                                                    "Invalid",
                                                    javax.swing.JOptionPane.ERROR_MESSAGE)
        }
      }
    }
  }
}

object ExportExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType, BooleanType))
  }

  def perform(args: Array[Argument], context: Context) {
    val protocol = BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get
      case ExperimentType.Code =>
        BehaviorSpaceExtension.protocolFromData(BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment))
      case _ =>
        return BehaviorSpaceExtension.nameError(
          s"""No experiment exists with the name "${BehaviorSpaceExtension.currentExperiment}"""", context)
    }

    if (!args(1).getBooleanValue && new java.io.File(args(0).getString).exists)
      return BehaviorSpaceExtension.nameError(s"""File "${args(0).getString}" already exists.""", context)

    val out = new java.io.PrintWriter(args(0).getString)

    out.write(s"${LabLoader.XMLVER}\n${LabLoader.DOCTYPE}\n")
    out.write(LabSaver.save(List(protocol)))

    out.close()
  }
}

object ClearExperiments extends Command {
  override def getSyntax = {
    commandSyntax()
  }

  def perform(args: Array[Argument], context: Context) {
    BehaviorSpaceExtension.experiments.clear()
    BehaviorSpaceExtension.savedExperiments.clear()
  }
}

object SetCurrentExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    BehaviorSpaceExtension.currentExperiment = args(0).getString
  }
}
