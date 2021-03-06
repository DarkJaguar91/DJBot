package org.darkjaguar.djbotdiscord.events

import org.darkjaguar.djbotdiscord.config.DJBotConfig
import org.darkjaguar.djbotdiscord.events.interfaces.CommandEvent
import org.darkjaguar.djbotdiscord.logging.logger
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class CommandEventManager(private val config: DJBotConfig, vararg events: CommandEvent) {
    private val events = events.flatMap { commandEvent -> commandEvent.commands.map { it to commandEvent} }.toMap()

    @EventSubscriber
    fun onMessageReceivedEvent(event: MessageReceivedEvent) {
        val args = event.message.content.split(" ")

        if (args.isEmpty() || !args[0].startsWith(config.commandPrefix)) return

        val command = args[0].substring(1).toLowerCase()

        events[command]?.onEvent(event, args.subList(1, args.size))
                ?: logger.error { "No event manager found for commands: $command" }
    }
}
