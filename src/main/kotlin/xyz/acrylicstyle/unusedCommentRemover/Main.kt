@file:JvmName("Main")
package xyz.acrylicstyle.unusedCommentRemover

import xyz.acrylicstyle.unusedCommentRemover.util.OS
import java.io.File
import kotlin.system.exitProcess

val utf8 = charset("UTF-8")

// removes useless '(Object)' cast which is incorrect most of the time
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: <target directory>")
        exitProcess(1)
    }
    val os = OS.detectOS()
    val charset = if (os == OS.WINDOWS) charset("SJIS") else charset("UTF-8")
    File(args[0]).walkTopDown().forEach { file ->
        if (file.isDirectory) return@forEach
        if (file.name.endsWith(".mcmeta") || file.name.endsWith(".json")) {
            val lines = file.readLines(charset) as MutableList<String>
            file.writeText(lines.joinToString("\r\n"), utf8)
        }
        if (!file.name.endsWith(".java")) return@forEach
        val lines = file.readLines(charset) as MutableList<String>
        file.write(lines)
    }
}

fun String.postProcessRemoveCast() = this
    .replace("(Object)")
    .replace("(Object[])")
    .replace("(List)")
    .replace("(Consumer)")
    .replace("(Function)")
    .replace("(Optional)")

fun String.convertCharacters() = this
    .replace("\u95e2\ufffd", "\\u84c0")
    .replace("return \"max\".equals(s2) ? '\u95a0\ufffd' : '\u95a0\ufffd';", "return \"max\".equals(s2) ? '\\u8008' : '\\u8006';")

fun String.replace(text: String) = this.replace(text, "")

fun File.write(lines: List<String>) {
    val text = lines.joinToString("\r\n")
        .postProcessRemoveCast()
        .convertCharacters()
    this.writeText(text, utf8)
}
