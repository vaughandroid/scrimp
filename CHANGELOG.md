# Changelog

## To do

* Fix StackOverFlow when printing graph with circular reference.
* Output file change analysis.
* Clean up logging.
* Make it possible to pass flags.
* Handle case where there are no changes.
* README updates:
    * Diagrams.
* Publish to Gradle Plugin Repository.
* Separate task to get a list of the changed files. (Makes caching possible for AnalyseImpactedModulesTask.)
* Add end-to-end tests.

## v0.1.4

* Support Gradle v7.x

## v0.1.3

* Support cyclic dependencies.

## v0.1.2

Not released.

## v0.1.1

* Fix all changes being attributed to the root module.
* Add support for 'scrimpExtraArgs' argument and a new 'scrimpCreateArgumentsFile' task.

## v0.1.0

First release!