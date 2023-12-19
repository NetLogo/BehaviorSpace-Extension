// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Command, Context, LabDefaultValues, LabVariableParser, Reporter }
import org.nlogo.core.I18N
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

object GetPreExperimentCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.preExperimentCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).preExperimentCommands
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetSetupCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.setupCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).setupCommands
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetGoCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.goCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).goCommands
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetPostRunCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.postRunCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).postRunCommands
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetPostExperimentCommands extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.postExperimentCommands
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).postExperimentCommands
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetRepetitions extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.repetitions
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).repetitions
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        LabDefaultValues.getDefaultRepetitions
    }
  }
}

object GetSequentialRunOrder extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.sequentialRunOrder
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).sequentialRunOrder
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        LabDefaultValues.getDefaultSequentialRunOrder
    }
  }
}

object GetRunMetricsEveryStep extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.runMetricsEveryStep
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).runMetricsEveryStep
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        LabDefaultValues.getDefaultRunMetricsEveryStep
    }
  }
}

object GetRunMetricsCondition extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.runMetricsCondition
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).runMetricsCondition
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetTimeLimit extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.timeLimit
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).timeLimit
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        LabDefaultValues.getDefaultTimeLimit
    }
  }
}

object GetStopCondition extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.exitCondition
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).exitCondition
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetMetrics extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = ListType | StringType)
  }

  override def report(args: Array[Argument], context: Context): List[String] = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.metrics
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).metrics
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        Nil
    }
  }
}

object GetVariables extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        LabVariableParser.combineVariables(
          context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.constants,
          context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.subExperiments)
      case ExperimentType.Code =>
        LabVariableParser.combineVariables(
          BehaviorSpaceExtension.experiments(args(0).getString).constants,
          BehaviorSpaceExtension.experiments(args(0).getString).subExperiments)
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetParallelRuns extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = NumberType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Double = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.runOptions.threadCount
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).threadCount
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        LabDefaultValues.getDefaultThreads
    }
  }
}

object GetTable extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.runOptions.table
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).table
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetSpreadsheet extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.runOptions.spreadsheet
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).spreadsheet
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetStats extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.runOptions.stats
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).stats
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetLists extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = StringType)
  }

  override def report(args: Array[Argument], context: Context): String = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.runOptions.lists
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).lists
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        ""
    }
  }
}

object GetUpdateView extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.runOptions.updateView
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).updateView
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
        LabDefaultValues.getDefaultUpdateView
    }
  }
}

object GetUpdatePlots extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = BooleanType)
  }

  override def report(args: Array[Argument], context: Context): java.lang.Boolean = {
    return BehaviorSpaceExtension.experimentType(args(0).getString, context) match {
      case ExperimentType.GUI =>
        context.workspace.getBehaviorSpaceExperiments.find(x => x.name == args(0).getString).get.runOptions.updatePlotsAndMonitors
      case ExperimentType.Code =>
        BehaviorSpaceExtension.experiments(args(0).getString).updatePlotsAndMonitors
      case _ =>
        BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment",
                                                       args(0).getString), context)
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

object GetReturnValue extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType), ret = WildcardType)
  }

  override def report(args: Array[Argument], context: Context): AnyRef = {
    val values = context.workspace.asInstanceOf[GUIWorkspace].getBehaviorSpaceReturnValues
    
    if (!values.contains(args(0).getString)) {
      BehaviorSpaceExtension.nameError(I18N.gui.getN("tools.behaviorSpace.extension.noReturn", args(0).getString), context)

      return null
    }
    
    return values(args(0).getString)
  }
}
