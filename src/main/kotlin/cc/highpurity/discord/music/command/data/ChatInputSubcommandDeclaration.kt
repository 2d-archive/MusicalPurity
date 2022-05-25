package cc.highpurity.discord.music.command.data

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

data class ChatInputSubcommandDeclaration(
    val name: String,
    val description: String,
) {
    // todo: register options
    fun toData(): SubcommandData = SubcommandData(name, description)
}
