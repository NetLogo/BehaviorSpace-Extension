extensions [bspace]

globals [ output-directory ]
breed [frogs frog]
breed [snakes snake]
frogs-own [struggle? poison]
snakes-own [resistance]

to startup
  ; Note: on windows the file separater should be changed to "\".
  ; The file separator is used because the NetLogo user-directory primitive includes one in its output
  ; The Pathdir extension could be used to make things operating system independent
  set output-directory word home-directory "/"
end

; This creates an experiment which is equivalent to the Red Queen's experiment Beta2 when the default values are used
to create-an-experiment [ expt-name ]
  clear-ticks clear-turtles clear-patches clear-drawing  clear-all-plots clear-output
  ;; Create Experiment and make it current
  bspace:create-experiment expt-name false
  bspace:set-current-experiment expt-name

   ;; Set Parameters
  bspace:set-variables  (list "[\"initial-resistance-mean\" 25]"
    "[\"initial-poison-mean\" [25 5 50]]"
    "[\"initial-number-frogs\" 150]"
    "[\"initial-number-snakes\" 150]")
  bspace:set-metrics  ["count frogs" "count snakes" "average-poison" "average-resistance" ]
  bspace:set-run-metrics-condition  [ false ]
  bspace:set-go-commands [ go ]
  ;; Here are some commands that are not used yet in this model
  ;;bspace:set-pre-experiment-commands  *commands*
  ;;bspace:set-post-run-commands  *commands*
  ;;bspace:set-post-experiment-commands  *commands*
  ;;bspace:set-stop-condition  *condition*
  set-changeable-values
end

to set-changeable-values
  ;; Set non-Run Conditions
  bspace:set-time-limit  5000
  bspace:set-repetitions  number-of-repetitions
  bspace:set-sequential-run-order sequential-run-order
  bspace:set-run-metrics-every-step run-metrics-every-step
  ifelse produce-repeatable-results
  [bspace:set-setup-commands [ random-seed (474 + behaviorspace-run-number) setup ] ]
  [bspace:set-setup-commands [ setup ]]
  ;; Set Run Conditions
  set-table
  set-spreadsheet
  set-stats
  set-lists

  bspace:set-update-view  update-view
  bspace:set-update-plots  update-plots-monitors
  bspace:set-parallel-runs  num-parallel-runs
end

to-report exists-current-experiment?
  report not empty? bspace:get-current-experiment
end

; This reports an output file name based on a template and an output type.
; Current output types are "spreadsheet", "table", "stats" and "lists"
; A more ambitious approach would allow the template for the filename to be an input
to-report output-file-name [ output-file-type ]
  report (word output-directory bspace:get-current-experiment "-reps-" number-of-repetitions "-" output-file-type ".csv")
end

to-report get-table-name
  ; the table name will be the empty string unless there is a current experiment and the user has selected table output
  report  ifelse-value exists-current-experiment? and table-output
  [ output-file-name "table" ]
  [""]
end

to set-table
  bspace:set-table get-table-name
end


to-report get-spreadsheet-name
  ; the spreadsheet name will be the empty string unless there is a current experiment and the user has selected spreadsheet output
  report  ifelse-value exists-current-experiment? and spreadsheet-output
  [ output-file-name "spreadsheet" ]
  [""]
end

to set-spreadsheet
  bspace:set-spreadsheet get-spreadsheet-name
end

to-report get-stats-name
  ; the stats name will be the empty string unless there is a current experiment and the user has selected stats output
  report  ifelse-value exists-current-experiment? and statistics-output
  [ output-file-name "stats" ]
  [""]
end

to set-stats
  bspace:set-stats get-stats-name
end

to-report get-lists-name
  ; the lists name will be the empty string unless there is a current experiment and the user has selected lists output
  report  ifelse-value exists-current-experiment? and lists-output
  [ output-file-name "lists" ]
  [""]
end

to set-lists
  bspace:set-lists get-lists-name
end



;;;;;; This code comes from the Red Queen Model
to setup
  clear-all
  ask patches [ set pcolor white ]
  set-default-shape frogs "frog top"
  setup-individuals
  reset-ticks
end

to setup-individuals
  create-frogs initial-number-frogs
  [
    set size 1
    set poison random-normal initial-poison-mean 1
    setxy random-xcor random-ycor
    set struggle? false
    set color red
  ]

  create-snakes initial-number-snakes
  [
    set size 1
    set resistance random-normal initial-resistance-mean 1
    setxy random-xcor random-ycor
    set color blue
  ]
end

to go
  ask snakes [
    if resistance < 0 [die]
    rt random-float 50 - random-float 50
    fd 1
    if any? frogs-here
    [
      hunt-frogs
    ]
  ]

  ask frogs [
    if poison < 0 [die]
    rt random-float 50 - random-float 50
    fd 1
    set struggle? false
  ]


;; new individual-based reproduction mechanism
  ask turtles [
    if breed = frogs and count frogs < max-population / 2 and random 100 < 5 [
      reproduce
    ]
    if breed = snakes and count snakes < max-population / 2 and random 100 < 5 [
      reproduce
    ]
  ]

  tick
end

to hunt-frogs
  let hunted one-of (frogs-here with [not struggle?])
  if hunted != nobody [
    ask hunted [ set struggle? true ]
    ifelse resistance > [ poison ] of hunted [
      ask hunted [ die ]
    ]
    [
      if resistance != [ poison ] of hunted [ die ]
    ]
  ]
end

to reproduce
 if breed = frogs [
   hatch 1 [
     set poison random-normal [ poison ] of myself 1
   ]
 ]
 if breed = snakes [
   hatch 1 [
     set resistance random-normal [resistance] of myself 1
   ]
 ]
end

to-report average-poison
  ifelse count frogs > 0
    [ report mean [ poison ] of frogs ]
    [ report 0 ]
end

to-report average-resistance
  ifelse count snakes > 0
    [ report mean [ resistance ] of snakes ]
    [ report 0 ]
end

;;;;;; End code from the Red Queen Model

; Copyright 2007 Uri Wilensky.
; See Info tab for full copyright and license.
@#$#@#$#@
GRAPHICS-WINDOW
295
475
554
735
-1
-1
7.17143
1
10
1
1
1
0
1
1
1
-17
17
-17
17
1
1
1
ticks
30.0

SLIDER
935
570
1139
603
initial-number-frogs
initial-number-frogs
1
150
150.0
1
1
NIL
HORIZONTAL

SLIDER
935
603
1138
636
initial-number-snakes
initial-number-snakes
1
150
148.0
1
1
NIL
HORIZONTAL

SLIDER
935
636
1138
669
initial-poison-mean
initial-poison-mean
0
50
25.0
1
1
NIL
HORIZONTAL

SLIDER
935
669
1138
702
initial-resistance-mean
initial-resistance-mean
0
50
25.0
1
1
NIL
HORIZONTAL

SLIDER
935
701
1138
734
max-population
max-population
0
500
500.0
1
1
NIL
HORIZONTAL

BUTTON
15
95
262
128
create experiment based on Beta2
let current-experiment-name user-input \"name for new experiment\"\ncreate-an-experiment current-experiment-name
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
15
180
195
213
run current  experiment
bspace:run-experiment
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
15
325
161
358
delete experiment
bspace:delete-experiment user-input \"name of experiment to delete\"\nclear-output
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
15
490
210
523
number-of-repetitions
number-of-repetitions
1
20
3.0
1
1
NIL
HORIZONTAL

SWITCH
330
270
463
303
update-view
update-view
0
1
-1000

SWITCH
330
320
531
353
update-plots-monitors
update-plots-monitors
0
1
-1000

SLIDER
325
420
497
453
num-parallel-runs
num-parallel-runs
1
bspace:get-recommended-max-parallel-runs
9.0
1
1
NIL
HORIZONTAL

BUTTON
330
45
485
78
set output directory
set output-directory user-directory
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

MONITOR
600
40
888
85
output directory
word output-directory \"\"
17
1
11

BUTTON
15
375
157
408
clear experiments
bspace:clear-experiments\nclear-output
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
15
275
212
308
rename current experiment
bspace:rename-experiment user-input \"new name for experiment\"\nclear-output
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
15
225
222
258
duplicate current experiment
bspace:duplicate-experiment user-input \"name for duplicated experiment\"\nclear-output
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

MONITOR
15
40
300
85
current experiment
bspace:get-current-experiment
17
1
11

BUTTON
15
420
188
453
set current experiment
let current-experiment-name  user-input \"name for current experiment\"\nbspace:set-current-experiment current-experiment-name
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

MONITOR
430
365
535
410
max parallel runs
bspace:get-recommended-max-parallel-runs
17
1
11

OUTPUT
645
480
895
740
13

BUTTON
675
345
807
378
List Experiments
clear-output\noutput-print bspace:get-experiments
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
615
390
892
423
Current Experiment Parameters
clear-output\noutput-print bspace:get-parameters bspace:get-current-experiment
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
680
435
817
468
Clear Information
clear-output
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SWITCH
330
90
517
123
spreadsheet-output
spreadsheet-output
0
1
-1000

SWITCH
330
135
467
168
table-output
table-output
0
1
-1000

SWITCH
330
180
492
213
statistics-output
statistics-output
0
1
-1000

SWITCH
330
225
462
258
lists-output
lists-output
0
1
-1000

TEXTBOX
35
10
215
41
Manipulate Experiments
13
0.0
1

TEXTBOX
335
15
485
33
Set Run Parameters
13
0.0
1

TEXTBOX
645
315
845
346
Output Experiment Information
13
0.0
1

MONITOR
325
365
420
410
default # runs
bspace:get-default-parallel-runs
17
1
11

SWITCH
15
535
202
568
sequential-run-order
sequential-run-order
1
1
-1000

SWITCH
15
580
217
613
run-metrics-every-step
run-metrics-every-step
0
1
-1000

SWITCH
15
630
242
663
produce-repeatable-results
produce-repeatable-results
1
1
-1000

TEXTBOX
30
465
180
483
Changeable Parameters
12
0.0
1

MONITOR
600
145
1045
190
Table Name
get-table-name
17
1
11

BUTTON
15
135
162
168
update experiment
set-changeable-values\nclear-output
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

TEXTBOX
940
475
1145
571
Changing these values will not change the programmatically created experiments. \nThey will have an effect if you execute the procedures setup and run.
11
0.0
1

MONITOR
600
90
1045
135
Spreadsheet Name
get-spreadsheet-name
17
1
11

MONITOR
600
200
1045
245
Stats Name
get-stats-name
17
1
11

MONITOR
605
255
1045
300
NIL
get-lists-name
17
1
11

@#$#@#$#@
## WHAT IS IT?

This model demonstrates the ideas of competitive co-evolution. In the model there are two species:  frogs and snakes. The snakes are the only predators of the frogs, but the frogs produce a fast acting poison that kills the snakes before they can be eaten. However, the snakes have developed an anti-venom to counter the frog's poison. In this model, we assume that there are no other predators of the frogs, or prey that are consumed by the snakes.  As such the two species enter a biological arms race in order to keep up with each other.

The name Red Queen comes from Lewis Carroll's "Through the Looking Glass", where the Red Queen says, "...it takes all the running you can do, to keep in the same place."  In this model under the right conditions, the two species evolve as fast as they can, but neither is able to gain an advantage over the other.

## HOW IT WORKS

When SETUP is pressed, INITIAL-NUMBER-SNAKES and INITIAL-NUMBER-FROGS are created.  Each of these have a poison or resistance drawn from a normal distribution with means of INITIAL-RESISTANCE-MEAN and INITIAL-POISON-MEAN with a standard deviation of 1.

Once GO is pressed, all organisms move with a random walk.  When a snake encounters a frog, the snake will try to eat the frog. If the frog's poison is stronger than snake's resistance the snake will die. If the snake's resistance is higher than the frog's poison the frog will die.  If the poison and resistance are equal then both individuals survive.

At the end of every tick, each animal get a chance to reproduce.  In order to reproduce the count of its species must be below half of MAX-INDIVIDUALS.  The chance of reproduction is still 5 in 100.  If the animal does reproduce, a new individual is created which has a resistance or poison drawn from a normal distribution with mean equal to the parent's value, and standard deviation of 1.

## HOW TO USE IT

First set the parameters of the model.  INITIAL-NUMBER-SNAKES and INITIAL-NUMBER-FROGS controls the initial number of each species.  INITIAL-RESISTANCE-MEAN and INITIAL-POISON-MEAN control the distributions from which the initial values for the snakes and frogs, respectively.  MAX-INDIVIDUALS controls the total carrying capacity of the environment.  Once these values are set, press SETUP and GO to watch the model run.

## THINGS TO NOTICE

With the initial settings of the model, both of the species will usually persist for a long period of time.  Both of the species persist, but their population levels change over time, what is the relationship between the populations of the frogs and snakes?  What happens to the levels of the poison and resistance during this time?

## THINGS TO TRY

Modify the INITIAL-RESISTANCE-MEAN and INITIAL-POISON-MEAN, do both species continue to persist?  What happens to the resistance and poison values?

Set the INITIAL-RESISTANCE-MEAN and INITIAL-POISON-MEAN to the same value, but change the INITIAL-NUMBER-FROGS and INITIAL-NUMBER-SNAKES, what happens to the population levels over time?  What happens to the poison and resistance values?

## EXTENDING THE MODEL

The frogs have their own shape, "frog top" but the snakes use the default turtle shape.  Create a snake shape for the snakes.

Originally, the reproduction of the individuals in this model is a global mechanism in that random individuals all over the environment are selected to reproduce, and snakes and frogs are selected equally likely.  We have changed this mechanism so that each agent gets a chance to reproduce each turn, making the reproduction rate population dependent, but there are many ways to make this change. Implement another individual-based mechanism. After this, change the model so that individuals that succeed in fights reproduce preferentially.

Currently the species in this model reproduce asexually.  Change the model so that it uses sexual reproduction.

## NETLOGO FEATURES

This model uses the "frog top" shape that was imported from the Shapes Library.

## RELATED MODELS

This model is related to the other BEAGLE models since they all examine evolution.

## CREDITS AND REFERENCES

This model is a part of the BEAGLE curriculum (http://ccl.northwestern.edu/rp/beagle/index.shtml)

This model is related to a model that was used as the basis for a poster published at the Genetic and Evolutionary Computation Conference.  This model uses a more individual-based reproductive mechanism, whereas the model in the paper used a global one:

"Coevolution of Predators and Prey in a Spatial Model: An Exploration of the Red Queen Effect" Jules Ottino-Loffler, William Rand, and Uri Wilensky (2007) GECCO 2007, London, UK

## HOW TO CITE

If you mention this model or the NetLogo software in a publication, we ask that you include the citations below.

For the model itself:

* Ottino-Loffler, J., Rand, W. and Wilensky, U. (2007).  NetLogo Red Queen model.  http://ccl.northwestern.edu/netlogo/models/RedQueen.  Center for Connected Learning and Computer-Based Modeling, Northwestern University, Evanston, IL.

Please cite the NetLogo software as:

* Wilensky, U. (1999). NetLogo. http://ccl.northwestern.edu/netlogo/. Center for Connected Learning and Computer-Based Modeling, Northwestern University, Evanston, IL.

## COPYRIGHT AND LICENSE

Copyright 2007 Uri Wilensky.

![CC BY-NC-SA 3.0](http://ccl.northwestern.edu/images/creativecommons/byncsa.png)

This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License.  To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons, 559 Nathan Abbott Way, Stanford, California 94305, USA.

Commercial licenses are also available. To inquire about commercial licenses, please contact Uri Wilensky at uri@northwestern.edu.

<!-- 2007 Cite: Ottino-Loffler, J., Rand, W. -->
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

arrow
true
0
Polygon -7500403 true true 150 0 0 150 105 150 105 293 195 293 195 150 300 150

box
false
0
Polygon -7500403 true true 150 285 285 225 285 75 150 135
Polygon -7500403 true true 150 135 15 75 150 15 285 75
Polygon -7500403 true true 15 75 15 225 150 285 150 135
Line -16777216 false 150 285 150 135
Line -16777216 false 150 135 15 75
Line -16777216 false 150 135 285 75

bug
true
0
Circle -7500403 true true 96 182 108
Circle -7500403 true true 110 127 80
Circle -7500403 true true 110 75 80
Line -7500403 true 150 100 80 30
Line -7500403 true 150 100 220 30

butterfly
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

car
false
0
Polygon -7500403 true true 300 180 279 164 261 144 240 135 226 132 213 106 203 84 185 63 159 50 135 50 75 60 0 150 0 165 0 225 300 225 300 180
Circle -16777216 true false 180 180 90
Circle -16777216 true false 30 180 90
Polygon -16777216 true false 162 80 132 78 134 135 209 135 194 105 189 96 180 89
Circle -7500403 true true 47 195 58
Circle -7500403 true true 195 195 58

circle
false
0
Circle -7500403 true true 0 0 300

circle 2
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240

cow
false
0
Polygon -7500403 true true 200 193 197 249 179 249 177 196 166 187 140 189 93 191 78 179 72 211 49 209 48 181 37 149 25 120 25 89 45 72 103 84 179 75 198 76 252 64 272 81 293 103 285 121 255 121 242 118 224 167
Polygon -7500403 true true 73 210 86 251 62 249 48 208
Polygon -7500403 true true 25 114 16 195 9 204 23 213 25 200 39 123

cylinder
false
0
Circle -7500403 true true 0 0 300

dot
false
0
Circle -7500403 true true 90 90 120

face happy
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 255 90 239 62 213 47 191 67 179 90 203 109 218 150 225 192 218 210 203 227 181 251 194 236 217 212 240

face neutral
false
0
Circle -7500403 true true 8 7 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Rectangle -16777216 true false 60 195 240 225

face sad
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 168 90 184 62 210 47 232 67 244 90 220 109 205 150 198 192 205 210 220 227 242 251 229 236 206 212 183

fish
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

flag
false
0
Rectangle -7500403 true true 60 15 75 300
Polygon -7500403 true true 90 150 270 90 90 30
Line -7500403 true 75 135 90 135
Line -7500403 true 75 45 90 45

flower
false
0
Polygon -10899396 true false 135 120 165 165 180 210 180 240 150 300 165 300 195 240 195 195 165 135
Circle -7500403 true true 85 132 38
Circle -7500403 true true 130 147 38
Circle -7500403 true true 192 85 38
Circle -7500403 true true 85 40 38
Circle -7500403 true true 177 40 38
Circle -7500403 true true 177 132 38
Circle -7500403 true true 70 85 38
Circle -7500403 true true 130 25 38
Circle -7500403 true true 96 51 108
Circle -16777216 true false 113 68 74
Polygon -10899396 true false 189 233 219 188 249 173 279 188 234 218
Polygon -10899396 true false 180 255 150 210 105 210 75 240 135 240

frog top
true
0
Polygon -7500403 true true 146 18 135 30 119 42 105 90 90 150 105 195 135 225 165 225 195 195 210 150 195 90 180 41 165 30 155 18
Polygon -7500403 true true 91 176 67 148 70 121 66 119 61 133 59 111 53 111 52 131 47 115 42 120 46 146 55 187 80 237 106 269 116 268 114 214 131 222
Polygon -7500403 true true 185 62 234 84 223 51 226 48 234 61 235 38 240 38 243 60 252 46 255 49 244 95 188 92
Polygon -7500403 true true 115 62 66 84 77 51 74 48 66 61 65 38 60 38 57 60 48 46 45 49 56 95 112 92
Polygon -7500403 true true 200 186 233 148 230 121 234 119 239 133 241 111 247 111 248 131 253 115 258 120 254 146 245 187 220 237 194 269 184 268 186 214 169 222
Circle -16777216 true false 157 38 18
Circle -16777216 true false 125 38 18

house
false
0
Rectangle -7500403 true true 45 120 255 285
Rectangle -16777216 true false 120 210 180 285
Polygon -7500403 true true 15 120 150 15 285 120
Line -16777216 false 30 120 270 120

leaf
false
0
Polygon -7500403 true true 150 210 135 195 120 210 60 210 30 195 60 180 60 165 15 135 30 120 15 105 40 104 45 90 60 90 90 105 105 120 120 120 105 60 120 60 135 30 150 15 165 30 180 60 195 60 180 120 195 120 210 105 240 90 255 90 263 104 285 105 270 120 285 135 240 165 240 180 270 195 240 210 180 210 165 195
Polygon -7500403 true true 135 195 135 240 120 255 105 255 105 285 135 285 165 240 165 195

line
true
0
Line -7500403 true 150 0 150 300

line half
true
0
Line -7500403 true 150 0 150 150

pentagon
false
0
Polygon -7500403 true true 150 15 15 120 60 285 240 285 285 120

person
false
0
Circle -7500403 true true 110 5 80
Polygon -7500403 true true 105 90 120 195 90 285 105 300 135 300 150 225 165 300 195 300 210 285 180 195 195 90
Rectangle -7500403 true true 127 79 172 94
Polygon -7500403 true true 195 90 240 150 225 180 165 105
Polygon -7500403 true true 105 90 60 150 75 180 135 105

plant
false
0
Rectangle -7500403 true true 135 90 165 300
Polygon -7500403 true true 135 255 90 210 45 195 75 255 135 285
Polygon -7500403 true true 165 255 210 210 255 195 225 255 165 285
Polygon -7500403 true true 135 180 90 135 45 120 75 180 135 210
Polygon -7500403 true true 165 180 165 210 225 180 255 120 210 135
Polygon -7500403 true true 135 105 90 60 45 45 75 105 135 135
Polygon -7500403 true true 165 105 165 135 225 105 255 45 210 60
Polygon -7500403 true true 135 90 120 45 150 15 180 45 165 90

square
false
0
Rectangle -7500403 true true 30 30 270 270

square 2
false
0
Rectangle -7500403 true true 30 30 270 270
Rectangle -16777216 true false 60 60 240 240

star
false
0
Polygon -7500403 true true 151 1 185 108 298 108 207 175 242 282 151 216 59 282 94 175 3 108 116 108

target
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240
Circle -7500403 true true 60 60 180
Circle -16777216 true false 90 90 120
Circle -7500403 true true 120 120 60

tree
false
0
Circle -7500403 true true 118 3 94
Rectangle -6459832 true false 120 195 180 300
Circle -7500403 true true 65 21 108
Circle -7500403 true true 116 41 127
Circle -7500403 true true 45 90 120
Circle -7500403 true true 104 74 152

triangle
false
0
Polygon -7500403 true true 150 30 15 255 285 255

triangle 2
false
0
Polygon -7500403 true true 150 30 15 255 285 255
Polygon -16777216 true false 151 99 225 223 75 224

truck
false
0
Rectangle -7500403 true true 4 45 195 187
Polygon -7500403 true true 296 193 296 150 259 134 244 104 208 104 207 194
Rectangle -1 true false 195 60 195 105
Polygon -16777216 true false 238 112 252 141 219 141 218 112
Circle -16777216 true false 234 174 42
Rectangle -7500403 true true 181 185 214 194
Circle -16777216 true false 144 174 42
Circle -16777216 true false 24 174 42
Circle -7500403 false true 24 174 42
Circle -7500403 false true 144 174 42
Circle -7500403 false true 234 174 42

turtle
true
0
Polygon -10899396 true false 215 204 240 233 246 254 228 266 215 252 193 210
Polygon -10899396 true false 195 90 225 75 245 75 260 89 269 108 261 124 240 105 225 105 210 105
Polygon -10899396 true false 105 90 75 75 55 75 40 89 31 108 39 124 60 105 75 105 90 105
Polygon -10899396 true false 132 85 134 64 107 51 108 17 150 2 192 18 192 52 169 65 172 87
Polygon -10899396 true false 85 204 60 233 54 254 72 266 85 252 107 210
Polygon -7500403 true true 119 75 179 75 209 101 224 135 220 225 175 261 128 261 81 224 74 135 88 99

wheel
false
0
Circle -7500403 true true 3 3 294
Circle -16777216 true false 30 30 240
Line -7500403 true 150 285 150 15
Line -7500403 true 15 150 285 150
Circle -7500403 true true 120 120 60
Line -7500403 true 216 40 79 269
Line -7500403 true 40 84 269 221
Line -7500403 true 40 216 269 79
Line -7500403 true 84 40 221 269

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270
@#$#@#$#@
NetLogo 6.4.0
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
<experiments>
  <experiment name="Beta2" repetitions="3" sequentialRunOrder="false" runMetricsEveryStep="false">
    <setup>random-seed (474 + behaviorspace-run-number) setup</setup>
    <go>go</go>
    <timeLimit steps="5000"/>
    <metric>count frogs</metric>
    <metric>count snakes</metric>
    <metric>average-poison</metric>
    <metric>average-resistance</metric>
    <enumeratedValueSet variable="initial-resistance-mean">
      <value value="25"/>
    </enumeratedValueSet>
    <steppedValueSet variable="initial-poison-mean" first="25" step="5" last="50"/>
    <enumeratedValueSet variable="initial-number-frogs">
      <value value="150"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="initial-number-snakes">
      <value value="150"/>
    </enumeratedValueSet>
  </experiment>
  <experiment name="Final Counts" repetitions="10" runMetricsEveryStep="false">
    <setup>setup</setup>
    <go>go</go>
    <timeLimit steps="5000"/>
    <metric>count frogs</metric>
    <metric>count snakes</metric>
    <enumeratedValueSet variable="initial-number-frogs">
      <value value="150"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="initial-resistance-mean">
      <value value="25"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="initial-number-snakes">
      <value value="150"/>
    </enumeratedValueSet>
    <steppedValueSet variable="initial-poison-mean" first="25" step="5" last="50"/>
  </experiment>
</experiments>
@#$#@#$#@
@#$#@#$#@
default
0.0
-0.2 0 0.0 1.0
0.0 1 1.0 0.0
0.2 0 0.0 1.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180
@#$#@#$#@
1
@#$#@#$#@
