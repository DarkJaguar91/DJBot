package org.darkjaguar.djbot.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.*


class TrackScheduler(private val player: AudioPlayer) {
    private val queue: MutableList<AudioTrack> = Collections.synchronizedList(LinkedList())

    init {
        // For encapsulation, keep the listener anonymous.
        player.addListener(object : AudioEventAdapter() {
            override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
                // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
                if (endReason.mayStartNext) {
                    nextTrack()
                }
            }
        })
    }

    @Synchronized
    fun queue(track: AudioTrack): Boolean {
        val playing = player.startTrack(track, true)

        if (!playing) {
            queue.add(track)
        }

        return playing
    }

    @Synchronized
    fun nextTrack(): AudioTrack? {
        val currentTrack = player.playingTrack
        val nextTrack = if (queue.isEmpty()) null else queue.removeAt(0)

        player.startTrack(nextTrack, false)
        return currentTrack
    }

    fun getQueue(): List<AudioTrack> {
        return this.queue
    }
}
