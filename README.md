# Scrimp

## About

Scrimp is a Gradle plugin designed to only do work when your code actually changes.

It analyses which modules have been impacted by changes in source control, and lets you run tests (or any tasks you like) on just those modules.

It is particularly suited for multi-module Android projects, which use instrumented tests.

You can [read the user guide here](https://vaughandroid.me/scrimp/).

## Tasks

### scrimpRun

Run tasks on impacted modules.

`./gradlew scrimpRun -PscrimpTasks="<task list>" -PscrimpExtraArgs="<extra Gradle arguments>" -PscrimpCommit=<commit ref>`

* The 'scrimpTasks parameter' is required, and should be a list of one or more task names separated by spaces. You can safely include the names of tasks which only exist for some modules.
* The 'scrimpCommit' parameter is optional, and will default to HEAD (i.e. changes since the last commit).
* The 'scrimpExtraArgs' parameter is optional, but can be a list of one or more additional Gradle parameters to use when running the tasks. e.g. "--parallel".

Example 1: Run tests on uncommitted changes:

`./gradlew scrimpRun -PscrimpTasks=test`

Example 2: Run tests and connected tests covering changes since commit `3b7e70c`:

`./gradlew scrimpRun -PscrimpTasks="test connectedCheck" -PscrimpCommit=3b7e70c`

### scrimpAnalyse

Produce a report of changed and impacted modules.

`./gradlew scrimpAnalyse -PscrimpCommit=<commit ref>`

This will output a file at `<build dir>/scrimp/module-analysis.json`.

* The 'scrimpCommit' parameter is optional, and will default to HEAD (i.e. changes since the last commit).

Example: Analyse changes since commit `3b7e70c`:

`./gradlew scrimpAnalyse -PscrimpCommit=3b7e70c`

### scrimpListTasks

`./gradlew scrimpListTasks -PscrimpTasks="<task list>" -PscrimpCommit=<commit ref>`

This will output a file at `<build dir>/scrimp/filtered-tasks.txt`.

## scrimpCreateArgumentsFile

`./gradlew scrimpCreateArgumentsFile -PscrimpExtraArgs="<extra Gradle arguments>" -PscrimpCommit=<commit ref>`

This will output a file at `<build dir>/scrimp/filtered-arguments.txt`.

You can run this and then run `./gradlew $(cat build/scrimp/filtered-arguments.txt)` to separate the steps of filtering tasks and running them.

### Print module graph

Print Scrimp's interpretation of the module graph to the console.

`./gradlew scrimpPrintModuleGraph`
