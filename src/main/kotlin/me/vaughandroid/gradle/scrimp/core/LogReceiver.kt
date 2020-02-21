package me.vaughandroid.gradle.scrimp.core

interface LogReceiver {

    fun log(message: String)
    fun logList(items: Collection<Any>, title: String)

}