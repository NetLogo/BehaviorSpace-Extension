
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
created in the code will persist for the lifetime of the application if not cleared with the `bspace:clear-experiments`
command (see [`bspace:clear-experiments`](#bspaceclear-experiments)).

## Primitives

Note: unless otherwise specified, any commands or reporters used as input for a BehaviorSpace extension primitive
should be a string.

### Experiment Management

[`bspace:create-experiment`](#bspacecreate-experiment)
[`bspace:delete-experiment`](#bspacedelete-experiment)
[`bspace:run-experiment`](#bspacerun-experiment)
[`bspace:rename-experiment`](#bspacerename-experiment)
[`bspace:duplicate-experiment`](#bspaceduplicate-experiment)
[`bspace:import-experiments`](#bspaceimport-experiments)
[`bspace:export-experiment`](#bspaceexport-experiment)
[`bspace:clear-experiments`](#bspaceclear-experiments)

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
[`bspace:set-return-reporter`](#bspaceset-return-reporter)

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

### Getting Experiment Run Conditions

[`bspace:get-spreadsheet`](#bspaceget-spreadsheet)
[`bspace:get-table`](#bspaceget-table)
[`bspace:get-stats`](#bspaceget-stats)
[`bspace:get-lists`](#bspaceget-list)
[`bspace:get-update-view`](#bspaceget-update-view)
[`bspace:get-update-plots`](#bspaceget-update-plots)
[`bspace:get-parallel-runs`](#bspaceget-parallel-runs)

### Experiment Information

[`bspace:goto-behaviorspace-documentation`](#bspacegoto-behaviorspace-documentation)
[`bspace:goto-bspace-extension-documentation`](#bspacegoto-bspace-extension-documentation)
[`bspace:get-default-parallel-runs`](#bspaceget-default-parallel-runs)
[`bspace:get-recommended-max-parallel-runs`](#bspaceget-recommended-max-parallel-runs)
[`bspace:get-return-value`](#bspaceget-return-value)

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

#### bspace:run-experiment *string*

Run the experiment specified in the first input. An error will be thrown if the specified experiment does not exist.

Example:

```
bspace:run-experiment "my-experiment"
```

### bspace:rename-experiment

#### bspace:rename-experiment *string* *string*

Rename the experiment specified in the first input to the name specified in the second input. An error
will be thrown if the experiment specified in the first input does not exist.

Example:

```
bspace:rename-experiment "my-experiment" "another-experiment"
```

### bspace:duplicate-experiment

#### bspace:duplicate-experiment *string* *string*

Make a copy of the experiment specified in the first input and rename the copy to the name specified in the second
input. An error will be thrown if the experiment specified in the first input does not exist.

Example:

```
bspace:duplicate-experiment "my-experiment" "my-experiment-copy"
```

### bspace:import-experiments

#### bspace:import-experiments *string*

Import the experiments contained in the file at the specified location. An error will be thrown for each experiment
that has a name that already exists, but the remaining experiments will be succesfully imported.

Example:

```
bspace:import-experiments "/Users/johndoe/Documents/experiments.xml"
```

### bspace:export-experiment

#### bspace:export-experiment *string* *string* *boolean*

Export the experiment specified in the first input to the file specified in the second input. If that file already
exists and the third input is true, it will be overwritten with the specified experiment. If that file already exists
and the third input is false, an error will be thrown.

Example:

```
bspace:export-experiment "my-experiment" "/Users/johndoe/Documents/my-experiment.xml"
```

### bspace:clear-experiments

#### bspace:clear-experiments

Clear the list of stored experiments that have been created in the code, and reset all experiments that have been
paused after running from the code.

Example:

```
bspace:clear-experiments
```

### bspace:set-variables

#### bspace:set-variables *string* *string*

Set the variables to vary for the experiment specified in the first input. An error will be thrown if the specified
experiment does not exist.

Example:

```
bspace:set-variables "my-experiment" "[ 'var1' 0 5 20 ]"
```

### bspace:set-repetitions

#### bspace:set-repetitions *string* *number*

Set the repetitions for the experiment specified in the first input. An error will be thrown if the specified
experiment does not exist.

Example:

```
bspace:set-repetitions "my-experiment" 3
```

### bspace:set-sequential-run-order

#### bspace:set-sequential-run-order *string* *boolean*

Set whether the experiment specified in the first input uses sequential run order. An error will be thrown if the
specified experiment does not exist.

Example:

```
bspace:set-sequential-run-order "my-experiment" true
```

### bspace:set-metrics

#### bspace:set-metrics *string* *list*

Set the metrics commands for the experiment specified in the first input. An error will be thrown if the specified
experiment does not exist. The second input must be a list of string commands.

Example:

```
bspace:set-metrics "my-experiment" [ "count turtles", "count patches" ]
```

### bspace:set-run-metrics-every-step

#### bspace:set-run-metrics-every-step *string* *boolean*

Set whether the experiment specified in the first input runs metrics every step. An error will be thrown if the
specified experiment does not exist.

Example:

```
bspace:set-run-metrics-every-step "my-experiment" false
```

### bspace:set-run-metrics-condition

#### bspace:set-run-metrics-condition *string* *string*

Set the run metrics condition for the experiment specified in the first input. An error will be thrown if the specified
experiment does not exist.

Example:

```
bspace:set-run-metrics-condition "my-experiment" "count turtles < 5"
```

### bspace:set-pre-experiment-commands

#### bspace:set-pre-experiment-commands *string* *string*

Set the pre-experiment commands for the experiment specified in the first input. An error will be thrown if the
specified experiment does not exist.

Example:

```
bspace:set-pre-experiment-commands "my-experiment" "clear-all"
```

### bspace:set-setup-commands

#### bspace:set-setup-commands *string* *string*

Set the setup commands for the experiment specified in the first input. An error will be thrown if the specified
experiment does not exist.

Example:

```
bspace:set-setup-commands "my-experiment" "clear-all create-turtles 50"
```

### bspace:set-go-commands

#### bspace:set-go-commands *string* *string*

Set the go commands for the experiment specified in the first input. An error will be thrown if the specified
experiment does not exist.

Example:

```
bspace:set-go-commands "my-experiment" "myFunction"
```

### bspace:set-stop-condition

#### bspace:set-stop-condition *string* *string*

Set the stop condition for the experiment specified in the first input. An error will be thrown if the specified
experiment does not exist.

Example:

```
bspace:set-stop-condition "my-experiment" "ticks > 1000"
```

### bspace:set-post-run-commands

#### bspace:set-post-run-commands *string* *string*

Set the post-run commands for the experiment specified in the first input. An error will be thrown if the specified
experiment does not exist.

Example:

```
bspace:set-post-run-commands "my-experiment" "print count turtles"
```

### bspace:set-post-experiment-commands

#### bspace:set-post-experiment-commands *string* *string*

Set the post-experiment commands for the experiment specified in the first input. An error will be thrown if the
specified experiment does not exist.

Example:

```
bspace:set-post-experiment-commands "my-experiment" "ask turtles [ setxy 0 0 ]"
```

### bspace:set-time-limit

#### bspace:set-time-limit *string* *number*

Set the time limit in ticks for the experiment specified in the first input. An error will be thrown if the specified
experiment does not exist.

Example:

```
bspace:set-time-limit "my-experiment" 1500
```

### bspace:set-return-reporter

#### bspace:set-return-reporter *string* *string* *string*

Set a return value for the experiment specified in the first input, with the name specified in the second input. An
error will be thrown if the specified experiment does not exist.

Example:

```
bspace:set-return-reporter "my-experiment" "numTurtles" "count turtles"
```

### bspace:get-variables

#### bspace:get-variables *string*

Report the variables for the specified experiment. An error will be thrown if the specified experiment does not exist.

Example:

```
bspace:get-variables "my-experiment"
```

### bspace:get-repetitions

#### bspace:get-repetitions *string*

Report the repetitions for the specified experiment. An error will be thrown if the specified experiment does not exist.

Example:

```
bspace:get-repetitions "my-experiment"
```

### bspace:get-sequential-run-order

#### bspace:get-sequential-run-order *string*

Report whether the specified experiment uses sequential run order. An error will be thrown if the specified experiment
does not exist.

Example:

```
bspace:get-sequential-run-order "my-experiment"
```

### bspace:get-metrics

#### bspace:get-metrics *string*

Report the metrics for the specified experiment. An error will be thrown if the specified experiment does not exist.

Example:

```
bspace:get-metrics "my-experiment"
```

### bspace:get-run-metrics-every-step

#### bspace:get-run-metrics-every-step *string*

Report whether the specified experiment runs metrics every step. An error will be thrown if the specified experiment
does not exist.

Example:

```
bspace:get-run-metrics-every-step "my-experiment"
```

### bspace:get-run-metrics-condition

#### bspace:get-run-metrics-condition *string*

Report the run metrics condition for the specified experiment. An error will be thrown if the specified experiment does
not exist.

Example:

```
bspace:get-run-metrics-condition "my-experiment"
```

### bspace:get-pre-experiment-commands

#### bspace:get-pre-experiment-commands *string*

Report the pre-experiment commands for the specified experiment. An error will be thrown if the specified experiment
does not exist.

Example:

```
bspace:get-pre-experiment-commands "my-experiment"
```

### bspace:get-setup-commands

#### bspace:get-setup-commands *string*

Report the setup commands for the specified experiment. An error will be thrown if the specified experiment does not
exist.

Example:

```
bspace:get-setup-commands "my-experiment"
```

### bspace:get-go-commands

#### bspace:get-go-commands *string*

Report the go commands for the specified experiment. An error will be thrown if the specified experiment does not
exist.

Example:

```
bspace:get-go-commands "my-experiment"
```

### bspace:get-stop-condition

#### bspace:get-stop-condition *string*

Report the stop condition for the specified experiment. An error will be thrown if the specified experiment does not
exist.

Example:

```
bspace:get-stop-condition "my-experiment"
```

### bspace:get-post-run-commands

#### bspace:get-post-run-commands *string*

Report the post-run commands for the specified experiment. An error will be thrown if the specified experiment does not
exist.

Example:

```
bspace:get-post-run-commands "my-experiment"
```

### bspace:get-post-experiment-commands

#### bspace:get-post-experiment-commands *string*

Report the post-experiment commands for the specified experiment. An error will be thrown if the specified experiment
does not exist.

Example:

```
bspace:get-post-experiment-commands "my-experiment"
```

### bspace:get-time-limit

#### bspace:get-time-limit *string*

Report the time limit for the specified experiment. An error will be thrown if the specified experiment does not exist.

Example:

```
bspace:get-time-limit "my-experiment"
```

### bspace:set-spreadsheet

#### bspace:set-spreadsheet *string* *string*

Set the path for the spreadsheet file for the experiment specified in the first input. An error will be thrown if the
specified experiment does not exist.

Example:

```
bspace:set-spreadsheet "my-experiment" "/Users/johndoe/Documents/exp-sheet.csv"
```

### bspace:set-table

#### bspace:set-table *string* *string*

Set the path for the table file for the experiment specified in the first input. An error will be thrown if the
specified experiment does not exist.

Example:

```
bspace:set-table "my-experiment" "/Users/johndoe/Documents/exp-table.csv"
```

### bspace:set-stats

#### bspace:set-stats *string* *string*

Set the path for the stats file for the experiment specified in the first input. An error will be thrown if the
specified experiment does not exist.

Example:

```
bspace:set-stats "my-experiment" "/Users/johndoe/Documents/exp-stats.csv"
```

### bspace:set-lists

#### bspace:set-lists *string* *string*

Set the path for the lists file for the experiment specified in the first input. An error will be thrown if the
specified experiment does not exist.

Example:

```
bspace:set-lists "my-experiment" "/Users/johndoe/Documents/exp-lists.csv"
```

### bspace:set-update-view

#### bspace:set-update-view *string* *boolean*

Set whether the specified experiment should update the view. An error will be thrown if the specified experiment does
not exist.

Example:

```
bspace:set-update-view "my-experiment" true
```

### bspace:set-update-plots

#### bspace:set-update-view *string* *boolean*

Set whether the specified experiment should update the plots. An error will be thrown if the specified experiment does
not exist.

Example:

```
bspace:set-update-view "my-experiment" false
```

### bspace:set-parallel-runs

#### bspace:set-parallel-runs *string* *number*

Set the number of parallel runs for the experiment specified in the first input. An error will be thrown if the
specified experiment does not exist.

Example:

```
bspace:set-parallel-runs "my-experiment" 3
```

### bspace:get-spreadsheet

#### bspace:get-spreadsheet *string*

Report the spreadsheet location for the specified experiment. An error will be thrown if the specified experiment does
not exist.

Example:

```
bspace:get-spreadsheet "my-experiment"
```

### bspace:get-table

#### bspace:get-table *string*

Report the table location for the specified experiment. An error will be thrown if the specified experiment does not
exist.

Example:

```
bspace:get-table "my-experiment"
```

### bspace:get-stats

#### bspace:get-stats *string*

Report the stats location for the specified experiment. An error will be thrown if the specified experiment does not
exist.

Example:

```
bspace:get-stats "my-experiment"
```

### bspace:get-lists

#### bspace:get-lists *string*

Report the lists location for the specified experiment. An error will be thrown if the specified experiment does not
exist.

Example:

```
bspace:get-lists "my-experiment"
```

### bspace:get-update-view

#### bspace:get-update-view *string*

Report whether the specified experiment will update the view. An error will be thrown if the specified experiment does
not exist.

Example:

```
bspace:get-update-view "my-experiment"
```

### bspace:get-update-plots

#### bspace:get-update-plots *string*

Report whether the specified experiment will update the plots. An error will be thrown if the specified experiment does
not exist.

Example:

```
bspace:get-update-plots "my-experiment"
```

### bspace:get-parallel-runs

#### bspace:get-parallel-runs *string*

Report the number of parallel runs for the specified experiment. An error will be thrown if the specified experiment
does not exist.

Example:

```
bspace:get-parallel-runs "my-experiment"
```

### bspace:goto-behaviorspace-documentation

#### bspace:goto-behaviorspace-documentation

Open the BehaviorSpace documentation page in a browser window.

Example:

```
bspace:goto-behaviorspace-documentation
```

### bspace:goto-bspace-extension-documentation

#### bspace:goto-bspace-extension-documentation

Open the bspace extension documentation page in a browser window.

Example:

```
bspace:goto-bspace-extension-documentation
```

### bspace:get-default-parallel-runs

#### bspace:get-default-parallel-runs

Reports the default number of parallel runs for the current device.

Example:

```
print bspace:get-default-parallel-runs
```

### bspace:get-recommended-max-parallel-runs

#### bspace:get-recommended-max-parallel-runs

Reports the recommended maximum number of parallel runs for the current device.

Example:

```
print bspace:get-recommended-max-parallel-runs
```

### bspace:get-return-value

#### bspace:get-return-value *string*

Gets the return value with the specified name. Will throw an error if the specified return value does not exist.

Example:

```
print bspace:get-return-value "numTurtles"
```

