package com.module.notelycompose.transcription.textAnalysis

class GujaratiTextSegmenter: TextSegmenter {

    // 1. Sentence ending patterns (verb forms that typically end sentences)
    private val sentenceEndingPatterns = listOf(
        // Present tense endings
        "છે", "છો", "છું", "છીએ",
        // Past tense endings
        "હતો", "હતા", "હતી", "હતાં",
        // Future tense endings
        "હશે", "હશો", "હશું", "હશીએ",
        // Perfect tense endings
        "ગયો", "ગયા", "ગઈ", "ગયાં",
        "દીધો", "દીધા", "દીધી", "દીધાં",
        "લીધો", "લીધા", "લીધી", "લીધાં",
        "કર્યો", "કર્યા", "કરી", "કર્યાં",
        "થયો", "થયા", "થઈ", "થયાં",
        // Participle endings
        "કરીને", "કરવા", "આવીને"
    )

    // 2. Topic transition indicators
    private val topicTransitions = listOf(
        "હવે", "પછી", "એ પછી", "ત્યાર પછી", "ત્યાં", "બીજી બાજુ",
        "આ બાજુ", "ત્યા બાજુ", "અહીં", "ત્યાં", "એ દરમિયાન", "એ વખતે",
        "બીજા સમાચાર", "આગળના સમાચાર", "હવે વાત", "આ મામલામાં",
        "આ પર", "આમાંથી", "પરંતુ", "પણ", "તેમ છતાં"
    )

    // 3. News segment starters
    private val newsSegmentStarters = listOf(
        "જુઓ", "જાણો", "સમજો", "સાંભળો", "પહેલા", "બીજા", "ત્રીજા",
        "મુખ્ય", "પ્રમુખ", "મોટા", "મહત્વના", "મહત્વપૂર્ણ", "ખાસ", "વિશેષ",
        "રિપોર્ટ", "સમાચાર", "ન્યૂઝ", "ખબર", "માહિતી", "સૂત્ર", "મામલો"
    )

    // 4. Question indicators
    private val questionWords = listOf(
        "શું", "ક્યારે", "ક્યાં", "કેવી રીતે", "કેમ", "કોણ", "કોને", "કોનું", "કેટલું", "કેટલા"
    )

    // 5. Quote/speech indicators
    private val speechIndicators = listOf(
        "કહ્યું", "જણાવ્યું", "જાહેરાત કરી", "માહિતી આપી", "સ્પષ્ટ કર્યું",
        "આક્ષેપ કર્યો", "દાવો કર્યો", "માગ કરી", "સૂચના આપી"
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
        if (before.endsWith("અને") || before.endsWith("કે") ||
            after.trimStart().startsWith("અને") || after.trimStart().startsWith("કે")) {
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
        val politicsKeywords = listOf("ચૂંટણી", "સરકાર", "વિપક્ષ", "નેતા", "મંત્રી", "વિધાનસભા")
        val economyKeywords = listOf("બજાર", "સેન્સેક્સ", "નિફ્ટી", "અર્થતંત્ર", "રૂપિયો")
        val sportsKeywords = listOf("રમત", "મેચ", "ટીમ", "ક્રિકેટ", "ફૂટબોલ")

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
        val timePattern = Regex("(આજે|કાલે|પરબા|\\d+\\s*(જુલાઈ|ઓગસ્ટ|સપ્ટેમ્બર|ઓક્ટોબર|નવેમ્બર|ડિસેમ્બર))")

        val currentTime = timePattern.find(current)?.value
        val nextTime = timePattern.find(next)?.value

        return currentTime != null && nextTime != null && currentTime != nextTime
    }
}