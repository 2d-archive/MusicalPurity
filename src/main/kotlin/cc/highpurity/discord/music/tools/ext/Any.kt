package cc.highpurity.discord.music.tools.ext

inline fun <reified T> Any?.into(): T = this as T

inline fun <reified T> Any?.intoOrNull(): T? = this as? T
