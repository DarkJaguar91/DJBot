package org.darkjaguar.djbot.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager


class DJGuildPlayerData(manager: AudioPlayerManager) {
    val player: AudioPlayer = manager.createPlayer()
    val audioProvider = DJAudioProvider(player)
    val scheduler = DJTrackScheduler(player)
}
