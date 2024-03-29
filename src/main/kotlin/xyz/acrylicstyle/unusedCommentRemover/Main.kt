@file:JvmName("Main")
package xyz.acrylicstyle.unusedCommentRemover

import xyz.acrylicstyle.unusedCommentRemover.util.OS
import java.io.File
import java.util.regex.Pattern
import kotlin.system.exitProcess

val utf8 = charset("UTF-8")

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
    .replace("(Map)")
    .replace("(BiPredicate)")
    .replace("(BiFunction)")

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
    .replace("new NearestAttackableTargetGoal(", "new NearestAttackableTargetGoal<>(")

fun String.convertCharacters() = this
    .replace("\ufffd\uff7f\uff7d", "\\ufffd") // 65533 // StringDecomposer.java
    .replace("\uff82\uff67", "\\u00a7") // 167 // StringUtil.java, ChatFormatting.java, and many more
    .replace("\u95e2\ufffd", "\\u84c0") // 33984 // EffectInstance.java, GlStateManager.java, and ShaderInstance.java
    .replace("\u7b18\ufffd", "\\u2603") // 9731 // DirectoryLock.java
    .replace("\ufffd\u6cca", "\\ud511") // 54545 // DualNoiseProvider.java
    .replace("\u962e\uff7a", "\\u85ba") // 34234 // DualNoiseProvider.java
    .replace("return i2 & 255 | (i & 32767) << 8 | (i3 & 32767) << 24 | (i < 0 ? Integer.MIN_VALUE : 0) | (i3 < 0 ? '\u95a0\ufffd' : 0);", "return i2 & 255 | (i & 32767) << 8 | (i3 & 32767) << 24 | (i < 0 ? Integer.MIN_VALUE : 0) | (i3 < 0 ? '\\u8000' : 0);") // Node.java
    .replace("return \"max\".equals(s2) ? '\u95a0\ufffd' : '\u95a0\ufffd';", "return \"max\".equals(s2) ? '\\u8008' : '\\u8006';") // 32776, 32774
    // Fix inconsistency between platforms (e.g. Windows vs. Linux)
    // For some reason, Windows does "\\u00a7" but Linux (tested on ubuntu 20.04) does "\u00a7"
    .replace("\u00a7", "\\u00a7") // 167
    .replace("\ufffd", "\\ufffd") // 65533
    .replace("\u2603", "\\u2603") // 9731 // DirectoryLock.java

val recordRegex = "(.*?)(?:final class|record) (.*?)(?: extends Record|\\(.+?\\))(.*?)".toRegex()
val fieldRegex = "(?!.*=.*)\\s*(private\\s+)?final (.*?) (.*?);".toRegex()
val uselessMethodRegex = "\\s*public final (String|int|boolean) (equals|hashCode|toString)\\((Object .+?)?\\) \\{\\R\\s*return this\\.(equals|hashCode|toString)<invokedynamic>\\(this(, object)?\\);\\R\\s*}".toRegex()

fun String.convertRecord(): String {
    if (this.lines().all { !recordRegex.matches(it) }) return this
    // class name: fields (field type: field name)
    val fields = mutableMapOf<String, MutableList<String>>()
    val names = mutableSetOf<String>()
    this.lines().forEach { s ->
        if (recordRegex.matches(s)) {
            recordRegex.find(s)?.groupValues?.get(2)?.let { names.add(it) } ?: System.err.println("Failed to find class name from line: $s")
            fields[names.last()] = mutableListOf()
        }
        if (!s.contains("=") && fieldRegex.matches(s) && names.isNotEmpty()) {
            fields[names.last()]!!.add(s.replace(fieldRegex, "$2 $3"))
        }
    }
    var s = this.replace(uselessMethodRegex)
    // transform class into record (final class $cn extends Record -> record $cn) and remove constructor
    names.forEach { cn ->
        val ctorParams = fields[cn]!!.joinToString(" .+?, ") { Pattern.quote(it.split("\\s+".toRegex())[0]) } + " .+?"
        s = if (s.contains("(public )?(static )?record $cn\\(".toRegex())) {
            s.replace("(\\s*)(public )?(?:static )?record $cn\\($ctorParams\\)".toRegex(), "$1$2record $cn(${fields[cn]!!.joinToString(", ")})")
                .replace("\\s*((public|private) )?$cn\\($ctorParams\\)\\s?\\{\n\\s*this[\\s\\S]+?}".toRegex())
        } else {
            s.replace("\\s*((public|private) )?$cn\\($ctorParams\\)\\s?\\{[\\s\\S]+?}".toRegex())
                .replace("final class $cn extends Record", "record $cn(${fields[cn]!!.joinToString(", ")})")
        }
    }
    fields.values.forEach {
        it.forEach { field ->
            s = s.replace("\\s*public ${Pattern.quote(field)}\\(\\)\\s?\\{[\\s\\S]+?}".toRegex())
                .replace("\\s*(private )?final ${Pattern.quote(field)};".toRegex())
        }
    }
    return s
}

fun String.replace(text: String) = this.replace(text, "")
fun String.replace(regex: Regex) = this.replace(regex, "")

fun File.write(lines: List<String>) {
    try {
        val text = lines.joinToString("\r\n")
            .convertCasts()
            .convertCharacters()
            .doType()
            .convertRecord()
            .lines()
            .joinToString("\r\n") // make sure line terminators are \r\n (CRLF)
        this.writeText(text, utf8)
    } catch (e: Exception) {
        System.err.println("Failed to process $path")
        e.printStackTrace()
    }
}
