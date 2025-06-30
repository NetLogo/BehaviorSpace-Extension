// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Command, Context, LabProtocol, LabVariableParser, RefValueSet }
import org.nlogo.core.Syntax._
import org.nlogo.nvm.{ ExperimentType, Procedure }
import org.nlogo.workspace.AbstractWorkspace

import scala.util.{ Failure, Success }

import BehaviorSpaceExtension._

// helper class for ensuring that a current experiment exists and is valid for editing (Isaac B 6/29/25)
abstract class CurrentGuard extends Command {
  protected def getCurrentExperiment(context: Context): Option[LabProtocol] = {
    val current = getExperimentManager(context).getCurrentExperiment

    if (current.isEmpty) {
      nameError(context, "noCurrent")

      return None
    }

    val protocol = current.get.protocol

    if (current.get.tpe != ExperimentType.Code) {
      nameError(context, protocol.name)

      return None
    }

    Option(protocol)
  }
}

object SetPreExperimentCommands extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.preExperimentCommands = extractSource(args(0).getCommand))
  }
}

object SetSetupCommands extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.setupCommands = extractSource(args(0).getCommand))
  }
}

object SetGoCommands extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.goCommands = extractSource(args(0).getCommand))
  }
}

object SetPostRunCommands extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.postRunCommands = extractSource(args(0).getCommand))
  }
}

object SetPostExperimentCommands extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(CommandType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.postExperimentCommands = extractSource(args(0).getCommand))
  }
}

object SetRepetitions extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.repetitions = args(0).getIntValue)
  }
}

object SetSequentialRunOrder extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.sequentialRunOrder = args(0).getBooleanValue)
  }
}

object SetRunMetricsEveryStep extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.runMetricsEveryStep = args(0).getBooleanValue)
  }
}

object SetRunMetricsCondition extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(ReporterType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.runMetricsCondition = extractSource(args(0).getReporter))
  }
}

object SetTimeLimit extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.timeLimit = args(0).getIntValue)
  }
}

object SetStopCondition extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(ReporterType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.exitCondition = extractSource(args(0).getReporter))
  }
}

object SetMetrics extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(ReporterType | RepeatableType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.metrics = args.map(x => extractSource(x.getReporter)).toList)
  }
}

object SetVariables extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(ListType | StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach { protocol =>
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
}

object SetParallelRuns extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(NumberType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.threadCount = args(0).getIntValue)
  }
}

object SetTable extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.table = args(0).getString)
  }
}

object SetSpreadsheet extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.spreadsheet = args(0).getString)
  }
}

object SetStats extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.stats = args(0).getString)
  }
}

object SetLists extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(StringType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.lists = args(0).getString)
  }
}

object SetMirrorHeadlessOutput extends CurrentGuard {
  override def getSyntax = {
    commandSyntax(right = List(BooleanType))
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    getCurrentExperiment(context).foreach(_.mirrorHeadlessOutput = args(0).getBooleanValue)
  }
}
