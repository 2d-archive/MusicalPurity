package cc.highpurity.discord.music.tools.ext

import org.koin.core.Koin
import org.koin.core.context.GlobalContext

fun getKoin(): Koin =
    GlobalContext.get()

inline fun <reified T> get(): T =
    getKoin().get()

inline fun <reified T> getAll(): List<T> =
    getKoin().getAll()
