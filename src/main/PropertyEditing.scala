// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Command, Context, LabProtocol, LabVariableParser, RefValueSet }
import org.nlogo.core.Syntax._
import org.nlogo.nvm.Procedure

import scala.util.{ Failure, Success }

object SetPreExperimentCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).preExperimentCommands =
      BehaviorSpaceExtension.extractSource(args(0).getCommand)
  }
}

object SetSetupCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).setupCommands =
      BehaviorSpaceExtension.extractSource(args(0).getCommand)
  }
}

object SetGoCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).goCommands =
      BehaviorSpaceExtension.extractSource(args(0).getCommand)
  }
}

object SetPostRunCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).postRunCommands =
      BehaviorSpaceExtension.extractSource(args(0).getCommand)
  }
}

object SetPostExperimentCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).postExperimentCommands =
      BehaviorSpaceExtension.extractSource(args(0).getCommand)
  }
}

object SetRepetitions extends Command {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).repetitions = args(0).getIntValue
  }
}

object SetSequentialRunOrder extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).sequentialRunOrder = args(0).getBooleanValue
  }
}

object SetRunMetricsEveryStep extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).runMetricsEveryStep = args(0).getBooleanValue
  }
}

object SetRunMetricsCondition extends Command {
  override def getSyntax = {
    commandSyntax(right = List(ReporterType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).runMetricsCondition =
      BehaviorSpaceExtension.extractSource(args(0).getReporter)
  }
}

object SetTimeLimit extends Command {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).timeLimit = args(0).getIntValue
  }
}

object SetStopCondition extends Command {
  override def getSyntax = {
    commandSyntax(right = List(ReporterType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).exitCondition =
      BehaviorSpaceExtension.extractSource(args(0).getReporter)
  }
}

object SetMetrics extends Command {
  override def getSyntax = {
    commandSyntax(right = List(ReporterType | RepeatableType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).metrics =
      args.map(x => BehaviorSpaceExtension.extractSource(x.getReporter)).toList
  }
}

object SetVariables extends Command {
  override def getSyntax = {
    commandSyntax(right = List(ListType | StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    LabVariableParser.parseVariables(args(0).getList.mkString("\n"),
                                     BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).repetitions,
                                     context.workspace.world,
                                     context.workspace.asInstanceOf[org.nlogo.workspace.AbstractWorkspace]) match {
      case Success((constants, subExperiments)) =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).constants = constants
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).subExperiments = subExperiments
      case Failure(t) =>
        return BehaviorSpaceExtension.nameError(context, t.getMessage)
    }
  }
}

object SetParallelRuns extends Command {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).threadCount = args(0).getIntValue
  }
}

object SetTable extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).table = args(0).getString
  }
}

object SetSpreadsheet extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).spreadsheet = args(0).getString
  }
}

object SetStats extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).stats = args(0).getString
  }
}

object SetLists extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).lists = args(0).getString
  }
}

object SetUpdateView extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).updateView = args(0).getBooleanValue
  }
}

object SetUpdatePlots extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).updatePlotsAndMonitors = args(0).getBooleanValue
  }
}

object SetMirrorHeadlessOutput extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    if (BehaviorSpaceExtension.currentExperiment.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).mirrorHeadlessOutput = args(0).getBooleanValue
  }
}
