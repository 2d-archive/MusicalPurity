package cc.highpurity.discord.music

import cc.highpurity.discord.music.tools.Config
import cc.highpurity.discord.music.tools.ExternalFilePropertySource
import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.sources.EnvironmentVariablesPropertySource
import dev.minn.jda.ktx.jdabuilder.light
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.koin.dsl.module

val mainModule = module {
    single(createdAtStart = true) {
        light(get<Config>().bot.token) {
            setEnabledIntents(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS)
            setMemberCachePolicy(MemberCachePolicy.VOICE)
            addEventListeners(*getAll<EventListener>().toTypedArray())
        }
    }

    single {
        val loader = ConfigLoader {
            addSource(ExternalFilePropertySource("config.toml"))
            addSource(EnvironmentVariablesPropertySource(useUnderscoresAsSeparator = true, allowUppercaseNames = true))
        }

        loader.loadConfigOrThrow<Config>()
    }
}
