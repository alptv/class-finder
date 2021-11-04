package clazz.finder

import clazz.finder.matcher.ClassMatcher
import clazz.finder.matcher.ClassName
import java.io.File


fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage <classes file> <pattern>")
    } else {
        val fileName = args[0]
        val pattern = args[1]
        val matcher = ClassMatcher(pattern)
        try {
            File(fileName).readLines().map { ClassName(it) }.filter { matcher.match(it.className) }.sorted().forEach { println(it) }
        } catch (e: Exception) {
            println("Error during reading file : $fileName")
            e.printStackTrace()
        }
    }

}