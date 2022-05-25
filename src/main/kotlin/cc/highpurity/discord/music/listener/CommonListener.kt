package cc.highpurity.discord.music.listener

import mu.KotlinLogging
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommonListener : ListenerAdapter() {
    companion object {
        val log = KotlinLogging.logger { }
    }
}
