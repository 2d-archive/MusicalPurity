package cc.highpurity.discord.music.command.data

interface ChatInputSubcommand : CommandExecutor {
    val declaration: ChatInputSubcommandDeclaration
}
