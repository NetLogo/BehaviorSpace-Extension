// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import java.io.{ File, FileWriter, PrintWriter }

import org.nlogo.api.{ Argument, Command, Context, LabProtocol, LabVariableParser, Reporter }
import org.nlogo.core.LogoList
import org.nlogo.core.Syntax._
import org.nlogo.fileformat.{ LabLoader, LabSaver }
import org.nlogo.headless.Main
import org.nlogo.lab.Worker
import org.nlogo.nvm.{ Experiment, ExperimentType, ExtensionContext, LabInterface }
import org.nlogo.workspace.AbstractWorkspace

import scala.io.Source

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

    try {
      for (protocol <- new LabLoader(context.workspace.asInstanceOf[AbstractWorkspace].compiler.utilities)
                                    (Source.fromFile(path).mkString, true,
                                     scala.collection.mutable.Set[String]())) {
        if (manager.getExperiment(protocol.name).isDefined) {
          nameError(context, "alreadyExists", protocol.name)
        } else {
          manager.addExperiment(Experiment(protocol, ExperimentType.Code))
        }
      }
    } catch {
      case e: org.xml.sax.SAXParseException =>
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

    val out = new PrintWriter(path)

    out.write(s"${LabLoader.XMLVER}\n${LabLoader.DOCTYPE}\n")
    out.write(LabSaver.save(List(current.get.protocol)))

    out.close()
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
      result += "\t" + _ + "\n"
    }

    result += "Repetitions:\n\t" + protocol.repetitions.toString + "\n"
    result += "Sequential run order:\n\t" + protocol.sequentialRunOrder.toString + "\n"
    result += "Metrics:\n\t" + protocol.metrics.toString + "\n"
    result += "Run metrics every step:\n\t" + protocol.runMetricsEveryStep.toString + "\n"
    result += "Run metrics condition:\n\t" + protocol.runMetricsCondition + "\n"
    result += "Pre experiment commands:\n\t" + protocol.preExperimentCommands + "\n"
    result += "Setup commands:\n\t" + protocol.setupCommands + "\n"
    result += "Go commands:\n\t" + protocol.goCommands + "\n"
    result += "Post run commands:\n\t" + protocol.postRunCommands + "\n"
    result += "Post experiment commands:\n\t" + protocol.postExperimentCommands + "\n"
    result += "Stop condtion:\n\t" + protocol.exitCondition + "\n"
    result += "Time limit:\n\t" + protocol.timeLimit.toString + "\n\n"

    result += "RUN OPTIONS:\n\n"

    result += "Spreadsheet:\n\t" + protocol.spreadsheet + "\n"
    result += "Table:\n\t" + protocol.table + "\n"
    result += "Stats:\n\t" + protocol.stats + "\n"
    result += "Lists:\n\t" + protocol.lists + "\n"
    result += "Update view:\n\t" + protocol.updateView.toString + "\n"
    result += "Update plots:\n\t" + protocol.updatePlotsAndMonitors.toString + "\n"
    result += "Parallel runs:\n\t" + protocol.threadCount.toString + "\n"
    result += "Mirror headless output:\n\t" + protocol.mirrorHeadlessOutput.toString + "\n"

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
