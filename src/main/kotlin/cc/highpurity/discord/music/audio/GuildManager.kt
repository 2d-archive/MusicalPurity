package cc.highpurity.discord.music.audio

import cc.highpurity.discord.music.tools.ext.get
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.interactions.commands.CommandInteraction

class GuildManager(val guildId: Long) : AudioEventAdapter() {

    val player by lazy {
        get<AudioPlayerManager>().createPlayer()
    }

    /**
     * The [TextChannel] assigned to this [GuildManager]
     */
    var textChannelId: Long? = null

    val textChannel: TextChannel?
        get() = textChannelId?.let { get<JDA>().getTextChannelById(it) }

    /**
     * The [VoiceChannel] assigned to this [GuildManager]
     */
    var voiceChannelId: Long? = null

    val voiceChannel: VoiceChannel?
        get() = voiceChannelId?.let { get<JDA>().getVoiceChannelById(it) }

    fun connect(interaction: CommandInteraction) {
        val vc = interaction.member?.voiceState?.channel
            ?: error("Interaction member is not in voice channel.")


    }

    override fun onTrackStart(player: AudioPlayer?, track: AudioTrack?) {
        super.onTrackStart(player, track)
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        super.onTrackEnd(player, track, endReason)
    }

    override fun onPlayerPause(player: AudioPlayer?) {
        super.onPlayerPause(player)
    }

    override fun onPlayerResume(player: AudioPlayer?) {
        super.onPlayerResume(player)
    }

}
