package org.darkjaguar.djbotdiscord.utils

import org.darkjaguar.djbotdiscord.logging.logger
import sx.blah.discord.handle.impl.events.guild.channel.ChannelEvent
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.RequestBuffer

fun ChannelEvent.sendMessage(message: () -> String) {
    channel.sendMessage(message)
}

fun IChannel.sendMessage(message: () -> String) {
    RequestBuffer.request {
        try {
            sendMessage(message())
        } catch (e: DiscordException) {
            logger.error(e) { "Error sending message: $message" }
        }
    }
}

fun MessageEvent.deleteMessage() {
    try {
        message.delete()
    } catch (e: DiscordException) {
        logger.error(e) { "Error deleting message inside channel: ${channel.name}" }
    }
}