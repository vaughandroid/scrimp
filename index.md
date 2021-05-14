---
layout: default
title: Home
nav_order: 1
permalink: /
---

# Don't do work you don't have to

> **scrimp**
  *verb*
  be thrifty or parsimonious; economize.

Scrimp is a Gradle plugin designed to only run tests on modules where the code has actually changed.

The plugin analyses which modules have been impacted by changes in source control, and lets you run tests (or any tasks you like!) on just those modules.

You can use it on your local machine, or as part of your CI setup. It is particularly suited for multi-module Android projects, which use instrumented tests.

[View it on GitHub](https://github.com/vaughandroid/scrimp){: .btn .fs-5 .mb-4 .mb-md-0 }

---

## Quick start

As a quick demo, we're going to see how to run tests on some uncommitted code.

If you want to see how you can run tests for code you have already committed, run other tasks, or how you can use Scrimp in a CI environment, head to [the usage page]({% link usage.md %}).

### 1. Apply the plugin

In your top-level build.gradle:

```groovy
buildscript {
    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath 'com.github.vaughandroid:scrimp:0.1.4'
    }
}

apply plugin: 'scrimp'
```

### 2. Commit the change

`$ git commit -am "Trying out the Scrimp plugin."`

### 3. Make some changes to your code

You'll have to figure this one out on your own.

Note that you will get much better results if you change 

### 4. Run tests only on impacted modules

`./gradlew scrimpRun -PscrimpTasks=test`
