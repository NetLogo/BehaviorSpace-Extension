// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import java.lang.{ Boolean => JBoolean, Double => JDouble }

import org.nlogo.api.{ Argument, Command, Context, LabDefaultValues, LabProtocol, LabVariableParser, Reporter }
import org.nlogo.core.LogoList
import org.nlogo.core.Syntax
import org.nlogo.nvm.Experiment
import org.nlogo.swing.BrowserLauncher

import BehaviorSpaceExtension._

object GotoBehaviorspaceDocumentation extends Command {
  override def getSyntax: Syntax =
    Syntax.commandSyntax()

  def perform(args: Array[Argument], context: Context): Unit = {
    BrowserLauncher.openPath(BrowserLauncher.docPath("behaviorspace.html"), "")
  }
}

object GotoBspaceExtensionDocumentation extends Command {
  override def getSyntax: Syntax =
    Syntax.commandSyntax()

  def perform(args: Array[Argument], context: Context): Unit = {
    BrowserLauncher.openPath(BrowserLauncher.docPath("bspace.html"), "")
  }
}

// helper class for ensuring that a current experiment exists (Isaac B 6/30/25)
abstract class CurrentGuardReporter extends Reporter {
  protected def getCurrentExperiment(context: Context): Option[LabProtocol] = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        Option(protocol)

      case _ =>
        nameError(context, "noCurrent")

        None
    }
  }
}

object GetPreExperimentCommands extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.preExperimentCommands).getOrElse("")
}

object GetSetupCommands extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.setupCommands).getOrElse("")
}

object GetGoCommands extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.goCommands).getOrElse("")
}

object GetPostRunCommands extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.postRunCommands).getOrElse("")
}

object GetPostExperimentCommands extends CurrentGuardReporter {
 override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.postExperimentCommands).getOrElse("")
}

object GetRepetitions extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.NumberType)

  override def report(args: Array[Argument], context: Context): JDouble =
    getCurrentExperiment(context).map(_.repetitions.toDouble).getOrElse(0.0)
}

object GetSequentialRunOrder extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.BooleanType)

  override def report(args: Array[Argument], context: Context): JBoolean =
    getCurrentExperiment(context).map(_.sequentialRunOrder).getOrElse(false)
}

object GetRunMetricsEveryStep extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.BooleanType)

  override def report(args: Array[Argument], context: Context): JBoolean =
    getCurrentExperiment(context).map(_.runMetricsEveryStep).getOrElse(false)
}

object GetRunMetricsCondition extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.runMetricsCondition).getOrElse("")
}

object GetTimeLimit extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.NumberType)

  override def report(args: Array[Argument], context: Context): JDouble =
    getCurrentExperiment(context).map(_.timeLimit.toDouble).getOrElse(0.0)
}

object GetStopCondition extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.exitCondition).getOrElse("")
}

object GetMetrics extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.ListType | Syntax.StringType)

  override def report(args: Array[Argument], context: Context): LogoList =
    getCurrentExperiment(context).map(p => LogoList(p.metrics*)).getOrElse(LogoList())
}

object GetVariables extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String = {
    getCurrentExperiment(context).map { protocol =>
      LabVariableParser.combineVariables(protocol.constants, protocol.subExperiments)
    }.getOrElse("")
  }
}

object GetParallelRuns extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.NumberType)

  override def report(args: Array[Argument], context: Context): JDouble =
    getCurrentExperiment(context).map(_.threadCount.toDouble).getOrElse(0.0)
}

object GetTable extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.table).getOrElse("")
}

object GetSpreadsheet extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.spreadsheet).getOrElse("")
}

object GetStats extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.stats).getOrElse("")
}

object GetLists extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.StringType)

  override def report(args: Array[Argument], context: Context): String =
    getCurrentExperiment(context).map(_.lists).getOrElse("")
}

object GetDefaultParallelRuns extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.NumberType)

  override def report(args: Array[Argument], context: Context): JDouble =
    LabDefaultValues.getDefaultThreads.toDouble
}

object GetRecommendedMaxParallelRuns extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.NumberType)

  override def report(args: Array[Argument], context: Context): JDouble =
    LabDefaultValues.getRecommendedMaxThreads.toDouble
}

object GetMirrorHeadlessOutput extends CurrentGuardReporter {
  override def getSyntax: Syntax =
    Syntax.reporterSyntax(ret = Syntax.BooleanType)

  override def report(args: Array[Argument], context: Context): JBoolean =
    getCurrentExperiment(context).map(_.mirrorHeadlessOutput).getOrElse(false)
}
