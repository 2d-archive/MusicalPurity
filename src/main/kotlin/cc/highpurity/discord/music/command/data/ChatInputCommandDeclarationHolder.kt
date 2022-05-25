package cc.highpurity.discord.music.command.data

interface ChatInputCommandDeclarationHolder : CommandDeclarationHolder {
    override val declaration: ChatInputCommandDeclaration
}
