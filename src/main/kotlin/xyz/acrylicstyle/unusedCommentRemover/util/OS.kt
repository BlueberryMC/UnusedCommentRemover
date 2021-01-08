package xyz.acrylicstyle.unusedCommentRemover.util

enum class OS {
    LINUX, MAC, WINDOWS, UNKNOWN;

    companion object {
        fun detectOS(): OS {
            val os = System.getProperty("os.name") ?: return UNKNOWN
            if (os == "Linux") return LINUX
            if (os == "Mac OS") return MAC
            if (os == "Mac OS X") return MAC
            if (os.contains("Windows")) return WINDOWS
            if (os == "Solaris") return LINUX
            return if (os == "FreeBSD") LINUX else UNKNOWN
        }
    }
}