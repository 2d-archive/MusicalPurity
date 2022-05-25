package cc.highpurity.discord.music.audio

import cc.highpurity.discord.music.tools.Config
import cc.highpurity.discord.music.tools.ext.get
import com.github.topislavalinkplugins.topissourcemanagers.applemusic.AppleMusicSourceManager
import com.github.topislavalinkplugins.topissourcemanagers.spotify.SpotifyConfig
import com.github.topislavalinkplugins.topissourcemanagers.spotify.SpotifySourceManager
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers

class PurifiedPlayerManager : DefaultAudioPlayerManager() {
    companion object {
        val DEFAULT_EXTERNAL_PROVIDERS = arrayOf(
            "ytsearch:%QUERY%",
            "ytsearch:%ISRC%",
            "scsearch:%QUERY%",
        )
    }

    init {
        configuration.apply {
            isFilterHotSwapEnabled = true
            resamplingQuality = AudioConfiguration.ResamplingQuality.HIGH
        }

        registerSourceManagers()
    }

    private fun registerSourceManagers() {
        val config = get<Config>()

        /* register spotify source manager */
        if (!config.audio.spotify.clientId.isNullOrEmpty() && !config.audio.spotify.clientSecret.isNullOrBlank()) {
            val spotifyConfig = SpotifyConfig()
            spotifyConfig.clientId = config.audio.spotify.clientId
            spotifyConfig.clientSecret = config.audio.spotify.clientSecret
            spotifyConfig.setCountryCode("us")

            registerSourceManager(SpotifySourceManager(DEFAULT_EXTERNAL_PROVIDERS, spotifyConfig, this))
        }

        /* register apple source manager */
        registerSourceManager(AppleMusicSourceManager(DEFAULT_EXTERNAL_PROVIDERS,  "us", this))

        /* register everything else */
        AudioSourceManagers.registerRemoteSources(this)
    }
}
