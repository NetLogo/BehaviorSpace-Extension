// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Command, Context }
import org.nlogo.core.I18N
import org.nlogo.core.Syntax._
import org.nlogo.fileformat.{ LabLoader, LabSaver }
import org.nlogo.lab.gui.Supervisor
import org.nlogo.window.GUIWorkspace
import org.nlogo.workspace.AbstractWorkspace

object CreateExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType, BooleanType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (!args(1).getBooleanValue &&
        BehaviorSpaceExtension.experimentType(args(0).getString, context) != ExperimentType.None)
      return BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.alreadyExists",
                                                            args(0).getString), context)
    if (args(0).getString.isEmpty)
      return BehaviorSpaceExtension.nameError(I18N.gui.get("edit.behaviorSpace.name.empty"), context)

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
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    val protocol = BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get
      case ExperimentType.Code =>
        if (BehaviorSpaceExtension.savedExperiments.contains(args(0).getString))
          BehaviorSpaceExtension.savedExperiments(args(0).getString)
        else
          BehaviorSpaceExtension.protocolFromData(BehaviorSpaceExtension.experiments(args(0).getString))
      case _ => return BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment", args(0).getString), context)
    }

    javax.swing.SwingUtilities.invokeLater(() => {
      Supervisor.runFromExtension(protocol, context.workspace.asInstanceOf[AbstractWorkspace], (protocol) => {
        if (BehaviorSpaceExtension.savedExperiments.contains(protocol.name)) {
          if (protocol.runsCompleted == 0)
            BehaviorSpaceExtension.savedExperiments -= protocol.name
          else
            BehaviorSpaceExtension.savedExperiments(protocol.name) = protocol
        }
        else if (protocol.runsCompleted != 0)
          BehaviorSpaceExtension.savedExperiments += ((protocol.name, protocol))
      }, if (context.workspace.isHeadless) Supervisor.Headless else Supervisor.Extension)
    })
  }
}

object RenameExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType, StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (!BehaviorSpaceExtension.validateForEditing(args(0).getString, context)) return
    if (BehaviorSpaceExtension.experimentType(args(1).getString, context) != ExperimentType.None)
      return BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.alreadyExists", args(1).getString), context)
    if (args(1).getString.isEmpty)
      return BehaviorSpaceExtension.nameError(I18N.gui.get("edit.behaviorSpace.name.empty"), context)

    val data = BehaviorSpaceExtension.experiments(args(0).getString)

    data.name = args(1).getString

    BehaviorSpaceExtension.experiments -= args(0).getString
    BehaviorSpaceExtension.experiments += ((args(1).getString, data))

    if (BehaviorSpaceExtension.savedExperiments.contains(args(0).getString)) {
      val protocol = BehaviorSpaceExtension.savedExperiments(args(0).getString)

      BehaviorSpaceExtension.savedExperiments -= args(0).getString
      BehaviorSpaceExtension.savedExperiments += ((args(1).getString, protocol.copy(name = args(1).getString)))
    }
  }
}

object DuplicateExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType, StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.experimentType(args(1).getString, context) != ExperimentType.None)
      return BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.alreadyExists", args(1).getString), context)
    if (args(1).getString.isEmpty)
      return BehaviorSpaceExtension.nameError(I18N.gui.get("edit.behaviorSpace.name.empty"), context)

    val data = BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        BehaviorSpaceExtension.dataFromProtocol(context.workspace.getBehaviorSpaceExperiments.
                                                find(x => x.name == args(0).getString).get)
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString)
    }

    data.name = args(1).getString

    BehaviorSpaceExtension.experiments += ((args(1).getString, data))
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
          BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.alreadyExists",
                                                         protocol.name), context)
        else
          BehaviorSpaceExtension.experiments += ((protocol.name, BehaviorSpaceExtension.dataFromProtocol(protocol)))
      }
    } catch {
      case e: org.xml.sax.SAXParseException => {
        if (!context.workspace.isHeadless) {
          javax.swing.JOptionPane.showMessageDialog(context.workspace.asInstanceOf[GUIWorkspace].getFrame,
                                                    I18N.gui.getN("tools.behaviorSpace.error.import",
                                                                  args(0).getString),
                                                    I18N.gui.get("tools.behaviorSpace.invalid"),
                                                    javax.swing.JOptionPane.ERROR_MESSAGE)
        }
      }
    }
  }
}

object ExportExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType, StringType, BooleanType))
  }

  def perform(args: Array[Argument], context: Context) {
    val protocol = BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get
      case ExperimentType.Code =>
        BehaviorSpaceExtension.protocolFromData(BehaviorSpaceExtension.experiments(args(0).getString))
      case _ =>
        return BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                              args(0).getString), context)
    }

    if (!args(2).getBooleanValue && new java.io.File(args(1).getString).exists)
      return BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.fileExists",
                                                            args(1).getString), context)

    val out = new java.io.PrintWriter(args(1).getString)

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
