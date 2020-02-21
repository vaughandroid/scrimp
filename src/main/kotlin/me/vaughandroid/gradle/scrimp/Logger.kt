package me.vaughandroid.gradle.scrimp

import org.gradle.api.Project

class Logger(
    private val project: Project,
    private val printLogs: Boolean = true
) {

    fun log(message: String) {
        project.logger.debug(message)
        if (printLogs) {
            println(message)
        }
    }

    fun logList(items: Collection<Any>, title: String) {
        if (printLogs) {
            if (items.isEmpty()) {
                log("$title none!")
            } else {
                log(title)
                items
                    .map { "    $it" }
                    .sorted()
                    .forEach { log(it) }
            }
        }
    }

}