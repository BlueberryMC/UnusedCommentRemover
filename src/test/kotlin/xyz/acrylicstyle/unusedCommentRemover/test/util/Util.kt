package xyz.acrylicstyle.unusedCommentRemover.test.util

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
}
