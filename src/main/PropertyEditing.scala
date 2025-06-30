// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Command, Context, LabProtocol, LabVariableParser, RefValueSet }
import org.nlogo.core.Syntax._
import org.nlogo.nvm.{ ExperimentType, Procedure }
import org.nlogo.workspace.AbstractWorkspace

import scala.util.{ Failure, Success }

import BehaviorSpaceExtension._

object SetPreExperimentCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.preExperimentCommands = extractSource(args(0).getCommand)
  }
}

object SetSetupCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.setupCommands = extractSource(args(0).getCommand)
  }
}

object SetGoCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.goCommands = extractSource(args(0).getCommand)
  }
}

object SetPostRunCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.postRunCommands = extractSource(args(0).getCommand)
  }
}

object SetPostExperimentCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.postExperimentCommands = extractSource(args(0).getCommand)
  }
}

object SetRepetitions extends Command {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.repetitions = args(0).getIntValue
  }
}

object SetSequentialRunOrder extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.sequentialRunOrder = args(0).getBooleanValue
  }
}

object SetRunMetricsEveryStep extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.runMetricsEveryStep = args(0).getBooleanValue
  }
}

object SetRunMetricsCondition extends Command {
  override def getSyntax = {
    commandSyntax(right = List(ReporterType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.runMetricsCondition = extractSource(args(0).getReporter)
  }
}

object SetTimeLimit extends Command {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.timeLimit = args(0).getIntValue
  }
}

object SetStopCondition extends Command {
  override def getSyntax = {
    commandSyntax(right = List(ReporterType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.exitCondition = extractSource(args(0).getReporter)
  }
}

object SetMetrics extends Command {
  override def getSyntax = {
    commandSyntax(right = List(ReporterType | RepeatableType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.metrics = args.map(x => extractSource(x.getReporter)).toList
  }
}

object SetVariables extends Command {
  override def getSyntax = {
    commandSyntax(right = List(ListType | StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    LabVariableParser.parseVariables(args(0).getList.mkString("\n"), protocol.repetitions, context.workspace.world,
                                     context.workspace.asInstanceOf[AbstractWorkspace]) match {
      case Success((constants, subExperiments)) =>
        protocol.constants = constants
        protocol.subExperiments = subExperiments
      case Failure(t) =>
        nameError(context, t.getMessage)
    }
  }
}

object SetParallelRuns extends Command {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.threadCount = args(0).getIntValue
  }
}

object SetTable extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.table = args(0).getString
  }
}

object SetSpreadsheet extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.spreadsheet = args(0).getString
  }
}

object SetStats extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.stats = args(0).getString
  }
}

object SetLists extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.lists = args(0).getString
  }
}

object SetMirrorHeadlessOutput extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty)
      return nameError(context, "noCurrent")

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code)
      return nameError(context, "gui", protocol.name)

    protocol.mirrorHeadlessOutput = args(0).getBooleanValue
  }
}
