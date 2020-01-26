# SCRIMP (Source ContRol Impacted Modules Plugin)

## About

> **scrimp**
  *verb*
  be thrifty or parsimonious; economize.

A Gradle plugin designed to minimize the amount of work your CI has to do.

The plugin analyses which modules have been affected by changes in source control, and lets you run a set of tasks on just those modules.

### Why is this needed?

Gradle's caching mechanism is great, but it isn't much use in a CI environment where every build is a 'clean' build.
 
### How does it work?

Given a commit reference for the last successful build and a list of tasks to run, the plugin can:

1. Establish which files have changed.
2. Figure out which modules those files belong to.
3. Figure out which modules depend on *those* modules.
4. Produce a filtered set of tasks by checking which of the list of tasks applies to each module.
5. Run the final set of tasks.

## Applying the plugin

In your project's build.gradle:

```groovy
plugins {
    id 'scrimp'
}
```

## Usage

Typically you want to run a set of tasks on any impacted modules, which you can do as follows:

`./gradlew scrimpRun -PscrimpTasks="<task list>" -PscrimpCommit=<commit ref>`

The 'scrimpTasks parameter' is required, and should be a list of one or more task names separated by spaces.

Tasks are applied to all impacted modules, as long as they exist for that module. E.g. If you have a mix of Android and non-Android modules, you can safely include "connectedCheck" in the list.

The 'scrimpCommit' parameter is optional, and will default to HEAD (i.e. changes since the last commit).

E.g.

``./gradlew scrimpRun -PscrimpTasks="test connectedCheck" -PscrimpCommit=HEAD~7``

### Other operations


#### Analyse changed and impacted modules 

`./gradlew scrimpAnalyse -PscrimpCommit=<commit ref>`

This task will output a file at `<build dir>/scrimp/module-analysis.json`.

#### Prepare a list of tasks to run only on impacted modules

`./gradlew scrimpFilter -PscrimpTasks="<task list>" -PscrimpCommit=<commit ref>`

This task will output a file at `<build dir>/scrimp/filtered-tasks.txt`.
