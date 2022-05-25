package cc.highpurity.discord.music.listener

import net.dv8tion.jda.api.hooks.EventListener
import org.koin.dsl.bind
import org.koin.dsl.module

val listenerModule = module {
    single { CommonListener() } bind EventListener::class
}
