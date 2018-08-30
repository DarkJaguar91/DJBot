package org.darkjaguar.djbot.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener


class GuildMusicManager(manager: AudioPlayerManager) {
    val player: AudioPlayer = manager.createPlayer()
    val audioProvider = AudioProvider(player)
    val scheduler = TrackScheduler(player)

    fun addAudioListener(listener: AudioEventListener) {
        player.addListener(listener)
    }

    fun removeAudioListener(listener: AudioEventListener) {
        player.removeListener(listener)
    }
}