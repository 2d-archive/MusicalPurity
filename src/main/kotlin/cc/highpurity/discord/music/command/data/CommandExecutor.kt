package cc.highpurity.discord.music.command.data

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction

interface CommandExecutor {
    suspend fun execute(interaction: SlashCommandInteraction)
}
