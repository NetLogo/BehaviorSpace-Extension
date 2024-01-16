// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Command, Context, LabDefaultValues, LabVariableParser, Reporter }
import org.nlogo.core.LogoList
import org.nlogo.core.Syntax._
import org.nlogo.swing.BrowserLauncher
import org.nlogo.window.GUIWorkspace

object GotoBehaviorspaceDocumentation extends Command {
  override def getSyntax = {
    commandSyntax()
  }

  def perform(args: Array[Argument], context: Context) {
    BrowserLauncher.openPath(BrowserLauncher.docPath("behaviorspace.html"), "")
  }
}

object GotoBspaceExtensionDocumentation extends Command {
  override def getSyntax = {
    commandSyntax()
  }

  def perform(args: Array[Argument], context: Context) {
    // will fill in once i know the link
  }
}

object GetCurrentExperiment extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    BehaviorSpaceExtension.currentExperiment
  }
}

object GetPreExperimentCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.preExperimentCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).preExperimentCommands
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetSetupCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.setupCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).setupCommands
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetGoCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.goCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).goCommands
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetPostRunCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.postRunCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).postRunCommands
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetPostExperimentCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.postExperimentCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).postExperimentCommands
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetRepetitions extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return LabDefaultValues.getDefaultRepetitions
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.repetitions
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).repetitions
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        LabDefaultValues.getDefaultRepetitions
    }
  }
}

object GetSequentialRunOrder extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return LabDefaultValues.getDefaultSequentialRunOrder
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.sequentialRunOrder
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).sequentialRunOrder
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        LabDefaultValues.getDefaultSequentialRunOrder
    }
  }
}

object GetRunMetricsEveryStep extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return LabDefaultValues.getDefaultRunMetricsEveryStep
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.runMetricsEveryStep
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).runMetricsEveryStep
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        LabDefaultValues.getDefaultRunMetricsEveryStep
    }
  }
}

object GetRunMetricsCondition extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.runMetricsCondition
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).runMetricsCondition
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetTimeLimit extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return LabDefaultValues.getDefaultTimeLimit
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.timeLimit
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).timeLimit
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        LabDefaultValues.getDefaultTimeLimit
    }
  }
}

object GetStopCondition extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.exitCondition
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).exitCondition
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetMetrics extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = ListType | StringType)
  }

  override def report(args: Array[Argument], context: Context): LogoList = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return LogoList()
    }

    val list = BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.metrics
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).metrics
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        Nil
    }

    return LogoList.fromList(list)
  }
}

object GetVariables extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }

    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        LabVariableParser.combineVariables(
          context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.constants,
          context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.subExperiments)
      case ExperimentType.Code =>
        LabVariableParser.combineVariables(
          BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).constants,
          BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).subExperiments)
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetParallelRuns extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return LabDefaultValues.getDefaultThreads
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.runOptions.threadCount
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).threadCount
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        LabDefaultValues.getDefaultThreads
    }
  }
}

object GetTable extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.runOptions.table
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).table
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetSpreadsheet extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.runOptions.spreadsheet
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).spreadsheet
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetStats extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.runOptions.stats
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).stats
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetLists extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return ""
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.runOptions.lists
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).lists
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        ""
    }
  }
}

object GetUpdateView extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return LabDefaultValues.getDefaultUpdateView
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.runOptions.updateView
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).updateView
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        LabDefaultValues.getDefaultUpdateView
    }
  }
}

object GetUpdatePlots extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    if (BehaviorSpaceExtension.currentExperiment.trim.isEmpty) {
      BehaviorSpaceExtension.nameError(context, "noCurrent")
      return LabDefaultValues.getDefaultUpdatePlotsAndMonitors
    }
        
    return BehaviorSpaceExtension.experimentType(BehaviorSpaceExtension.currentExperiment, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == BehaviorSpaceExtension.currentExperiment).get.runOptions.updatePlotsAndMonitors
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(BehaviorSpaceExtension.currentExperiment).updatePlotsAndMonitors
      case _ =>
        BehaviorSpaceExtension.nameError(context, "noExperiment", BehaviorSpaceExtension.currentExperiment)
        LabDefaultValues.getDefaultUpdatePlotsAndMonitors
    }
  }
}

object GetDefaultParallelRuns extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    LabDefaultValues.getDefaultThreads
  }
}

object GetRecommendedMaxParallelRuns extends Reporter {
  override def getSyntax = {
    reporterSyntax(ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    LabDefaultValues.getRecommendedMaxThreads
  }
}
