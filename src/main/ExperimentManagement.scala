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
    val name = args(0).getString.trim

    if (!args(1).getBooleanValue &&
        BehaviorSpaceExtension.experimentType(name, context) != ExperimentType.None)
      return BehaviorSpaceExtension.nameError(context, "alreadyExists", args(0).getString)
    if (name.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "emptyName")

    if (BehaviorSpaceExtension.experiments.contains(name))
      BehaviorSpaceExtension.experiments(name) = new ExperimentData()
    else
      BehaviorSpaceExtension.experiments += ((name, new ExperimentData()))

    BehaviorSpaceExtension.experiments(name).name = name

    if (BehaviorSpaceExtension.savedExperiments.contains(name))
      BehaviorSpaceExtension.savedExperiments -= name
  }
}

object DeleteExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    val name = args(0).getString.trim

    if (!BehaviorSpaceExtension.validateForEditing(name, context)) return

    BehaviorSpaceExtension.experiments -= name
    
    if (BehaviorSpaceExtension.savedExperiments.contains(name))
      BehaviorSpaceExtension.savedExperiments -= name
  }
}

object RunExperiment extends Command {
  override def getSyntax = {
    commandSyntax()
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    val protocol = BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get
      case ExperimentType.Code =>
        if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
          BehaviorSpaceExtension.savedExperiments(BehaviorSpaceExtension.currentExperiment)
        else
          BehaviorSpaceExtension.protocolFromData(
            BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment))
      case _ =>
        return BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
    }

    javax.swing.SwingUtilities.invokeLater(() => {
      if (BehaviorSpaceExtension.experimentStack.contains(protocol.name)) {
        return BehaviorSpaceExtension.nameError(context, "recursive")
      }

      BehaviorSpaceExtension.experimentStack += protocol.name

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

      Main.runExperimentWithProtocol(new Settings(context.workspace.getModelPath, None, None, table, spreadsheet,
                                                  stats, lists, None, protocol.runOptions.threadCount, false,
                                                  protocol.runOptions.updatePlotsAndMonitors), protocol,
                                    () => {
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
    })
  }
}

object RenameExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    val name = args(0).getString.trim
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return
    if (BehaviorSpaceExtension.experimentType(name, context) != ExperimentType.None)
      return BehaviorSpaceExtension.nameError(context, "noExperiment", name)
    if (name.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "emptyName")

    val data = BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment)

    data.name = name

    BehaviorSpaceExtension.experiments -= BehaviorSpaceExtension.currentExperiment
    BehaviorSpaceExtension.experiments += ((name, data))

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment)) {
      val protocol = BehaviorSpaceExtension.savedExperiments(BehaviorSpaceExtension.currentExperiment)

      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
      BehaviorSpaceExtension.savedExperiments += ((name, protocol.copy(name = name)))
    }
  }
}

object DuplicateExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    val name = args(0).getString.trim

    if (BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) !=ExperimentType.None)
      return BehaviorSpaceExtension.nameError(context, "noExperiment", name)
    if (name.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "emptyName")

    val data = BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        BehaviorSpaceExtension.dataFromProtocol(context.workspace.getBehaviorSpaceExperiments.
                                                find(x => x.name == BehaviorSpaceExtension.currentExperiment).get)
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment)
    }

    data.name = name

    BehaviorSpaceExtension.experiments += ((name, data))
  }
}

object ImportExperiments extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    val path = args(0).getString.trim

    try {
      for (protocol <- new LabLoader(context.workspace.asInstanceOf[AbstractWorkspace].compiler.utilities)
                                    (scala.io.Source.fromFile(path).mkString, true,
                                     scala.collection.mutable.Set[String]()))
      {
        if (BehaviorSpaceExtension.experimentType(protocol.name, context) != ExperimentType.None)
          BehaviorSpaceExtension.nameError(context, "noExperiment", protocol.name)
        else
          BehaviorSpaceExtension.experiments += ((protocol.name, BehaviorSpaceExtension.dataFromProtocol(protocol)))
      }
    } catch {
      case e: org.xml.sax.SAXParseException =>
        BehaviorSpaceExtension.nameError(context, "invalidFormat", path)
    }
  }
}

object ExportExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType, BooleanType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    val path = args(0).getString.trim
        
    val protocol = BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get
      case ExperimentType.Code =>
        BehaviorSpaceExtension.protocolFromData(BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment))
      case _ =>
        return BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
    }

    if (!args(1).getBooleanValue && new java.io.File(path).exists)
      return BehaviorSpaceExtension.nameError(context, "fileExists", path)

    val out = new java.io.PrintWriter(path)

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
    BehaviorSpaceExtension.currentExperiment = args(0).getString.trim
  }
}
