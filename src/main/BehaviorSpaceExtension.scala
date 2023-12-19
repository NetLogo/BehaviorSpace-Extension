// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Command, Context, DefaultClassManager, LabDefaultValues, LabProtocol,
                       LabRunOptions, PrimitiveManager, RefValueSet }
import org.nlogo.core.I18N
import org.nlogo.window.GUIWorkspace

import javax.swing.JOptionPane

import scala.collection.mutable.Map

class ExperimentData {
  var name = ""
  var preExperimentCommands = ""
  var setupCommands = ""
  var goCommands = ""
  var postRunCommands = ""
  var postExperimentCommands = ""
  var repetitions = LabDefaultValues.getDefaultRepetitions
  var sequentialRunOrder = LabDefaultValues.getDefaultSequentialRunOrder
  var runMetricsEveryStep = LabDefaultValues.getDefaultRunMetricsEveryStep
  var runMetricsCondition = ""
  var timeLimit = LabDefaultValues.getDefaultTimeLimit
  var exitCondition = ""
  var metrics: List[String] = Nil
  var constants: List[RefValueSet] = Nil
  var subExperiments: List[List[RefValueSet]] = Nil
  var returnReporters = Map[String, String]()
  var threadCount = LabDefaultValues.getDefaultThreads
  var table = ""
  var spreadsheet = ""
  var stats = ""
  var lists = ""
  var updateView = LabDefaultValues.getDefaultUpdateView
  var updatePlotsAndMonitors = LabDefaultValues.getDefaultUpdatePlotsAndMonitors
}

object ExperimentType extends Enumeration {
  type ExperimentType = Value
  val GUI, Code, None = Value
}

object BehaviorSpaceExtension {
  val experiments = Map[String, ExperimentData]()
  val savedExperiments = Map[String, LabProtocol]()

  def experimentType(name: String, context: Context): ExperimentType.ExperimentType = {
    if (experiments.contains(name))
      ExperimentType.Code
    else if (context.workspace.getBehaviorSpaceExperiments.find(x => x.name == name).isDefined)
      ExperimentType.GUI
    else
      ExperimentType.None
  }

  def validateForEditing(name: String, context: Context): Boolean = {
    return experimentType(name, context) match {
      case ExperimentType.None =>
        nameError(I18N.gui.getN("tools.behaviorSpace.extension.noExperiment", name), context)
        false
      case ExperimentType.GUI =>
        nameError(I18N.gui.getN("tools.behaviorSpace.extension.guiExperiment", name), context)
        false
      case ExperimentType.Code => true
    }
  }

  def nameError(message: String, context: Context) {
    JOptionPane.showMessageDialog(context.workspace.asInstanceOf[GUIWorkspace].getFrame,
                                  message,
                                  I18N.gui.get("tools.behaviorSpace.invalid"),
                                  JOptionPane.ERROR_MESSAGE)
  }

  def dataFromProtocol(protocol: LabProtocol): ExperimentData = {
    val data = new ExperimentData()

    data.name = protocol.name
    data.preExperimentCommands = protocol.preExperimentCommands
    data.setupCommands = protocol.setupCommands
    data.goCommands = protocol.goCommands
    data.postRunCommands = protocol.postRunCommands
    data.postExperimentCommands = protocol.postExperimentCommands
    data.repetitions = protocol.repetitions
    data.sequentialRunOrder = protocol.sequentialRunOrder
    data.runMetricsEveryStep = protocol.runMetricsEveryStep
    data.runMetricsCondition = protocol.runMetricsCondition
    data.timeLimit = protocol.timeLimit
    data.exitCondition = protocol.exitCondition
    data.metrics = protocol.metrics
    data.constants = protocol.constants
    data.subExperiments = protocol.subExperiments
    data.threadCount = protocol.runOptions.threadCount
    data.table = protocol.runOptions.table
    data.spreadsheet = protocol.runOptions.spreadsheet
    data.stats = protocol.runOptions.stats
    data.lists = protocol.runOptions.lists
    data.updateView = protocol.runOptions.updateView
    data.updatePlotsAndMonitors = protocol.runOptions.updatePlotsAndMonitors

    data
  }

  def protocolFromData(data: ExperimentData): LabProtocol = {
    new LabProtocol(data.name, data.preExperimentCommands, data.setupCommands, data.goCommands, data.postRunCommands,
                    data.postExperimentCommands, data.repetitions, data.sequentialRunOrder, data.runMetricsEveryStep,
                    data.runMetricsCondition, data.timeLimit, data.exitCondition, data.metrics, data.constants,
                    data.subExperiments, data.returnReporters.toMap,
                    runOptions = new LabRunOptions(data.threadCount, data.table, data.spreadsheet, data.stats,
                                                   data.lists, data.updateView, data.updatePlotsAndMonitors))
  }
}

class BehaviorSpaceExtension extends DefaultClassManager {
  def load(manager: PrimitiveManager) {
    manager.addPrimitive("create-experiment", CreateExperiment)
    manager.addPrimitive("delete-experiment", DeleteExperiment)
    manager.addPrimitive("run-experiment", RunExperiment)
    manager.addPrimitive("rename-experiment", RenameExperiment)
    manager.addPrimitive("duplicate-experiment", DuplicateExperiment)
    manager.addPrimitive("import-experiment", ImportExperiment)
    manager.addPrimitive("export-experiment", ExportExperiment)
    manager.addPrimitive("clear-experiments", ClearExperiments)

    manager.addPrimitive("set-pre-experiment-commands", SetPreExperimentCommands)
    manager.addPrimitive("set-setup-commands", SetSetupCommands)
    manager.addPrimitive("set-go-commands", SetGoCommands)
    manager.addPrimitive("set-post-run-commands", SetPostRunCommands)
    manager.addPrimitive("set-post-experiment-commands", SetPostExperimentCommands)
    manager.addPrimitive("set-repetitions", SetRepetitions)
    manager.addPrimitive("set-sequential-run-order", SetSequentialRunOrder)
    manager.addPrimitive("set-run-metrics-every-step", SetRunMetricsEveryStep)
    manager.addPrimitive("set-run-metrics-condition", SetRunMetricsCondition)
    manager.addPrimitive("set-time-limit", SetTimeLimit)
    manager.addPrimitive("set-stop-condition", SetStopCondition)
    manager.addPrimitive("set-metrics", SetMetrics)
    manager.addPrimitive("set-variables", SetVariables)
    manager.addPrimitive("set-return-reporter", SetReturnReporter)
    manager.addPrimitive("set-parallel-runs", SetParallelRuns)
    manager.addPrimitive("set-table", SetTable)
    manager.addPrimitive("set-spreadsheet", SetSpreadsheet)
    manager.addPrimitive("set-stats", SetStats)
    manager.addPrimitive("set-lists", SetLists)
    manager.addPrimitive("set-update-view", SetUpdateView)
    manager.addPrimitive("set-update-plots", SetUpdatePlots)

    manager.addPrimitive("goto-behaviorspace-documentation", GotoBehaviorspaceDocumentation)
    manager.addPrimitive("goto-bspace-extension-documentation", GotoBspaceExtensionDocumentation)
    manager.addPrimitive("get-default-parallel-runs", GetDefaultParallelRuns)
    manager.addPrimitive("get-recommended-max-parallel-runs", GetRecommendedMaxParallelRuns)
    manager.addPrimitive("get-return-value", GetReturnValue)
  }
}
