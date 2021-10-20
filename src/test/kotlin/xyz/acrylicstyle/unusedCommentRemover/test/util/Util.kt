package xyz.acrylicstyle.unusedCommentRemover.test.util

import xyz.acrylicstyle.unusedCommentRemover.convertRecord
import xyz.acrylicstyle.unusedCommentRemover.test.Tests
import java.util.stream.Collectors

object Util {
    fun getResourceAsString(path: String) =
        Tests::class.java
            .getResourceAsStream(path)
            ?.bufferedReader()
            ?.lines()
            ?.collect(Collectors.toList())
            ?.joinToString("\n")

    fun testRecord(srcPath: String, expectedPath: String) {
        val src = getResourceAsString(srcPath)
            ?: error("$srcPath is missing")
        val expected = getResourceAsString(expectedPath)
            ?: error("$expectedPath is missing")
        assert(src.convertRecord().lines().joinToString("\r\n") == expected.lines().joinToString("\r\n")) {
            "Expected:\n$expected\n\nActual:\n${src.convertRecord()}"
        }
    }
}
