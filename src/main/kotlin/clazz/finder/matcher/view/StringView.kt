package clazz.finder.matcher.view

class StringView private constructor(
    private val string: String,
    private val offset: Int,
    val length: Int = string.length
) {

    constructor(string: String) : this(string, 0, string.length)

    operator fun get(index: Int): Char {
        validateIndex(index)
        return string[offset + index]
    }

    fun isEmpty(): Boolean {
        return length == 0
    }

    fun subview(from: Int, to: Int): StringView {
        validateIndex(from)
        validateIndex(to)
        return StringView(string, offset + from, to - from)
    }

    fun subview(from: Int): StringView {
        return subview(from, length)
    }


    fun splitByCondition(condition: (Char) -> Boolean): List<StringView> {
        val chunks = mutableListOf<StringView>()
        var previousChunkEnd = length
        for (i in length - 1 downTo 0) {
            if (condition(get(i))) {
                chunks.add(subview(i, previousChunkEnd))
                previousChunkEnd = i
            }
        }
        if (previousChunkEnd != 0) {
            chunks.add(subview(0, previousChunkEnd))
        }
        return chunks
    }

    fun find(pattern: StringView, from: Int, caseInsensitive: Boolean): Int {
        for (i in from..length - pattern.length) {
            val possibleEntry = subview(i, i + pattern.length)
            if (possibleEntry.equalsWithCase(pattern, caseInsensitive)) {
                return i
            }
        }
        return -1
    }

    override fun toString(): String {
        return string.substring(offset, offset + length)
    }

    private fun equalsWithCase(other: StringView, caseInsensitive: Boolean) =
        length == other.length && (0 until length).all {
            if (caseInsensitive) {
                get(it).lowercase() == other[it].lowercase()
            } else {
                get(it) == other[it]
            }
        }

    private fun validateIndex(index: Int) {
        if (index < 0 || index > length) {
            throw IllegalArgumentException("Index $index not in range [0, $length)")
        }
    }
}