package clazz.finder.matcher

import clazz.finder.matcher.view.StringView
import java.lang.Integer.max

class ClassMatcher(pattern: String) : Matcher {
    private val pattern = StringView(pattern)
    private val caseInsensitive = !pattern.any { it.isUpperCase() }
    private val shouldEndsMatch = pattern.endsWith(" ")


    override fun match(className: String): Boolean {
        val classNameView = prepareClassName(className, shouldEndsMatch)

        var firstMatchedChar = pattern.length
        val camelCaseChunks = classNameView.splitByCondition { it.isUpperCase() }

        for (camelCaseChunk in camelCaseChunks) {
            firstMatchedChar = tryMatchChunk(camelCaseChunk, firstMatchedChar)
        }
        return firstMatchedChar == 0
    }


    private fun prepareClassName(className: String, shouldEndsMatch: Boolean): StringView {
        return StringView(with(className) { substring(lastIndexOf('.') + 1) }
                + if (shouldEndsMatch) " " else "")
    }


    private fun tryMatchChunk(camelCaseChunk: StringView, firstMatchedChar: Int): Int {
        val minUnmatchedChar = max(0, firstMatchedChar - camelCaseChunk.length)

        for (possibleNewFirstMatchedChar in minUnmatchedChar until firstMatchedChar) {
            val patternPart = pattern.subview(possibleNewFirstMatchedChar, firstMatchedChar)
            if (tryMatchChunkToPatternPart(camelCaseChunk, patternPart)) {
                return possibleNewFirstMatchedChar
            }
        }
        return firstMatchedChar
    }

    private fun tryMatchChunkToPatternPart(camelCaseChunk: StringView, patternPart: StringView): Boolean {
        var camelCaseChunkIndex = 0
        var firstCharMatched = patternPart[0] == '*'

        for (chunk in patternPart.splitByCondition { it == '*' }.reversed()) {
            val letters = if (chunk[0] == '*') {
                camelCaseChunkIndex++
                chunk.subview(1)
            } else {
                chunk
            }
            if (!letters.isEmpty()) {
                camelCaseChunkIndex = camelCaseChunk.find(letters, camelCaseChunkIndex, caseInsensitive)
                firstCharMatched = firstCharMatched || camelCaseChunkIndex == 0
                if (camelCaseChunkIndex == -1) {
                    return false
                }
                camelCaseChunkIndex += chunk.length
            }
        }
        return firstCharMatched
    }
}


