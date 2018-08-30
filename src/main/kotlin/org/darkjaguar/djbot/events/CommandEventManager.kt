package org.darkjaguar.djbot.events

import org.darkjaguar.djbot.logging.logger
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent


object CommandEventManager {
    private val events = HashMap<String, CommandEvent>()

    internal fun registerCommand(event: CommandEvent) {
        events[event.command] = event
        logger.info("Command event registered: ${event.command}")
    }

    @EventSubscriber
    fun onMessageReceivedEvent(event: MessageReceivedEvent) {
        val args = event.message.content.split(" ")

        if (args.isEmpty() || !args[0].startsWith("/")) return

        val command = args[0].substring(1).toLowerCase()

        events[command]?.onEvent(event, args.subList(1, args.size))
                ?: logger.error("No event manager found for command: $command")
    }
}
