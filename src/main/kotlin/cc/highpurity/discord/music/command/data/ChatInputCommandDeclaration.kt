@file:OptIn(ExperimentalContracts::class)

package cc.highpurity.discord.music.command.data

import dev.minn.jda.ktx.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class ChatInputCommandDeclaration(
    name: String,
    description: String,
    val subcommands: List<ChatInputSubcommand>,
    val subcommandGroups: List<ChatInputSubcommandGroupDeclaration>,
) : CommandDeclaration(name, description) {
    override fun toData(): SlashCommandData {
        val declaration = this
        return Command(name, description) {
            when {
                declaration.subcommands.isNotEmpty() -> addSubcommands(declaration.subcommands.map { it.declaration.toData() })
                declaration.subcommandGroups.isNotEmpty() -> addSubcommandGroups(declaration.subcommandGroups.map { it.toData() })
            }
        }
    }

    class Builder(
        var name: String,
        var description: String,
    ) {
        val subcommands: MutableList<ChatInputSubcommand> = mutableListOf()

        val subcommandGroups: MutableList<ChatInputSubcommandGroupDeclaration> = mutableListOf()

        /**
         * Adds a subcommand to this root command.
         *
         * @param command The subcommand to add.
         */
        fun subcommand(command: ChatInputSubcommand) {
            subcommands.add(command)
        }

        /**
         * Creates a new subcommand group declaration
         *
         * @param name The name of the subcommand group
         * @param block The block to execute when building the subcommand group declaration
         */
        inline fun subcommandGroup(
            name: String,
            description: String,
            block: ChatInputSubcommandGroupDeclaration.Builder.() -> Unit,
        ): Builder {
            contract {
                callsInPlace(block, InvocationKind.EXACTLY_ONCE)
            }

            val group = ChatInputSubcommandGroupDeclaration.Builder(name, description).apply(block).build()

            subcommandGroups.add(group)
            return this
        }

        fun build(): ChatInputCommandDeclaration {
            return ChatInputCommandDeclaration(name, description, subcommands, subcommandGroups)
        }
    }
}
