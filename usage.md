---
layout: default
title: Usage
nav_order: 4
permalink: /usage
---

# Usage

## Use `scrimpRun` to run tasks on changed modules

This is the most straightforward "out of the box" way to use Scrimp. It will attempt to run all the given tasks on any modules which have changed, or which have been impacted by changes to their dependencies.

`$ ./gradlew scrimpRun -PscrimpTasks="<task list>" -PscrimpExtraArgs="<extra Gradle arguments>" -PscrimpCommit=<commit ref>`

* The 'scrimpTasks parameter' is required, and should be a list of one or more task names separated by spaces. You can safely include the names of tasks which only exist for some modules.
* The 'scrimpCommit' parameter is optional, and will default to HEAD (i.e. changes since the last commit).
* The 'scrimpExtraArgs' parameter is optional, but can be a list of one or more additional Gradle parameters to use when running the tasks. e.g. "--parallel".

Example 1: Run tests on uncommitted changes:

`$ ./gradlew scrimpRun -PscrimpTasks=test`

Example 2: Run tests and connected tests covering changes since commit `3b7e70c`:

`$ ./gradlew scrimpRun -PscrimpTasks="test connectedCheck" -PscrimpCommit=3b7e70c`

### Analyse changed and impacted modules

If you need to apply some customisation - e.g. running different tasks for different modules - then Scrimp can produce a report of changed and impacted modules.

`$ ./gradlew scrimpAnalyse -PscrimpCommit=<commit ref>`

This will output a file at `<build dir>/scrimp/module-analysis.json`.

* The 'scrimpCommit' parameter is optional, and will default to HEAD (i.e. changes since the last commit).

Example: Analyse changes since commit `3b7e70c`:

`$ ./gradlew scrimpAnalyse -PscrimpCommit=3b7e70c`

## Getting a commit reference

Strictly speaking, this is outside the scope of the plugin, and you may wish to use different strategies in different circumstances. However, here are some suggestions:

### Local development

When developing locally, you may wish to validate changes before each commit. In this case, you can not specify a commit and Scrimp will use `HEAD` by default - i.e. it will only look at uncommitted changes.

### CI

For CI, one strategy is to have a "known good" branch, which is updated by your CI when tests pass successfully on your main branch. Say you call this branch `green-master`.

1. When tasks complete successfully on main, have your CI run `$ git push origin HEAD:refs/heads/green-master`.
2. On PRs, use `$ git merge-base HEAD green-master` to find the most recent common ancestor of the PR branch and green master. E.g. `$ ./gradlew scrimpRun -PscrimpTasks="test" -PscrimpExtraArgs="--parallel" -PscrimpCommit=$(git merge-base HEAD green)`


## Other tasks

### Output file with Gradle arguments

`$ ./gradlew scrimpListTasks -scrimpCreateArgumentsFile="<task list> -PscrimpExtraArgs="<extra Gradle arguments>" -PscrimpCommit=<commit ref>`

This will output a file at `<build dir>/scrimp/filtered-arguments.txt`.

You can run this and then run `$ ./gradlew $(cat build/scrimp/filtered-arguments.txt)` to separate the steps of filtering tasks and running them.

### List tasks (but do not run them)

`$ ./gradlew scrimpListTasks -PscrimpTasks="<task list>" -PscrimpCommit=<commit ref>`

This will output a file at `<build dir>/scrimp/filtered-tasks.txt`.

### Print module graph

`$ ./gradlew scrimpPrintModuleGraph`

This will print a view of the module graph to the console.
