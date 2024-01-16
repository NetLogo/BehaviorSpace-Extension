// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Command, Context, LabProtocol, LabVariableParser, RefValueSet }
import org.nlogo.core.Syntax._

object SetPreExperimentCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")

    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).preExperimentCommands = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetSetupCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).setupCommands = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetGoCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).goCommands = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetPostRunCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).postRunCommands = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetPostExperimentCommands extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).postExperimentCommands = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetRepetitions extends Command {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).repetitions = args(0).getIntValue

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetSequentialRunOrder extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).sequentialRunOrder = args(0).getBooleanValue

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetRunMetricsEveryStep extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).runMetricsEveryStep = args(0).getBooleanValue

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetRunMetricsCondition extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).runMetricsCondition = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetTimeLimit extends Command {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).timeLimit = args(0).getIntValue

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetStopCondition extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).exitCondition = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetMetrics extends Command {
  override def getSyntax = {
    commandSyntax(right = List(ListType | StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).metrics = args(0).getList.toList.map(_.toString)

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetVariables extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    LabVariableParser.parseVariables(args(0).getString,
                                     BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).repetitions,
                                     context.workspace.world,
                                     context.workspace.asInstanceOf[org.nlogo.workspace.AbstractWorkspace]) match {
      case (Some((constants: List[RefValueSet], subExperiments: List[List[RefValueSet]])), _) =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).constants = constants
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).subExperiments = subExperiments
      case (None, message: String) =>
        BehaviorSpaceExtension.nameError(context, message)
    }

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetParallelRuns extends Command {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).threadCount = args(0).getIntValue

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetTable extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).table = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetSpreadsheet extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).spreadsheet = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetStats extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).stats = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetLists extends Command {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).lists = args(0).getString

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetUpdateView extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).updateView = args(0).getBooleanValue

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}

object SetUpdatePlots extends Command {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context) {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty)
      return BehaviorSpaceExtension.nameError(context, "noCurrent")
        
    if (!BehaviorSpaceExtension.validateForEditing(BehaviorSpaceExtension.currentExperiment, context)) return

    BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).updatePlotsAndMonitors = args(0).getBooleanValue

    if (BehaviorSpaceExtension.savedExperiments.contains(BehaviorSpaceExtension.currentExperiment))
      BehaviorSpaceExtension.savedExperiments -= BehaviorSpaceExtension.currentExperiment
  }
}
