package cc.highpurity.discord.music.tools

data class Config(val bot: Bot, val audio: Audio = Audio()) {
    data class Bot(val token: String)

    data class Audio(val spotify: Spotify = Spotify(), val youtube: YouTube = YouTube()) {
        data class Spotify(
            val clientId: String? = null,
            val clientSecret: String? = null
        )

        data class YouTube(
            val ipBlocks: List<String> = emptyList(),
            val psid: String? = null,
            val papisid: String? = null
        )
    }
}
