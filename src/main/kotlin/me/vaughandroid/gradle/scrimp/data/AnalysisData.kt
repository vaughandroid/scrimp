package me.vaughandroid.gradle.scrimp.data

import kotlinx.serialization.Serializable

@Serializable
data class AnalysisData(
    val commitRef: String,
    val modulesWithChanges: Set<String>,
    val modulesImpactedByChanges: Set<String>
)