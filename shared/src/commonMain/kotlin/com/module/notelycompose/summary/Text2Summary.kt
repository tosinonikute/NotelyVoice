/*
 * Copyright (C) 2025 Shubham Panchal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.module.notelycompose.summary

import kotlin.jvm.JvmStatic

// The Text2Summary API for Android.
class Text2Summary {
    companion object {
        // Summarizes the given text. Note, this method should be used whe you're dealing with long texts.
        // It performs the summarization on the background thread. Once the process is complete the summary is
        // passed to the SummaryCallback.onSummaryProduced callback.
        @JvmStatic
        suspend fun summarize(
            text: String,
            compressionRate: Float,
        ): String {
            val sentences = Tokenizer.textToSentences(Tokenizer.removeLineBreaks(text))
            println("Sentences : ${sentences.size}")
            val tfidfSummarizer = TFIDFSummarizer()
            val summarySentenceIndices = tfidfSummarizer.compute(text, compressionRate)
            return buildString(sentences, summarySentenceIndices).trim()
        }

        // Fetchs the sentences from topNValues and concatenates them to produce a complete. String.
        private fun buildString(
            sentences: Array<String>,
            topNValues: Array<Int>,
        ): String {
            var summary = ""
            topNValues.forEach {
                summary += sentences[it] + " "
            }
            return summary
        }
    }
}
