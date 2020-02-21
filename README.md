# Scrimp (Source ContRol Impacted Modules Plugin)

## About

> **scrimp**
  *verb*
  be thrifty or parsimonious; economize.

Scrimp is a Gradle plugin designed to minimize the amount of tests you have to run - locally, or on CI.

The plugin analyses which modules have been impacted by changes in source control, and lets you run tests (or any tasks you like) on just those modules.

### Why is this needed?

Gradle's caching mechanism is great, but it isn't 100% reliable. It also isn't much use in a CI environment where builds are done from scratch.

For large projects, running all the tests can take a long time.

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

plugins {
    id 'scrimp'
}
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
