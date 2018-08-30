package org.darkjaguar.djbot.commands.roll

import org.darkjaguar.djbot.events.CommandEvent
import org.darkjaguar.djbot.utils.sendMessage
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import java.util.*

object RollEvent : CommandEvent {
    private val random = Random()

    override val command: String
        get() = "roll"

    override fun onEvent(event: MessageReceivedEvent, args: List<String>) {
        val min = getMin(args)
        val max = getMax(args)

        event.sendMessage("${event.author} rolled ${random.nextInt(max - min + 1) + min} ($min - $max)")
    }

    private fun getMax(args: List<String>): Int {
        return (when {
            args.size >= 2 -> args[1].toIntOrNull()
            args.isNotEmpty() -> args[0].toIntOrNull()
            else -> null
        }) ?: 100
    }

    private fun getMin(args: List<String>): Int {
        return (if (args.size >= 2)
            args[0].toIntOrNull()
        else null) ?: 0
    }
}