package cc.highpurity.discord.music.command

import cc.highpurity.discord.music.command.data.CommandDeclarationHolder
import cc.highpurity.discord.music.command.impl.InfoCommand
import net.dv8tion.jda.api.hooks.EventListener
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.single

val commandModule = module {
    single { CommandManager() } bind EventListener::class

    command<InfoCommand>()
}

@OptIn(KoinReflectAPI::class)
inline fun <reified T : CommandDeclarationHolder> Module.command(): Pair<Module, InstanceFactory<*>> {
    return single<T>() bind CommandDeclarationHolder::class
}
