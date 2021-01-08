@file:JvmName("Main")
package xyz.acrylicstyle.unusedCommentRemover

import xyz.acrylicstyle.unusedCommentRemover.util.OS
import java.io.File
import kotlin.system.exitProcess

val utf8 = charset("UTF-8")

// A simple application that removes unneeded comments from decompiler
// also removes useless '(Object)' cast which is incorrect most of the time
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
        // Procyon decompiler
        if (lines[0].startsWith("// ") && lines[1].startsWith("// Decompiled") && lines[2].startsWith("// ")) {
            println("${file.path}: Procyon Decompiler (${lines[1]})")
            // 4 lines long, so remove index 0 for 4 times
            lines.removeAt(0)
            lines.removeAt(0)
            lines.removeAt(0)
            lines.removeAt(0)
            file.write(lines)
            return@forEach
        }
        // CFR decompiler
        if (lines[0].startsWith("/*")) {
            val start = 0
            var end = -1
            var l = 0
            for (s in lines) {
                if (s.startsWith(" */") || s.endsWith("*/")) {
                    end = l
                    break
                }
                l++
            }
            println("${file.path}: CFR Decompiler (${lines[1]}, range: $start - $end)")
            if (end == -1) {
                System.err.println("WARNING: ${file.path} is corrupted.")
                return@forEach
            }
            for (i in start..end) {
                lines.removeAt(0)
            }
            file.write(lines)
        }
    }
}

fun File.write(lines: List<String>) {
    this.writeText(lines.joinToString("\r\n").replace("\\(Object\\)".toRegex(), ""), utf8)
}
