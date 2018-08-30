package org.darkjaguar.djbot.utils

import org.darkjaguar.djbot.logging.logger
import sx.blah.discord.handle.impl.events.guild.channel.ChannelEvent
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.RequestBuffer

fun ChannelEvent.sendMessage(message: String) {
    RequestBuffer.request {
        try {
            channel.sendMessage(message)
        } catch (e: DiscordException) {
            logger.error("Error sending message", e)
        }
    }
}