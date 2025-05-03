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

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// Helper class which transforms texts to tokens, builds ( word , frequency )  HashMaps.
class Tokenizer {
    companion object {
        // Returns a list of sentences from the given paragraph.
        fun textToSentences(para: String): Array<String> {
            val text = para.trim()
            val pattern =
                Regex("(?m)(?:(?<=\\s)|^)([^.!?\\n]+(?:[.!?]+|(?=\\n|\$)))")
            val result =  pattern.findAll(text).map {
                it.value
            }.toList().toTypedArray()
            return result
        }

        // Returns a list of tokens ( words ) from a given sentence ( seq of words ).
        fun sentenceToTokens(s: String): Array<String> {
            val sentence = s.trim().toLowerCase()
            var tokens = sentence.split(" ")
            tokens = tokens.map { Regex("[^A-Za-z0-9 ]").replace(it, "") }
            tokens = tokens.filter { !ENGLISH_STOP_WORDS.contains(it.trim()) }
            tokens = tokens.filter { it.trim().isNotEmpty() and it.trim().isNotBlank() }
            return tokens.toTypedArray()
        }

        // Builds a ( word , frequency ) HashMap.
        fun buildVocab(words: Array<String>): Map<String, Int> {
            val sortedWords = words.toSet()
            val vocab = HashMap<String, Int>()
            for (word in sortedWords) {
                vocab[word] = words.count { it.equals(word) }
            }
            return vocab
        }

        // Builds a ( word , weighted_frequency ) HashMap.
        fun getWeightedVocab(vocab: Map<String, Int>): Map<String, Float> {
            val maxFreq = vocab.values.max().toFloat()
            val weightedFreqHashMap = HashMap<String, Float>()
            vocab.entries.forEach {
                weightedFreqHashMap.put(it.key, it.value.toFloat() / maxFreq)
            }
            return weightedFreqHashMap
        }

        // Removes \n and \r from the given String.
        fun removeLineBreaks(para: String): String =
            para

        // Checks if the compression rate lie in ( 0 , 1 ].
        fun checkRate(rate: Float): Boolean = rate > 0.0 && rate < 1.0

        // Get the indices of top N maximum values in X.
        fun getTopNIndices(
            x: Array<Float>,
            xSorted: Array<Float>,
            N: Int,
        ): Array<Int> {
            val topN = xSorted.take(N)
            val topNIndices = ArrayList<Int>()
            for (i in topN) {
                topNIndices.add(x.indexOf(i))
            }
            topNIndices.sort()
            return topNIndices.distinct().toTypedArray()
        }
    }
}
