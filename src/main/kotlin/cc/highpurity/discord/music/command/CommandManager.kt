package cc.highpurity.discord.music.command

import cc.highpurity.discord.music.command.data.ChatInputCommandDeclaration
import cc.highpurity.discord.music.command.data.CommandDeclarationHolder
import cc.highpurity.discord.music.command.data.CommandExecutor
import cc.highpurity.discord.music.command.error.UnknownCommandException
import cc.highpurity.discord.music.tools.Constants
import cc.highpurity.discord.music.tools.ext.get
import cc.highpurity.discord.music.tools.ext.getAll
import cc.highpurity.discord.music.tools.ext.into
import cc.highpurity.discord.music.tools.ext.pluralized
import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.interactions.commands.updateCommands
import dev.minn.jda.ktx.messages.Embed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class CommandManager : ListenerAdapter() {
    companion object {
        val log = KotlinLogging.logger { }
    }

    val commands: List<CommandDeclarationHolder> by lazy {
        getAll()
    }

    val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()) {
        thread(name = "Command Executor-Thread ${"??"}", start = false) { it.run() }
    }

    val scope = CoroutineScope(executor.asCoroutineDispatcher() + SupervisorJob())

    suspend fun sync(guildId: Long? = null) {
        if (guildId != null) {
            val guild = get<JDA>().getGuildById(guildId)
                ?: error("Couldn't find guild w/ id $guildId")

            syncGuildCommands(guild)
        } else {
            syncGlobalCommands()
        }
    }

    suspend fun syncGuildCommands(guild: Guild) {
        /* get all existing commands in this guild. */
        val e = guild.retrieveCommands().await()

        /* filter out commands we want to create/update */
        val creating = commands.filterNot { c -> e.any { it.name == c.declaration.name } }
        val updating = commands.filter { c -> e.any { it.name == c.declaration.name && it != c.declaration.toData() } }
        val deleting = e.filter { commands.none { c -> c.declaration.name == it.name } }

        /* make sure there's nothing to do to avoid a needless api request */
        val upserting = creating + updating
        if (upserting.isEmpty() && deleting.isEmpty()) {
            log.info { "* nothing to do in guild '${guild.name}' (${guild.id})" }
            return
        }

        /* update commands in the guild. */
        log.info { "* upserting ${"command".pluralized(upserting.size)} in '${guild.name}' (${guild.id})" }
        val action = guild.updateCommands {
            for (command in upserting) {
                val chatInputCommand: ChatInputCommandDeclaration = command.declaration.into()
                addCommands(chatInputCommand.toData())
            }
        }

        action.await()
        log.info { "* done syncing commands in '${guild.name}' (${guild.id})" }
    }

    suspend fun syncGlobalCommands() {
        val jda = get<JDA>()

        /* get all globally existing commands */
        val e = jda
            .retrieveCommands()
            .await()

        /* filter out commands we want to create and update. */
        val creating = commands.filterNot { c -> e.any { it.name == c.declaration.name } }
        val updating = commands.filter { c -> e.any { it.name == c.declaration.name && it != c.declaration.toData() } }
        val deleting = e.filter { commands.none { c -> c.declaration.name == it.name } }

        /* upsert commands */
        val upserting = creating + updating
        if (upserting.isEmpty() && deleting.isEmpty()) {
            log.info { "* nothing to do globally." }
            return
        }

        log.info { "* upserting ${"command".pluralized(upserting.size)}" }

        val action = jda.updateCommands {
            for (command in upserting) {
                val chatInputCommand: ChatInputCommandDeclaration = command.declaration.into()
                addCommands(chatInputCommand.toData())
            }
        }

        action.await()
        log.info { "* done syncing global commands." }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val executor: CommandExecutor
        try {
            val command = commands.find { it.declaration.name == event.name }
                ?: throw UnknownCommandException()

            if (!event.commandPath.contains('/')) {
                /* single command */
                log.trace { "single command: ${event.commandString}" }
                executor = command.into()
            } else {
                require(command !is CommandExecutor) {
                    "A command with subcommands/subcommand groups cannot have an executor."
                }

                /* get the root command declaration. */
                val declaration = command.declaration.into<ChatInputCommandDeclaration>()

                /* figure out whether it is a subcommand or a nested subcommand */
                val parts = event.commandPath.split('/')
                executor = if (parts.size == 2) {
                    /* subcommand */
                    log.trace { "sub command: ${event.commandString}" }
                    declaration.subcommands.find { it.declaration.name == parts[1] } ?: throw UnknownCommandException()
                } else {
                    /* subcommand group */
                    val group = declaration.subcommandGroups.find { it.name == parts[1] }
                        ?: throw UnknownCommandException()

                    log.trace { "sub command in group: ${event.commandString}" }
                    group.subcommands.find { it.declaration.name == parts[2] } ?: throw UnknownCommandException()
                }
            }
        } catch (ex: UnknownCommandException) {
            log.error(ex) { "Couldn't find executor for command: ${event.commandString} " }

            event
                .replyEmbeds(Embed {
                    color = Constants.COLOR
                    description = "Couldn't find an executor for this command, try spam pinging @Highpurity about it..."
                })
                .setEphemeral(true)
                .queue()

            return
        } catch (ex: Exception) {
            log.error(ex) { "An error while processing command: ${event.commandString} " }
            return
        }

        scope.launch {
            try {
                executor.execute(event)
            } catch (ex: Exception) {
                log.error(ex) { "An error while executing command: ${event.commandString} " }
            }
        }
    }
}
