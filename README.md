
# BehaviorSpace Extension for NetLogo

The BehaviorSpace extension allows you to create and run BehaviorSpace experiments from NetLogo code. You can use the
extension alone or in combination with the GUI BehaviorSpace tool, and you can run experiments created in the GUI tool
with the extension.

## Using the BehaviorSpace Extension

To use the BehaviorSpace extension in a model, include the following line at the top of your code:

```
extensions [bspace]
```

If you are already using another extension, you can just add `bspace` to the list inside the square brackets.

Once the extension is loaded, you can use it to run experiments from anywhere in the code. Note that experiments
created in the code will persist for the duration of the current NetLogo session if not cleared with the
`bspace:clear-experiments` command (see [`bspace:clear-experiments`](#bspaceclear-experiments)).

Experiments created in the code will have the following default values:

Pre Experiment Commands: empty
Setup Commands: empty
Go Commands: empty
Post Run Commands: empty
Post Experiment Commands: empty
Repetitions: 1
Sequential Run Order: true
Run Metrics Every Step: true
Run Metrics Condition: empty
Time Limit: 0
Stop Condition: empty
Metrics: empty
Variables: empty
Threads: determined by number of processor cores
Recommended Max Threads: determined by number of processor cores
Table: empty
Spreadsheet: empty
Stats: empty
Lists: empty
Update View: true
Update Plots And Monitors: true
Mirror Headless Output: false

Any properties can also be set explicitly to other default values. For code blocks, you can specify "do nothing" with
an empty pair of square brackets.

## Primitives

### Experiment Management

[`bspace:create-experiment`](#bspacecreate-experiment)
[`bspace:delete-experiment`](#bspacedelete-experiment)
[`bspace:run-experiment`](#bspacerun-experiment)
[`bspace:rename-experiment`](#bspacerename-experiment)
[`bspace:duplicate-experiment`](#bspaceduplicate-experiment)
[`bspace:import-experiments`](#bspaceimport-experiments)
[`bspace:export-experiment`](#bspaceexport-experiment)
[`bspace:clear-experiments`](#bspaceclear-experiments)
[`bspace:set-current-experiment`](#bspaceset-current-experiment)
[`bspace:get-experiments`](#bspaceget-experiments)
[`bspace:get-current-experiment`](#bspaceget-current-experiment)
[`bspace:get-parameters`](#bspaceget-parameters)
[`bspace:experiment-exists`](#bspaceexperiment-exists)
[`bspace:valid-experiment-name`](#bspacevalid-experiment-name)

### Setting Experiment Parameters

[`bspace:set-variables`](#bspaceset-variables)
[`bspace:set-repetitions`](#bspaceset-repetitions)
[`bspace:set-sequential-run-order`](#bspaceset-sequential-run-order)
[`bspace:set-metrics`](#bspaceset-metrics)
[`bspace:set-run-metrics-every-step`](#bspaceset-run-metrics-every-step)
[`bspace:set-run-metrics-condition`](#bspaceset-run-metrics-condition)
[`bspace:set-pre-experiment-commands`](#bspaceset-pre-experiment-commands)
[`bspace:set-setup-commands`](#bspaceset-setup-commands)
[`bspace:set-go-commands`](#bspaceset-go-commands)
[`bspace:set-stop-condition`](#bspaceset-stop-condition)
[`bspace:set-post-run-commands`](#bspaceset-post-run-commands)
[`bspace:set-post-experiment-commands`](#bspaceset-post-experiment-commands)
[`bspace:set-time-limit`](#bspaceset-time-limit)

### Getting Experiment Parameters

[`bspace:get-variables`](#bspaceget-variables)
[`bspace:get-repetitions`](#bspaceget-repetitions)
[`bspace:get-sequential-run-order`](#bspaceget-sequential-run-order)
[`bspace:get-metrics`](#bspaceget-metrics)
[`bspace:get-run-metrics-every-step`](#bspaceget-run-metrics-every-step)
[`bspace:get-run-metrics-condition`](#bspaceget-run-metrics-condition)
[`bspace:get-pre-experiment-commands`](#bspaceget-pre-experiment-commands)
[`bspace:get-setup-commands`](#bspaceget-setup-commands)
[`bspace:get-go-commands`](#bspaceget-go-commands)
[`bspace:get-stop-condition`](#bspaceget-stop-condition)
[`bspace:get-post-run-commands`](#bspaceget-post-run-commands)
[`bspace:get-post-experiment-commands`](#bspaceget-post-experiment-commands)
[`bspace:get-time-limit`](#bspaceget-time-limit)

### Setting Experiment Run Conditions

[`bspace:set-spreadsheet`](#bspaceset-spreadsheet)
[`bspace:set-table`](#bspaceset-table)
[`bspace:set-stats`](#bspaceset-stats)
[`bspace:set-lists`](#bspaceset-list)
[`bspace:set-update-view`](#bspaceset-update-view)
[`bspace:set-update-plots`](#bspaceset-update-plots)
[`bspace:set-parallel-runs`](#bspaceset-parallel-runs)
[`bspace:set-mirror-headless-output`](#bspaceset-mirror-headless-output)

### Getting Experiment Run Conditions

[`bspace:get-spreadsheet`](#bspaceget-spreadsheet)
[`bspace:get-table`](#bspaceget-table)
[`bspace:get-stats`](#bspaceget-stats)
[`bspace:get-lists`](#bspaceget-list)
[`bspace:get-update-view`](#bspaceget-update-view)
[`bspace:get-update-plots`](#bspaceget-update-plots)
[`bspace:get-parallel-runs`](#bspaceget-parallel-runs)
[`bspace:get-mirror-headless-output`](#bspaceget-mirror-headless-output)

### Processing BehaviorSpace Output

[`bspace:get-output-metric`](#bspaceget-output-metric)

### Experiment Information

[`bspace:goto-behaviorspace-documentation`](#bspacegoto-behaviorspace-documentation)
[`bspace:goto-bspace-extension-documentation`](#bspacegoto-bspace-extension-documentation)
[`bspace:get-default-parallel-runs`](#bspaceget-default-parallel-runs)
[`bspace:get-recommended-max-parallel-runs`](#bspaceget-recommended-max-parallel-runs)

### bspace:create-experiment

#### bspace:create-experiment *string* *boolean*

Create a new experiment specified in the first input. If an experiment already exists with that name and
the second input is true, that experiment will be overwritten with the default experiment. If an experiment already
exists with that name and the second input is false, an error will be thrown.

Example:

```
bspace:create-experiment "my-experiment" true
```

### bspace:delete-experiment

#### bspace:delete-experiment *string*

Delete the experiment specified in the first input. An error will be thrown if the specified experiment does not exist.

Example:

```
bspace:delete-experiment "my-experiment"
```

### bspace:run-experiment

#### bspace:run-experiment

Run the current working experiment. An error will be thrown if no current working experiment has been set.

Example:

```
bspace:run-experiment
```

### bspace:rename-experiment

#### bspace:rename-experiment *string*

Rename the current working experiment to the name specified in the first input. An error will be thrown if no current
working experiment has been set.

Example:

```
bspace:rename-experiment "another-experiment"
```

### bspace:duplicate-experiment

#### bspace:duplicate-experiment *string*

Make a copy of the current working experiment and rename the copy to the name specified in the first input, then set
the current working experiment to the new experiment. An error will be thrown if no current working experiment has
been set.

Example:

```
bspace:duplicate-experiment "my-experiment-copy"
```

### bspace:import-experiments

#### bspace:import-experiments *string*

Import the experiments contained in the file at the specified location. An error will be thrown for each experiment
that has a name that already exists, but the remaining experiments will be succesfully imported.

Example:

```
bspace:import-experiments "/Users/hacker53/Documents/experiments.xml"
```

### bspace:export-experiment

#### bspace:export-experiment *string* *boolean*

Export the current working experiment to the file specified in the second input. If that file already exists and the
second input is true, it will be overwritten with the specified experiment. If that file already exists and the second
input is false, an error will be thrown.

Example:

```
bspace:export-experiment "/Users/hacker53/Documents/my-experiment.xml"
```

### bspace:clear-experiments

#### bspace:clear-experiments

Clear the list of stored experiments that have been created in the code, and reset all experiments that have been
paused after running from the code. The model and all experiments created in the GUI will remain unchanged.

Example:

```
bspace:clear-experiments
```

### bspace:set-current-experiment

#### bspace:set-current-experient *string*

Set the current working experiment to the name specified in the first input.

Example:

```
bspace:set-current-experiment "my-experiment"
```

### bspace:get-experiments

#### bspace:get-experiments

Report a list of all BehaviorSpace experiments created programmatically, followed by all BehaviorSpace experiments
created in the GUI.

### bspace:get-current-experiment

#### bspace:get-current-experiment

Report the name of the current working experiment.

### bspace:get-parameters

#### bspace:get-parameters *string*

Report the parameter values for the experiment with the name specified in the first input. An error will be thrown if
no experiment exists with the specified name.

Example:

```
bspace:get-parameters "my-experiment"
```

Sample output:

```
EXPERIMENT PARAMETERS:

Variable values:
	
Repetitions:
	1
Sequential run order:
	true
Metrics:
	List("count turtles")
Run metrics every step:
	true
Run metrics condition:
	
Pre experiment commands:
	
Setup commands:
    setup
	
Go commands:
    go
	
Post run commands:
	
Post experiment commands:
	
Stop condtion:
	
Time limit:
	0

RUN OPTIONS:

Spreadsheet:
	
Table:
	
Stats:
	
Lists:
	
Update view:
	true
Update plots:
	true
Parallel runs:
	6
Mirror Headless Output:
	false
```

### bspace:experiment-exists

#### bspace:experiment-exists *string*

Report whether an experiment exists with the name specified in the first input.

Example:

```
bspace:experiment-exists "my-experiment"
```

### bspace:valid-experiment-name

#### bspace:valid-experiment-name *string*

Report whether the specified name is a valid experiment name.

Example:

```
bspace:valid-experiment-name "" ; false
bspace:valid-experiment-name "my-experiment" ; true

bspace:create-experiment "my-experiment" true
bspace:valid-experiment-name "my-experiment" false
```

### bspace:set-variables

#### bspace:set-variables *list of strings*

Set the variables to vary for the current working experiment. Each element of the input list should contain one
constant or sub-experiment specification. An error will be thrown if no current working experiment has been set.
Note that nested quotation marks must be escaped double quotes, using single quotes will cause errors.

Example:

```
bspace:set-variables [ "[ \"var1\" 0 5 20 ]" ]
```

### bspace:set-repetitions

#### bspace:set-repetitions *number*

Set the repetitions for the current working experiment. An error will be thrown if no current working experiment has
been set.

Example:

```
bspace:set-repetitions 3
```

### bspace:set-sequential-run-order

#### bspace:set-sequential-run-order *boolean*

Set whether the current working experiment uses sequential run order. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-sequential-run-order true
```

### bspace:set-metrics

#### bspace:set-metrics *anonymous reporter*
#### (bspace:set-metrics *anonymous reporter* ...)

Set the metrics commands for the current working experiment. An error will be thrown if no current working experiment
has been set. To set only one metric, the primitive can be called as normal with one input, but to set multiple
metrics, the entire command must be surrounded with parentheses.

Example:

```
bspace:set-metrics [ count turtles ]
(bspace:set-metrics [ count turtles ] [ count patches ])
```

### bspace:set-run-metrics-every-step

#### bspace:set-run-metrics-every-step *boolean*

Set whether the current working experiment runs metrics every step. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-run-metrics-every-step false
```

### bspace:set-run-metrics-condition

#### bspace:set-run-metrics-condition *anonymous reporter*

Set the run metrics condition for the current working experiment. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-run-metrics-condition [ count turtles < 5 ]
```

### bspace:set-pre-experiment-commands

#### bspace:set-pre-experiment-commands *anonymous command*

Set the pre-experiment commands for the current working experiment. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-pre-experiment-commands [ clear-all ]
```

### bspace:set-setup-commands

#### bspace:set-setup-commands *anonymous command*

Set the setup commands for the current working experiment. An error will be thrown if no current working experiment has
been set.

Example:

```
bspace:set-setup-commands [ clear-all create-turtles 50 ]
```

### bspace:set-go-commands

#### bspace:set-go-commands *anonymous command*

Set the go commands for the current working experiment. An error will be thrown if no current working experiment has
been set.

Example:

```
bspace:set-go-commands [ myFunction ]
```

### bspace:set-stop-condition

#### bspace:set-stop-condition *anonymous reporter*

Set the stop condition for the current working experiment. An error will be thrown if no current working experiment has
been set.

Example:

```
bspace:set-stop-condition [ ticks > 1000 ]
```

### bspace:set-post-run-commands

#### bspace:set-post-run-commands *anonymous command*

Set the post-run commands for the current working experiment. An error will be thrown if no current working experiment
has been set.

Example:

```
bspace:set-post-run-commands [ print count turtles ]
```

### bspace:set-post-experiment-commands

#### bspace:set-post-experiment-commands *anonymous command*

Set the post-experiment commands for the current working experiment. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-post-experiment-commands [ ask turtles [ setxy 0 0 ] ]
```

### bspace:set-time-limit

#### bspace:set-time-limit *number*

Set the time limit in ticks for the current working experiment. An error will be thrown if no current working experiment
has been set.

Example:

```
bspace:set-time-limit 1500
```

### bspace:get-variables

#### bspace:get-variables

Report the variables for the current working experiment. An error will be thrown if no current working experiment has
been set.

### bspace:get-repetitions

#### bspace:get-repetitions

Report the repetitions for the current working experiment. An error will be thrown if no current working experiment has
been set.

### bspace:get-sequential-run-order

#### bspace:get-sequential-run-order

Report whether the current working experiment uses sequential run order. An error will be thrown if no current working
experiment has been set.

### bspace:get-metrics

#### bspace:get-metrics

Report the metrics for the current working experiment. An error will be thrown if no current working experiment has
been set.

### bspace:get-run-metrics-every-step

#### bspace:get-run-metrics-every-step

Report whether the current working experiment runs metrics every step. An error will be thrown if no current working
experiment has been set.

### bspace:get-run-metrics-condition

#### bspace:get-run-metrics-condition

Report the run metrics condition for the current working experiment. An error will be thrown if no current working
experiment has been set.

### bspace:get-pre-experiment-commands

#### bspace:get-pre-experiment-commands

Report the pre-experiment commands for the current working experiment. An error will be thrown if no current working
experiment has been set.

### bspace:get-setup-commands

#### bspace:get-setup-commands

Report the setup commands for the current working experiment. An error will be thrown if no current working experiment
has been set.

### bspace:get-go-commands

#### bspace:get-go-commands

Report the go commands for the current working experiment. An error will be thrown if no current working experiment has
been set.

### bspace:get-stop-condition

#### bspace:get-stop-condition

Report the stop condition for the current working experiment. An error will be thrown if no current working
experiment has been set.

### bspace:get-post-run-commands

#### bspace:get-post-run-commands

Report the post-run commands for the current working experiment. An error will be thrown if no current working
experiment has been set.

### bspace:get-post-experiment-commands

#### bspace:get-post-experiment-commands

Report the post-experiment commands for the current working experiment. An error will be thrown if no current working
experiment has been set.

### bspace:get-time-limit

#### bspace:get-time-limit

Report the time limit for the current working experiment. An error will be thrown if no current working experiment has
been set.

### bspace:set-spreadsheet

#### bspace:set-spreadsheet *string*

Set the path for the spreadsheet file for the current working experiment. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-spreadsheet "/Users/hacker53/Documents/exp-sheet.csv"
```

### bspace:set-table

#### bspace:set-table *string*

Set the path for the table file for the current working experiment. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-table "/Users/hacker53/Documents/exp-table.csv"
```

### bspace:set-stats

#### bspace:set-stats *string*

Set the path for the stats file for the current working experiment. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-stats "/Users/hacker53/Documents/exp-stats.csv"
```

### bspace:set-lists

#### bspace:set-lists *string*

Set the path for the lists file for the current working experiment. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-lists "/Users/hacker53/Documents/exp-lists.csv"
```

### bspace:set-update-view

#### bspace:set-update-view *boolean*

Set whether the current working experiment should update the view. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-update-view true
```

### bspace:set-update-plots

#### bspace:set-update-view *boolean*

Set whether the current working experiment should update the plots. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-update-view false
```

### bspace:set-parallel-runs

#### bspace:set-parallel-runs *number*

Set the number of parallel runs for the current working experiment. An error will be thrown if no current working
experiment has been set.

Example:

```
bspace:set-parallel-runs 3
```

### bspace:set-mirror-headless-output

#### bspace:set-mirror-headless-output *boolean*

Set whether the current working experiment should display its background output in the Command Center, if running in
the GUI.

Example:

```
bspace:set-mirror-headless-output true
```

### bspace:get-spreadsheet

#### bspace:get-spreadsheet

Report the spreadsheet location for the current working experiment. An error will be thrown if no current working
experiment has been set. Will report an empty string if no spreadsheet file was specified in the current working
experiment, or the string "-" if the output is directed to standard output.

### bspace:get-table

#### bspace:get-table

Report the table location for the current working experiment. An error will be thrown if no current working experiment
has been set. Will report an empty string if no table file was specified in the current working experiment, or the
string "-" if the output is directed to standard output.

### bspace:get-stats

#### bspace:get-stats

Report the stats location for the current working experiment. An error will be thrown if no current working experiment
has been set. Will report an empty string if no spreastatsdsheet file was specified in the current working experiment,
or the string "-" if the output is directed to standard output.

### bspace:get-lists

#### bspace:get-lists

Report the lists location for the current working experiment. An error will be thrown if no current working experiment
has been set. Will report an empty string if no lists file was specified in the current working experiment, or the
string "-" if the output is directed to standard output.

### bspace:get-update-view

#### bspace:get-update-view

Report whether the current working experiment will update the view. An error will be thrown if no current working
experiment has been set.

### bspace:get-update-plots

#### bspace:get-update-plots

Report whether the current working experiment will update the plots. An error will be thrown if no current working
experiment has been set.

### bspace:get-parallel-runs

#### bspace:get-parallel-runs

Report the number of parallel runs for the current working experiment. An error will be thrown if no current working
experiment has been set.

### bspace:get-mirror-headless-output

#### bspace:get-mirror-headless-output

Report whether the current working experiment will display its background output in the Command Center, if running in
the GUI.

### bspace:get-output-metric

#### bspace:get-output-metric *string* *string* *number*

Report the list of values for the metric specified in the second input for the run number specified in the third input,
from the file specified in the first input. The specified file must be a spreadsheet output to ensure proper ordering
of values. An error will be thrown if any of the inputs are invalid.

Example:

```
bspace:get-output-metric "/Users/hacker53/Documents/spreadsheet.csv" "count turtles" 3
```

### bspace:goto-behaviorspace-documentation

#### bspace:goto-behaviorspace-documentation

Open the BehaviorSpace documentation page in a browser window.

### bspace:goto-bspace-extension-documentation

#### bspace:goto-bspace-extension-documentation

Open the bspace extension documentation page in a browser window.

### bspace:get-default-parallel-runs

#### bspace:get-default-parallel-runs

Report the default number of parallel runs for the current device.

### bspace:get-recommended-max-parallel-runs

#### bspace:get-recommended-max-parallel-runs

Report the recommended maximum number of parallel runs for the current device.
