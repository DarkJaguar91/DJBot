package org.darkjaguar.djbotdiscord.utils

import org.darkjaguar.djbotdiscord.logging.logger
import sx.blah.discord.handle.impl.events.guild.channel.ChannelEvent
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.RequestBuffer

fun ChannelEvent.sendMessage(deleteDelay: Long = -1, message: () -> String) {
    channel.sendMessage(deleteDelay, message)
}

fun IChannel.sendMessage(deleteDelay: Long = -1, message: () -> String) {
    RequestBuffer.request {
        try {
            sendMessage(message()).also {
                if (deleteDelay > -1) {
                    it.deleteMessage(deleteDelay)
                }
            }
        } catch (e: DiscordException) {
            logger.error(e) { "Error sending message: $message" }
        }
    }
}

fun MessageEvent.deleteMessage(delay: Long = 0) {
    message.deleteMessage(delay)
}

fun IMessage.deleteMessage(delay: Long = 0) {
    RequestBuffer.request {
        try {
            if (delay > 0) {
                Thread.sleep(delay)
            }
            delete()
        } catch (e: DiscordException) {
            logger.error(e) { "Error deleting message: $content inside channel: ${channel.name}" }
        }
    }
}
