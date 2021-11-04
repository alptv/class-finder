package clazz.finder.matcher

class ClassName(fullClassName: String) : Comparable<ClassName> {
    val packageName: String
    val className: String

    init {
        val packageEnd = fullClassName.lastIndexOf('.') + 1
        className = fullClassName.substring(packageEnd)
        packageName = fullClassName.substring(0, packageEnd)
    }

    override fun toString(): String {
        return packageName + className
    }

    override fun compareTo(other: ClassName): Int {
        return className.compareTo(other.className)
    }

}