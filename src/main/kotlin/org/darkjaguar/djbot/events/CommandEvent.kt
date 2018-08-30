package org.darkjaguar.djbot.events

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

interface CommandEvent {
    val command: String

    fun onEvent(event: MessageReceivedEvent, args: List<String>)
}
