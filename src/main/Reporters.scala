// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Command, Context, LabDefaultValues, LabVariableParser, Reporter }
import org.nlogo.core.LogoList
import org.nlogo.core.Syntax._
import org.nlogo.nvm.Experiment
import org.nlogo.swing.BrowserLauncher
import org.nlogo.window.GUIWorkspace

import BehaviorSpaceExtension._

object GotoBehaviorspaceDocumentation extends Command {
  override def getSyntax = {
    commandSyntax()
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    BrowserLauncher.openPath(BrowserLauncher.docPath("behaviorspace.html"), "")
  }
}

object GotoBspaceExtensionDocumentation extends Command {
  override def getSyntax = {
    commandSyntax()
  }

  def perform(args: Array[Argument], context: Context): Unit = {
    BrowserLauncher.openPath(BrowserLauncher.docPath("bspace.html"), "")
  }
}

object GetPreExperimentCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.preExperimentCommands

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetSetupCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.setupCommands

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetGoCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.goCommands

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetPostRunCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.postRunCommands

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetPostExperimentCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.postExperimentCommands

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetRepetitions extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.repetitions.toDouble

      case _ =>
        nameError(context, "noCurrent")

        0
    }
  }
}

object GetSequentialRunOrder extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.sequentialRunOrder

      case _ =>
        nameError(context, "noCurrent")

        false
    }
  }
}

object GetRunMetricsEveryStep extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.runMetricsEveryStep

      case _ =>
        nameError(context, "noCurrent")

        false
    }
  }
}

object GetRunMetricsCondition extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.runMetricsCondition

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetTimeLimit extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.timeLimit.toDouble

      case _ =>
        nameError(context, "noCurrent")

        0
    }
  }
}

object GetStopCondition extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.exitCondition

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetMetrics extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = ListType | StringType)
  }

  override def report(args: Array[Argument], context: Context): LogoList = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        LogoList(protocol.metrics*)

      case _ =>
        nameError(context, "noCurrent")

        LogoList()
    }
  }
}

object GetVariables extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        LabVariableParser.combineVariables(protocol.constants, protocol.subExperiments)

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetParallelRuns extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.threadCount.toDouble

      case _ =>
        nameError(context, "noCurrent")

        0
    }
  }
}

object GetTable extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.table

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetSpreadsheet extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.spreadsheet

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetStats extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.stats

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetLists extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.lists

      case _ =>
        nameError(context, "noCurrent")

        ""
    }
  }
}

object GetDefaultParallelRuns extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    LabDefaultValues.getDefaultThreads.toDouble
  }
}

object GetRecommendedMaxParallelRuns extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    LabDefaultValues.getRecommendedMaxThreads.toDouble
  }
}

object GetMirrorHeadlessOutput extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    getExperimentManager(context).getCurrentExperiment match {
      case Some(Experiment(protocol, _)) =>
        protocol.mirrorHeadlessOutput

      case _ =>
        nameError(context, "noCurrent")

        false
    }
  }
}
