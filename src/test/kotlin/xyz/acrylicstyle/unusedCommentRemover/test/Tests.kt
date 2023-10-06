package xyz.acrylicstyle.unusedCommentRemover.test

import org.junit.jupiter.api.Test
import xyz.acrylicstyle.unusedCommentRemover.test.util.Util

class Tests {
    @Test
    fun testRecords() {
        Util.testRecord("/src/RecordClass.java", "/processed/RecordClass.java")
        Util.testRecord("/src/ObfRecordClass.java", "/processed/ObfRecordClass.java")
        Util.testRecord("/src/InnerRecordClass.java", "/processed/InnerRecordClass.java")
    }
}
