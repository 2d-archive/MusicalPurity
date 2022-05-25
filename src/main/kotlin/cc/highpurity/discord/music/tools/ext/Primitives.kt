package cc.highpurity.discord.music.tools.ext

fun String.pluralized(number: Int, showNumber: Boolean = true, prefix: String = "", suffix: String = "s"): String {
    return (if (showNumber) "$number " else "") + if (number == 1) this else "$prefix$this$suffix"
}
