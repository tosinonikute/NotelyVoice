package com.module.notelycompose.transcription.textAnalysis

class HindiTextSegmenter: TextSegmenter {

    // 1. Sentence ending patterns (verb forms that typically end sentences)
    private val sentenceEndingPatterns = listOf(
        // Present tense endings
        "है", "हैं", "हूँ", "हो",
        // Past tense endings
        "था", "थे", "थी", "थीं",
        // Future tense endings
        "होगा", "होंगे", "होगी", "होगीं",
        // Perfect tense endings
        "गया", "गए", "गई", "गईं", "गये",
        "दिया", "दिए", "दी", "दीं",
        "लिया", "लिए", "ली", "लीं",
        "किया", "किए", "की", "कीं",
        "हुआ", "हुए", "हुई", "हुईं",
        // Participle endings
        "कर", "करके", "करने", "आकर"
    )

    // 2. Topic transition indicators
    private val topicTransitions = listOf(
        "अब", "फिर", "इसके बाद", "उसके बाद", "वहीं", "दूसरी ओर",
        "इधर", "उधर", "यहाँ", "वहाँ", "इस बीच", "इस दौरान",
        "दूसरी खबर", "अगली खबर", "अब बात", "इस मामले में",
        "इस पर", "इससे", "लेकिन", "परंतु", "हालांकि"
    )

    // 3. News segment starters
    private val newsSegmentStarters = listOf(
        "देखिए", "जानिए", "समझिए", "सुनिए", "पहले", "दूसरे", "तीसरे",
        "मुख्य", "प्रमुख", "बड़ी", "अहम", "महत्वपूर्ण", "ख़ास", "विशेष",
        "रिपोर्ट", "समाचार", "न्यूज़", "ख़बर", "जानकारी", "सूत्र", "मामला"
    )

    // 4. Question indicators
    private val questionWords = listOf(
        "क्या", "कब", "कहाँ", "कैसे", "क्यों", "कौन", "किसे", "किसका", "कितना", "कितने"
    )

    // 5. Quote/speech indicators
    private val speechIndicators = listOf(
        "ने कहा", "बताया", "घोषणा की", "जानकारी दी", "स्पष्ट किया",
        "आरोप लगाया", "दावा किया", "मांग की", "निर्देश दिए"
    )

    // 6. Name patterns (for speaker changes)
    private val namePattern = Regex("[\\p{L}]{2,}\\s+[\\p{L}]{2,}(?:\\s+[\\p{L}]{2,})?")

    data class SentenceCandidate(
        val text: String,
        val startIndex: Int,
        val endIndex: Int,
        val confidence: Double
    )

    override fun segmentText(text: String): List<String> {
        // Step 1: Find all potential sentence boundaries
        val boundaries = findPotentialBoundaries(text)

        // Step 2: Score and filter boundaries
        val sentences = extractSentences(text, boundaries)

        // Step 3: Group sentences into paragraphs
        val paragraphs = groupIntoParagraphs(sentences)

        return paragraphs
    }

    private fun findPotentialBoundaries(text: String): List<Int> {
        val boundaries = mutableSetOf<Int>()

        // Find sentence ending patterns
        sentenceEndingPatterns.forEach { pattern ->
            findPatternOccurrences(text, pattern).forEach { index ->
                val confidence = scoreBoundaryConfidence(text, index + pattern.length)
                if (confidence > 0.6) {
                    boundaries.add(index + pattern.length)
                }
            }
        }

        // Find topic transitions
        topicTransitions.forEach { transition ->
            val index = text.indexOf(" $transition ")
            if (index > 0) {
                boundaries.add(index)
            }
        }

        return boundaries.sorted()
    }

    private fun findPatternOccurrences(text: String, pattern: String): List<Int> {
        val occurrences = mutableListOf<Int>()
        var index = 0

        while (index < text.length) {
            val found = text.indexOf(pattern, index)
            if (found == -1) break

            // Check if it's a word boundary (not part of a larger word)
            val beforeChar = if (found > 0) text[found - 1] else ' '
            val afterChar = if (found + pattern.length < text.length)
                text[found + pattern.length] else ' '

            if (beforeChar == ' ' && (afterChar == ' ' || afterChar.isWhitespace())) {
                occurrences.add(found)
            }

            index = found + 1
        }

        return occurrences
    }

    private fun scoreBoundaryConfidence(text: String, position: Int): Double {
        var score = 0.5 // Base score

        if (position >= text.length) return 1.0

        val before = text.substring(maxOf(0, position - 50), position)
        val after = text.substring(position, minOf(text.length, position + 50))

        // Increase score if followed by topic transition
        if (topicTransitions.any { after.trimStart().startsWith(it) }) {
            score += 0.3
        }

        // Increase score if followed by news segment starter
        if (newsSegmentStarters.any { after.contains(it) }) {
            score += 0.2
        }

        // Increase score if followed by a name (speaker change)
        if (namePattern.find(after.trimStart()) != null) {
            score += 0.2
        }

        // Decrease score if in the middle of a compound word or phrase
        if (before.endsWith("और") || before.endsWith("या") ||
            after.trimStart().startsWith("और") || after.trimStart().startsWith("या")) {
            score -= 0.2
        }

        // Check sentence length - too short sentences are suspicious
        val beforeLength = before.split(" ").size
        if (beforeLength < 3) {
            score -= 0.3
        }

        return score
    }

    private fun extractSentences(text: String, boundaries: List<Int>): List<String> {
        val sentences = mutableListOf<String>()
        var lastIndex = 0

        boundaries.forEach { boundary ->
            if (boundary > lastIndex) {
                val sentence = text.substring(lastIndex, boundary).trim()
                if (sentence.isNotEmpty() && sentence.split(" ").size >= 3) {
                    sentences.add(sentence)
                }
                lastIndex = boundary
            }
        }

        // Add remaining text
        if (lastIndex < text.length) {
            val remaining = text.substring(lastIndex).trim()
            if (remaining.isNotEmpty()) {
                sentences.add(remaining)
            }
        }

        return sentences
    }

    private fun groupIntoParagraphs(sentences: List<String>): List<String> {
        val paragraphs = mutableListOf<String>()
        var currentParagraph = mutableListOf<String>()

        sentences.forEachIndexed { index, sentence ->
            currentParagraph.add(sentence)

            val nextSentence = sentences.getOrNull(index + 1)

            if (shouldStartNewParagraph(sentence, nextSentence)) {
                paragraphs.add(currentParagraph.joinToString(". ") + ".")
                currentParagraph.clear()
            }
        }

        if (currentParagraph.isNotEmpty()) {
            paragraphs.add(currentParagraph.joinToString(". ") + ".")
        }

        return paragraphs.filter { it.length > 30 } // Filter very short paragraphs
    }

    private fun shouldStartNewParagraph(current: String, next: String?): Boolean {
        if (next == null) return true

        // Topic change indicators
        if (topicTransitions.any { next.trimStart().startsWith(it) }) return true

        // News segment change
        if (newsSegmentStarters.any { next.contains(it) }) return true

        // Speaker change detection
        val currentSpeaker = extractSpeaker(current)
        val nextSpeaker = extractSpeaker(next)
        if (currentSpeaker.isNotEmpty() && nextSpeaker.isNotEmpty() &&
            currentSpeaker != nextSpeaker) return true

        // Subject matter change (basic keyword clustering)
        if (hasSubjectChange(current, next)) return true

        // Time reference change
        if (hasTimeChange(current, next)) return true

        return false
    }

    private fun extractSpeaker(sentence: String): String {
        speechIndicators.forEach { indicator ->
            val index = sentence.indexOf(indicator)
            if (index > 0) {
                // Look for name before the speech indicator
                val beforeIndicator = sentence.substring(0, index).trim()
                val words = beforeIndicator.split(" ")
                if (words.size >= 2) {
                    return words.takeLast(2).joinToString(" ")
                }
            }
        }
        return ""
    }

    private fun hasSubjectChange(current: String, next: String): Boolean {
        val politicsKeywords = listOf("चुनाव", "सरकार", "विपक्ष", "नेता", "मंत्री", "संसद")
        val economyKeywords = listOf("बाजार", "सेंसेक्स", "निफ्टी", "अर्थव्यवस्था", "रुपया")
        val sportsKeywords = listOf("खेल", "मैच", "टीम", "क्रिकेट", "फुटबॉल")

        val currentSubject = when {
            politicsKeywords.any { current.contains(it) } -> "politics"
            economyKeywords.any { current.contains(it) } -> "economy"
            sportsKeywords.any { current.contains(it) } -> "sports"
            else -> "general"
        }

        val nextSubject = when {
            politicsKeywords.any { next.contains(it) } -> "politics"
            economyKeywords.any { next.contains(it) } -> "economy"
            sportsKeywords.any { next.contains(it) } -> "sports"
            else -> "general"
        }

        return currentSubject != "general" && nextSubject != "general" &&
                currentSubject != nextSubject
    }

    private fun hasTimeChange(current: String, next: String): Boolean {
        val timePattern = Regex("(आज|कल|परसों|\\d+\\s*(जुलाई|अगस्त|सितंबर|अक्टूबर|नवंबर|दिसंबर))")

        val currentTime = timePattern.find(current)?.value
        val nextTime = timePattern.find(next)?.value

        return currentTime != null && nextTime != null && currentTime != nextTime
    }
}