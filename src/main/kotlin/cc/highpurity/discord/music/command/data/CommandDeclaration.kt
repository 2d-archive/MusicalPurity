package cc.highpurity.discord.music.command.data

import net.dv8tion.jda.api.interactions.commands.build.CommandData

abstract class CommandDeclaration(
    val name: String,
    val description: String
) {
    abstract fun toData(): CommandData
}
