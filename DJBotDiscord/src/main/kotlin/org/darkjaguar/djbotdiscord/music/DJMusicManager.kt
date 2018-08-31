package org.darkjaguar.djbot.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import org.darkjaguar.djbotdiscord.logging.logger
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild


class DJMusicManager {
    private val playerManager = DefaultAudioPlayerManager()
    private val musicManagers = HashMap<Long, GuildMusicManager>()

    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
        AudioSourceManagers.registerLocalSource(playerManager)

//        CommandEventManager.registerCommand(object : CommandEvent {
//            override val commands: String
//                get() = "join"
//
//            override fun onEvent(event: MessageReceivedEvent, args: List<String>) {
//                event.author.getVoiceStateForGuild(event.guild).channel?.join()
//                event.message.delete()
//            }
//        })
//        CommandEventManager.registerCommand(object : CommandEvent {
//            override val commands: String
//                get() = "leave"
//
//            override fun onEvent(event: MessageReceivedEvent, args: List<String>) {
//                event.client.ourUser.getVoiceStateForGuild(event.guild).channel?.let {
//                    val scheduler = getGuildAudioPlayer(event.guild).scheduler
//                    scheduler.getQueue().clear()
//                    scheduler.nextTrack()
//
//                    it.leave()
//                    event.message.delete()
//                }
//            }
//        })
//        CommandEventManager.registerCommand(object : CommandEvent {
//            override val commands: String
//                get() = "play"
//
//            override fun onEvent(event: MessageReceivedEvent, args: List<String>) {
//                event.client.ourUser.getVoiceStateForGuild(event.guild).channel?.let {
//                    loadAndPlay(event.channel, args.joinToString(""))
//                } // TODO send message that cant play song cus not in channel
//
//                event.message.delete()
//            }
//        })
//        CommandEventManager.registerCommand(object : CommandEvent {
//            override val commands: String
//                get() = "skip"
//
//            override fun onEvent(event: MessageReceivedEvent, args: List<String>) {
//                event.client.ourUser.getVoiceStateForGuild(event.guild).channel?.let {
//                    skipTrack(event.channel)
//                } // TODO send message that cant play song cus not in channel
//
//                event.message.delete()
//            }
//        })
//        CommandEventManager.registerCommand(object : CommandEvent {
//            override val commands: String
//                get() = "volume"
//
//            override fun onEvent(event: MessageReceivedEvent, args: List<String>) {
//                event.client.ourUser.getVoiceStateForGuild(event.guild).channel?.let {
//                    if (!args.isEmpty()) {
//                        args[0].toIntOrNull()?.let { volume ->
//                            getGuildAudioPlayer(event.guild).player.volume = volume.coerceIn(0, 100)
//                        } // TODO send message that volume must be a number
//                    } else {
//                        // TODO send message saying volume needs to be provided
//                    }
//                } // TODO send message that cant play song cus not in channel
//                event.message.addReaction(EmojiManager.getForAlias("thumbsup"))
//                event.message.delete(2000)
//            }
//        })
    }

    @Synchronized
    fun getGuildAudioPlayer(guild: IGuild): GuildMusicManager {
        val guildId = guild.longID
        var musicManager = musicManagers[guildId]

        if (musicManager == null) {
            musicManager = GuildMusicManager(playerManager)
            musicManagers[guildId] = musicManager
        }

        guild.audioManager.audioProvider = musicManager.audioProvider

        return musicManager
    }

    private fun loadAndPlay(channel: IChannel, trackUrl: String) {
        val musicManager = getGuildAudioPlayer(channel.guild)

        playerManager.loadItemOrdered(musicManager, trackUrl, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                //BotUtils.sendMessage(channel, "Adding to queue " + track.getInfo().title)
                logger.info { track.info.toString() }

                play(musicManager, track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                var firstTrack: AudioTrack? = playlist.selectedTrack

                if (firstTrack == null) {
                    firstTrack = playlist.tracks[0]
                }

                //BotUtils.sendMessage(channel, "Adding to queue " + firstTrack!!.getInfo().title + " (first track of playlist " + playlist.name + ")")

                play(musicManager, firstTrack)
            }

            override fun noMatches() {
                //BotUtils.sendMessage(channel, "Nothing found by $trackUrl")
            }

            override fun loadFailed(exception: FriendlyException) {
                //BotUtils.sendMessage(channel, "Could not play: " + exception.message)
            }
        })
    }

    private fun play(musicManager: GuildMusicManager, track: AudioTrack?) {
        musicManager.scheduler.queue(track!!)
    }

    private fun skipTrack(channel: IChannel) {
        val musicManager = getGuildAudioPlayer(channel.guild)
        musicManager.scheduler.nextTrack()

        //BotUtils.sendMessage(channel, "Skipped to next track.")
    }
}
