---
layout: default
title: Why is this needed?
nav_order: 2
permalink: /why-is-this-needed
---

# Why is this needed?

## The general case

Projects of all sizes can benefit from reduced cycle times. For many development and CI workflows, the slowest part of the process is running tests.

Gradle has [up-to-date checks](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:up_to_date_checks) and the [Build Cache](https://docs.gradle.org/current/userguide/build_cache.html), both of which are designed to avoid repetition of work, by caching task outputs which can be reused when the task inputs have not changed. These are both great tools, but they both have a number of limitations:

1. CI systems typically use "clean" builds, so tasks are never considered up-to-date.
2. You may not be able to use the Build Cache, for technical or organisational reasons.

## On Android

If you are developing for Android, instrumented tests are particularly problematic.

* They are [not cacheable](https://issuetracker.google.com/issues/115873051) by Gradle.
* They are slow.
* They can be flaky.

For these reasons, running instrumented tests locally can lead to a lot of wasted developer time, and running them in CI environments can lead to bottlenecks and long feedback cycles.
