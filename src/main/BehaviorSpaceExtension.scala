// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ AnonymousProcedure, Argument, Command, Context, DefaultClassManager, ExtensionException,
                       ExtensionManager, LabDefaultValues, LabProtocol, PrimitiveManager, RefValueSet }
import org.nlogo.core.I18N
import org.nlogo.lab.Worker
import org.nlogo.nvm.HaltException
import org.nlogo.swing.OptionPane
import org.nlogo.window.GUIWorkspace

import scala.collection.mutable.Map

object ExperimentType extends Enumeration {
  type ExperimentType = Value
  val GUI, Code, None = Value
}

object BehaviorSpaceExtension {
  val experiments = Map[String, LabProtocol]()
  val experimentStack = Map[String, Worker]()

  var currentExperiment = ""

  private val errors = Map[String, String](
    "alreadyExists" -> "An experiment already exists with the name \"$0\".",
    "emptyName" -> "Experiment name cannot be empty.",
    "noCurrent" -> """You must set a current working experiment before running bspace commands with no specified experiment name""",
    "noExperiment" -> "No experiment exists with the name \"$0\".",
    "recursive" -> "Cannot run an experiment recursively.",
    "invalidFormat" -> "Invalid format in \"$0\".",
    "fileExists" -> "File \"$0\" already exists.",
    "gui" -> "Experiment \"$0\" is a GUI experiment, it cannot be edited.",
    "noRun" -> "Run \"$0\" does not exist in the specified output file."
  )

  def experimentType(name: String, context: Context): ExperimentType.ExperimentType = {
    if (experiments.contains(name))
      ExperimentType.Code
    else if (context.workspace.getBehaviorSpaceExperiments.find(x => x.name == name).isDefined)
      ExperimentType.GUI
    else
      ExperimentType.None
  }

  def validateForEditing(name: String, context: Context): Boolean = {
    experimentType(name, context) match {
      case ExperimentType.None =>
        nameError(context, "noExperiment", name)
        false
      case ExperimentType.GUI =>
        nameError(context, "gui", name)
        false
      case ExperimentType.Code => true
    }
  }

  def nameError(context: Context, message: String, keys: String*): Unit = {
    if (context.workspace.isHeadless)
      throw new ExtensionException(replaceErrorString(message, keys))

    else {
      if (new OptionPane(context.workspace.asInstanceOf[GUIWorkspace].getFrame,
                         I18N.gui.get("tools.behaviorSpace.invalid"), replaceErrorString(message, keys),
                         Seq(I18N.gui.get("common.buttons.ok"), I18N.gui.get("common.buttons.halt")),
                         OptionPane.Icons.Error).getSelectedIndex != 0)
        throw new HaltException(true)
    }
  }

  def replaceErrorString(message: String, keys: Seq[String]): String = {
    var error =
      if (errors.contains(message)) errors(message)
      else message

    for (i <- 0 until keys.length) {
      error = error.replace("$" + i, keys(i))
    }

    return error
  }

  def removeQuotes(string: String): String = {
    if (string(0) == '"') string.substring(1, string.length - 1) else string
  }

  def extractSource(proc: AnonymousProcedure): String = {
    """\[(.*)\]""".r.findFirstMatchIn(proc.toString).get.group(1).trim
  }
}

class BehaviorSpaceExtension extends DefaultClassManager {
  def load(manager: PrimitiveManager): Unit = {
    manager.addPrimitive("create-experiment", CreateExperiment)
    manager.addPrimitive("delete-experiment", DeleteExperiment)
    manager.addPrimitive("run-experiment", RunExperiment)
    manager.addPrimitive("rename-experiment", RenameExperiment)
    manager.addPrimitive("duplicate-experiment", DuplicateExperiment)
    manager.addPrimitive("import-experiments", ImportExperiments)
    manager.addPrimitive("export-experiment", ExportExperiment)
    manager.addPrimitive("clear-experiments", ClearExperiments)
    manager.addPrimitive("set-current-experiment", SetCurrentExperiment)
    manager.addPrimitive("print-experiments", PrintExperiments)
    manager.addPrimitive("get-experiment-list", GetExperimentList)
    manager.addPrimitive("get-current-experiment", GetCurrentExperiment)
    manager.addPrimitive("get-parameters", GetParameters)
    manager.addPrimitive("experiment-exists", ExperimentExists)
    manager.addPrimitive("valid-experiment-name", ValidExperimentName)

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
    manager.addPrimitive("set-parallel-runs", SetParallelRuns)
    manager.addPrimitive("set-table", SetTable)
    manager.addPrimitive("set-spreadsheet", SetSpreadsheet)
    manager.addPrimitive("set-stats", SetStats)
    manager.addPrimitive("set-lists", SetLists)
    manager.addPrimitive("set-mirror-headless-output", SetMirrorHeadlessOutput)

    manager.addPrimitive("goto-behaviorspace-documentation", GotoBehaviorspaceDocumentation)
    manager.addPrimitive("goto-bspace-extension-documentation", GotoBspaceExtensionDocumentation)
    manager.addPrimitive("get-pre-experiment-commands", GetPreExperimentCommands)
    manager.addPrimitive("get-setup-commands", GetSetupCommands)
    manager.addPrimitive("get-go-commands", GetGoCommands)
    manager.addPrimitive("get-post-run-commands", GetPostRunCommands)
    manager.addPrimitive("get-post-experiment-commands", GetPostExperimentCommands)
    manager.addPrimitive("get-repetitions", GetRepetitions)
    manager.addPrimitive("get-sequential-run-order", GetSequentialRunOrder)
    manager.addPrimitive("get-run-metrics-every-step", GetRunMetricsEveryStep)
    manager.addPrimitive("get-run-metrics-condition", GetRunMetricsCondition)
    manager.addPrimitive("get-time-limit", GetTimeLimit)
    manager.addPrimitive("get-stop-condition", GetStopCondition)
    manager.addPrimitive("get-metrics", GetMetrics)
    manager.addPrimitive("get-variables", GetVariables)
    manager.addPrimitive("get-parallel-runs", GetParallelRuns)
    manager.addPrimitive("get-table", GetTable)
    manager.addPrimitive("get-spreadsheet", GetSpreadsheet)
    manager.addPrimitive("get-stats", GetStats)
    manager.addPrimitive("get-lists", GetLists)
    manager.addPrimitive("get-default-parallel-runs", GetDefaultParallelRuns)
    manager.addPrimitive("get-recommended-max-parallel-runs", GetRecommendedMaxParallelRuns)
    manager.addPrimitive("get-mirror-headless-output", GetMirrorHeadlessOutput)
  }

  override def runOnce(manager: ExtensionManager): Unit = {
    BehaviorSpaceExtension.experiments.clear()
    BehaviorSpaceExtension.currentExperiment = ""
  }
}
