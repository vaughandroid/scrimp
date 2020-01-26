package me.vaughandroid.gradle.scrimp.data

import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import java.nio.file.Path

class AnalysisStore(
    private val filePath: Path
) {

    @UseExperimental(UnstableDefault::class)
    fun store(analysisData: AnalysisData) {
        val text = Json.stringify(
            AnalysisData.serializer(),
            analysisData
        )
        filePath.toFile().writeText(text)
    }

    @UseExperimental(UnstableDefault::class)
    fun retrieve(): AnalysisData? {
        val text = filePath.toFile().readText()
        return if (text.isNotEmpty()) Json.parse(
            AnalysisData.serializer(),
            text
        ) else null
    }

}