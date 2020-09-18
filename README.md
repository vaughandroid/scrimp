# Scrimp

## About

> **scrimp**
  *verb*
  be thrifty or parsimonious; economize.

Scrimp is a Gradle plugin designed to only run tests when your code actually changes.

The plugin analyses which modules have been impacted by changes in source control, and lets you run tests (or any tasks you like) on just those modules.

It is particularly suited for multi-module Android projects, which use instrumented tests.

## Why is this needed?

### The general case

Projects of all sizes can benefit from reduced cycle times. For many development and CI workflows, the slowest part of the process is running tests.

Gradle has [up-to-date checks](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:up_to_date_checks) and the [Build Cache](https://docs.gradle.org/current/userguide/build_cache.html), both of which are designed to avoid repetition of work, by caching task outputs which can be reused when the task inputs have not changed. These are both great tools, but they both have a number of limitations:

1. CI systems typically use "clean" builds, so tasks are never considered up-to-date.
2. You may not be able to use the Build Cache, for technical or organisational reasons.

### On Android

If you are developing for Android, instrumented tests are particularly problematic.

* They are [not cacheable](https://issuetracker.google.com/issues/115873051) by Gradle.
* They are slow.
* They can be flaky.

For these reasons, running instrumented tests locally can lead to a lot of wasted developer time, and running them in CI environments can get expensive quickly.

## Usage

### Apply the plugin

In your top-level build.gradle:

```groovy
buildscript {
    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath 'com.github.vaughandroid:scrimp:master-SNAPSHOT'
    }
}

apply plugin: 'scrimp'
```

### Run tests on impacted modules

`./gradlew scrimpRun -PscrimpTasks="<task list>" -PscrimpCommit=<commit ref>`

* The 'scrimpTasks parameter' is required, and should be a list of one or more task names separated by spaces. You can safely include the names of tasks which only exist for some modules.
* The 'scrimpCommit' parameter is optional, and will default to HEAD (i.e. changes since the last commit).

Example 1: Run tests on uncommitted changes:

`./gradlew scrimpRun -PscrimpTasks=test`

Example 2: Run tests and connected tests covering changes since commit `3b7e70c`:

`./gradlew scrimpRun -PscrimpTasks="test connectedCheck" -PscrimpCommit=3b7e70c`

## How does it work?

Given a commit reference and a list of tasks to run for each module, the plugin will:

1. Establish which files have changed.
2. Work out out which modules those files belong to.
3. Work out which modules depend on *those* modules (and so on...).
4. Produce a set of tasks to be run.
5. Run the final set of tasks.

## Tips

### Test all changes on a branch

You can use `git merge-base HEAD <branch>` to find the most recent common ancestor of this branch and another branch.

So, to run tests for all changes since branching from 'master' you can do:

`./gradlew scrimpRun -PscrimpTasks="test" -PscrimpCommit=$(git merge-base HEAD master)`

### List tasks (but do not run them)

`./gradlew scrimpListTasks -PscrimpTasks="<task list>" -PscrimpCommit=<commit ref>`

This will output a file at `<build dir>/scrimp/filtered-tasks.txt`.

### Analyse changed and impacted modules 

`./gradlew scrimpAnalyse -PscrimpCommit=<commit ref>`

This will output a file at `<build dir>/scrimp/module-analysis.json`.

### Print module graph

`./gradlew scrimpPrintModuleGraph`

This will print a view of the module graph to the console. 