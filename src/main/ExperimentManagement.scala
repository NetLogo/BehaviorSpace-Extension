// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import java.io.{ File, FileWriter, PrintWriter }

import org.nlogo.api.{ Argument, Command, Context, LabProtocol, LabVariableParser, Reporter }
import org.nlogo.core.LogoList
import org.nlogo.core.Syntax._
import org.nlogo.fileformat.FileFormat
import org.nlogo.headless.Main
import org.nlogo.lab.Worker
import org.nlogo.nvm.{ Experiment, ExperimentType, ExtensionContext, LabInterface }
import org.nlogo.workspace.AbstractWorkspace

import scala.io.Source
import scala.util.{ Failure, Success }

import BehaviorSpaceExtension._

object CreateExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType, BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val name = args(0).getString.trim
    val manager = getExperimentManager(context)

    if (!args(1).getBooleanValue && manager.getExperiment(name).isDefined)
      return nameError(context, "alreadyExists", name)

    if (name.isEmpty)
      return nameError(context, "emptyName")

    manager.addExperiment(Experiment(LabProtocol.defaultCodeProtocol(name), ExperimentType.Code))
  }
}

object DeleteExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val name = args(0).getString.trim
    val manager = getExperimentManager(context)
    val experiment = manager.getExperiment(name)

    if (experiment.isEmpty)
      return nameError(context, "noExperiment")

    if (experiment.exists(_.tpe != ExperimentType.Code))
      return nameError(context, "gui", name)

    manager.removeExperiment(name)
  }
}

object RunExperiment extends Command {
  override def getSyntax = {
    commandSyntax()
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val manager = getExperimentManager(context)
    val current = manager.getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (!manager.addExperimentToStack(protocol.name))
      return nameError(context, "recursive")

    var outputPath = ""

    val spreadsheet =
      if (protocol.spreadsheet.trim.isEmpty) None
      else {
        outputPath = protocol.spreadsheet.trim
        Some(new PrintWriter(new FileWriter(protocol.spreadsheet.trim)))
      }
    val table =
      if (protocol.table.trim.isEmpty) None
      else {
        outputPath = protocol.table.trim
        Some(new PrintWriter(new FileWriter(protocol.table.trim)))
      }
    val stats =
      if (protocol.stats.trim.isEmpty) None
      else Some((new PrintWriter(new FileWriter(protocol.stats.trim)), outputPath))
    val lists =
      if (protocol.lists.trim.isEmpty) None
      else Some((new PrintWriter(new FileWriter(protocol.lists.trim)), outputPath))

    val loadedExtensions = context.workspace.getExtensionManager.loadedExtensionNames

    Main.runExperimentWithProtocol(new LabInterface.Settings(context.workspace.getModelPath, None, None, table,
                                                             spreadsheet, stats, lists, None,
                                                             protocol.threadCount, false, false,
                                                             protocol.mirrorHeadlessOutput),
                                   protocol, manager.setStackWorker(protocol.name, _),
                                   () => manager.removeExperimentFromStack(protocol.name),
                                   context.asInstanceOf[ExtensionContext].workspace.getPrimaryWorkspace,
                                   loadedExtensions)
  }
}

object RenameExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val manager = getExperimentManager(context)
    val current = manager.getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val name = args(0).getString.trim

    if (name.isEmpty)
      return nameError(context, "emptyName")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    manager.removeExperiment(protocol.name)

    protocol.name = name

    manager.addExperiment(Experiment(protocol, ExperimentType.Code))
    manager.setCurrentExperiment(protocol.name)
  }
}

object DuplicateExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val manager = getExperimentManager(context)
    val current = manager.getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val name = args(0).getString.trim

    if (name.isEmpty)
      return nameError(context, "emptyName")

    if (manager.getExperiment(name).isDefined)
      return nameError(context, "alreadyExists", name)

    manager.addExperiment(Experiment(current.get.protocol.copy(name), ExperimentType.Code))
  }
}

object ImportExperiments extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val path = args(0).getString.trim
    val manager = getExperimentManager(context)

    val loader = FileFormat.standardAnyLoader(context.workspace.isHeadless,
                                              context.asInstanceOf[ExtensionContext].workspace.compiler.utilities,
                                              true)

    loader.readExperiments(Source.fromFile(path).mkString, true, Set()) match {
      case Success((protocols, _)) =>
        protocols.foreach { protocol =>
          if (manager.getExperiment(protocol.name).isDefined) {
            nameError(context, "alreadyExists", protocol.name)
          } else {
            manager.addExperiment(Experiment(protocol, ExperimentType.Code))
          }
        }

      case _ =>
        nameError(context, "invalidFormat", path)
    }
  }
}

object ExportExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType, BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val manager = getExperimentManager(context)
    val current = manager.getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val path = args(0).getString.trim

    if (!args(1).getBooleanValue && new File(path).exists)
      return nameError(context, "fileExists", path)

    val loader = FileFormat.standardAnyLoader(context.workspace.isHeadless,
                                              context.asInstanceOf[ExtensionContext].workspace.compiler.utilities,
                                              true)

    val out = new PrintWriter(path)

    val result = loader.writeExperiments(Seq(current.get.protocol), out)

    out.close()

    if (result.isFailure)
      nameError(context, "exportFailed")
  }
}

object ClearExperiments extends Command {
  override def getSyntax = {
    commandSyntax()
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getExperimentManager(context).clearExperiments()
  }
}

object SetCurrentExperiment extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val name = args(0).getString.trim
    val manager = getExperimentManager(context)

    if (manager.getExperiment(name).isEmpty)
      return nameError(context, "noExperiment", name)

    manager.setCurrentExperiment(name)
  }
}

object PrintExperiments extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    val (code, gui) = getExperimentManager(context).allExperiments.partition(_.tpe == ExperimentType.Code)

    var result = "Code Experiments:\n"

    code.map(_.protocol.name).sorted.foreach {
      result += "\t" + _ + "\n"
    }

    result += "GUI Experiments:\n"

    gui.map(_.protocol.name).sorted.foreach {
      result += "\t" + _ + "\n"
    }

    result
  }
}

object GetExperimentList extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = ListType)
  }

  override def report(args: Array[Argument], context: Context): LogoList =
    LogoList(getExperimentManager(context).allExperiments.map(_.protocol.name).sorted*)
}

object GetCurrentExperiment extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String =
    getExperimentManager(context).getCurrentExperiment.map(_.protocol.name).getOrElse("")
}

object GetParameters extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    val name = args(0).getString.trim
    val experiment = getExperimentManager(context).getExperiment(name)

    if (experiment.isEmpty) {
      nameError(context, "noExperiment", name)

      return ""
    }

    val protocol = experiment.get.protocol

    var result = "EXPERIMENT PARAMETERS:\n\n"

    result += "Variable values:\n"

    LabVariableParser.combineVariables(protocol.constants, protocol.subExperiments).split("\n").foreach {
      result += "  " + _ + "\n"
    }

    result += s"Repetitions: ${protocol.repetitions}\n"
    result += s"Sequential run order: ${protocol.sequentialRunOrder}\n"

    if (protocol.metrics.nonEmpty)
      result += s"Metrics:\n  ${protocol.metrics.mkString("\n  ")}\n"

    result += s"Run metrics every step: ${protocol.runMetricsEveryStep}\n"

    if (protocol.runMetricsCondition.nonEmpty)
      result += s"Run metrics condition:\n  ${protocol.runMetricsCondition}\n"

    if (protocol.preExperimentCommands.nonEmpty)
      result += s"Pre experiment commands:\n  ${protocol.preExperimentCommands}\n"

    if (protocol.setupCommands.nonEmpty)
      result += s"Setup commands:\n  ${protocol.setupCommands}\n"

    if (protocol.goCommands.nonEmpty)
      result += s"Go commands:\n  ${protocol.goCommands}\n"

    if (protocol.postRunCommands.nonEmpty)
      result += s"Post run commands:\n  ${protocol.postRunCommands}\n"

    if (protocol.postExperimentCommands.nonEmpty)
      result += s"Post experiment commands:\n ${protocol.postExperimentCommands}\n"

    if (protocol.exitCondition.nonEmpty)
      result += s"Stop condtion:\n ${protocol.exitCondition}\n"

    result += s"Time limit: ${protocol.timeLimit}\n\n"

    result += "RUN OPTIONS:\n\n"

    if (protocol.spreadsheet.nonEmpty)
      result += s"Spreadsheet: ${protocol.spreadsheet}\n"

    if (protocol.table.nonEmpty)
      result += s"Table: ${protocol.table}\n"

    if (protocol.stats.nonEmpty)
      result += s"Stats: ${protocol.stats}\n"

    if (protocol.lists.nonEmpty)
      result += s"Lists: ${protocol.lists}\n"

    result += s"Update view: ${protocol.updateView}\n"
    result += s"Update plots: ${protocol.updatePlotsAndMonitors}\n"
    result += s"Parallel runs: ${protocol.threadCount}\n"
    result += s"Mirror headless output: ${protocol.mirrorHeadlessOutput}\n"

    result
  }
}

object ExperimentExists extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean =
    getExperimentManager(context).getExperiment(args(0).getString.trim).isDefined
}

object ValidExperimentName extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    val name = args(0).getString.trim

    name.nonEmpty && getExperimentManager(context).getExperiment(name).isEmpty
  }
}
