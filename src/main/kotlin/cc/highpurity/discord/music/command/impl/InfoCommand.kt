package cc.highpurity.discord.music.command.impl

import cc.highpurity.discord.music.command.data.ChatInputCommand
import cc.highpurity.discord.music.command.data.command
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction

class InfoCommand : ChatInputCommand {
    override val declaration = command("info", "Shows info on the bot")

    override suspend fun execute(interaction: SlashCommandInteraction) {
        interaction.reply("poop").queue()
    }
}
