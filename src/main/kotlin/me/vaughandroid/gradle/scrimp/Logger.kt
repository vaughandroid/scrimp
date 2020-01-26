package me.vaughandroid.gradle.scrimp

class Logger(
    private val enabled: Boolean = true
) {

    fun log(message: String) {
        @Suppress("ConstantConditionIf")
        if (enabled) {
            println(message)
        }
    }

    fun logList(items: Collection<Any>, title: String) {
        @Suppress("ConstantConditionIf")
        if (enabled) {
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