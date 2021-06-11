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

fun String.convertCasts() = this
    .replace("(Object)")
    .replace("(Object[])")
    .replace("(List)")
    .replace("(Consumer)")
    .replace("(Function)")
    .replace("(Optional)")
    .replace("(Set)")
    .replace("(Supplier)")
    .replace("(DataResult)")
    .replace("(Codec)")
    .replace("(CompletableFuture)")
    .replace("(Long2ObjectLinkedOpenHashMap)")
    .replace("(Collection)")
    .replace("(Pair)")
    .replace("(Dynamic)")
    .replace("(Typed)")
    .replace("(BiConsumer)")
    .replace("(Predicate)")

fun String.doType() = this
    .replace("new BabyFollowAdult(", "new BabyFollowAdult<>(")
    .replace("new BackUpIfTooClose(", "new BackUpIfTooClose<>(")
    .replace("new CrossbowAttack()", "new CrossbowAttack<>()")
    .replace("new DismountOrSkipMounting(", "new DismountOrSkipMounting<>(")
    .replace("new EraseMemoryIf(", "new EraseMemoryIf<>(")
    .replace("new GoToCelebrateLocation(", "new GoToCelebrateLocation<>(")
    .replace("new GoToWantedItem(", "new GoToWantedItem<>(")
    .replace("new LongJumpToRandomPos(", "new LongJumpToRandomPos<>(")
    .replace("new Mount(", "new Mount<>(")
    .replace("new PrepareRamNearestTarget(", "new PrepareRamNearestTarget<>(")
    .replace("new RamTarget(", "new RamTarget<>(")
    .replace("new RememberIfHoglinWasKilled()", "new RememberIfHoglinWasKilled<>()")
    .replace("new RunOne(", "new RunOne<>(")
    .replace("new RunIf(", "new RunIf<>(")
    .replace("new StopHoldingItemIfNoLongerAdmiring()", "new StopHoldingItemIfNoLongerAdmiring<>()")
    .replace("new StopAttackingIfTargetInvalid(", "new StopAttackingIfTargetInvalid<>(")
    .replace("new StopBeingAngryIfTargetDead()", "new StopBeingAngryIfTargetDead<>()")
    .replace("new StopAdmiringIfTiredOfTryingToReachItem(", "new StopAdmiringIfTiredOfTryingToReachItem<>(")
    .replace("new StartAttacking(", "new StartAttacking<>(")
    .replace("new StartAdmiringItemIfSeen(", "new StartAdmiringItemIfSeen<>(")
    .replace("new StartHuntingHoglin()", "new StartHuntingHoglin<>()")

fun String.convertCharacters() = this
    .replace("\uff82\uff67", "\\u00a7")
    .replace("\u95e2\ufffd", "\\u84c0")
    .replace("return \"max\".equals(s2) ? '\u95a0\ufffd' : '\u95a0\ufffd';", "return \"max\".equals(s2) ? '\\u8008' : '\\u8006';")
    // Fix inconsistency between platforms (e.g. Windows vs. Linux)
    // For some reason, Windows does "\\u00a7" but Linux (tested on ubuntu 20.04) does "\u00a7"
    .replace("\u00a7", "\\u00a7")

fun String.replace(text: String) = this.replace(text, "")

fun File.write(lines: List<String>) {
    val text = lines.joinToString("\r\n")
        .convertCasts()
        .convertCharacters()
        .doType()
    this.writeText(text, utf8)
}
