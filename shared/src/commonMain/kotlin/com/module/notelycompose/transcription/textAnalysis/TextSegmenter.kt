package com.module.notelycompose.transcription.textAnalysis

interface TextSegmenter {

    fun segmentText(text: String): List<String>
}

val segmenters: Map<String, TextSegmenter> = mapOf(
    "hi" to HindiTextSegmenter(),
    "gu" to GujaratiTextSegmenter()
)

class DefaultSegmenter : TextSegmenter {
    override fun segmentText(text: String) = listOf(text) // no segmentation
}

fun getSegmenter(lang: String): TextSegmenter = segmenters[lang] ?: DefaultSegmenter()
