package me.vaughandroid.gradle.scrimp.core

interface ModuleNameProvider {
    val rootProjectName: String
    val moduleNames: Set<String>
}