package xyz.acrylicstyle.unusedCommentRemover.test

import org.junit.jupiter.api.Test
import xyz.acrylicstyle.unusedCommentRemover.convertRecord
import xyz.acrylicstyle.unusedCommentRemover.test.util.Util

class Tests {
    @Test
    fun testRecord() {
        val src = Util.getResourceAsString("/src/RecordClass.java")
            ?: error("/src/RecordClass.java is missing")
        val expected = Util.getResourceAsString("/processed/RecordClass.java")
            ?: error("/processed/RecordClass.java is missing")
        assert(src.convertRecord().lines().joinToString("\r\n") == expected.lines().joinToString("\r\n")) {
            "Expected:\n$expected\n\nActual:\n${src.convertRecord()}"
        }
    }
}
