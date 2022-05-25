package cc.highpurity.discord.music

import cc.highpurity.discord.music.command.CommandManager
import cc.highpurity.discord.music.command.commandModule
import cc.highpurity.discord.music.listener.listenerModule
import cc.highpurity.discord.music.tools.ext.get
import dev.minn.jda.ktx.events.listener
import mu.KotlinLogging
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.ReadyEvent
import org.koin.core.context.startKoin

private val log = KotlinLogging.logger { }

suspend fun main() {
    Thread.currentThread().name = "Main Thread"

    log.info { "* initializing..." }

    /* initialize koin */
    startKoin {
        modules(commandModule, listenerModule, mainModule)
    }

    /* syncing commands */
    val jda = get<JDA>()
    jda.listener<ReadyEvent> {
        log.info { "logged in as ${jda.selfUser.asTag}" }

        /* sync commands */
        get<CommandManager>().sync(830268307948175380L)
    }
}
