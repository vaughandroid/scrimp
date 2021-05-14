package me.vaughandroid.gradle.scrimp.data

import kotlinx.serialization.json.Json
import java.nio.file.Path

class AnalysisStore(
    private val filePath: Path
) {

    fun store(analysisData: AnalysisData) {
        val text = Json.encodeToString(
            AnalysisData.serializer(),
            analysisData
        )
        filePath.toFile().writeText(text)
    }

    fun retrieve(): AnalysisData? {
        val text = filePath.toFile().readText()
        return if (text.isNotEmpty()) Json.decodeFromString(
            AnalysisData.serializer(),
            text
        ) else null
    }

}