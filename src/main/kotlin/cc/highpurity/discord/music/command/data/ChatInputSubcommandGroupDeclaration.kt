package cc.highpurity.discord.music.command.data

import dev.minn.jda.ktx.interactions.commands.SubcommandGroup
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData

data class ChatInputSubcommandGroupDeclaration(
    val name: String,
    val description: String,
    val subcommands: List<ChatInputSubcommand>
) {
    fun toData(): SubcommandGroupData {
        val declaration = this
        return SubcommandGroup(name, description) {
            addSubcommands(declaration.subcommands.map { it.declaration.toData() })
        }
    }

    class Builder(var name: String, var description: String) {
        val subcommands: MutableList<ChatInputSubcommand> = mutableListOf()

        fun subcommand(command: ChatInputSubcommand) {
            subcommands.add(command)
        }

        fun build(): ChatInputSubcommandGroupDeclaration {
            require(subcommands.isNotEmpty()) {
                "Subcommand group $name must have at least one subcommand"
            }

            return ChatInputSubcommandGroupDeclaration(name, description, subcommands)
        }
    }
}
