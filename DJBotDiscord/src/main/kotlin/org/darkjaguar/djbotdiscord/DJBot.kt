package org.darkjaguar.djbotdiscord

import com.beust.jcommander.JCommander
import org.darkjaguar.djbotdiscord.config.DJBotConfig
import org.darkjaguar.djbotdiscord.discord.ClassDiscordCommandEvent
import org.darkjaguar.djbotdiscord.events.CommandEventManager
import org.darkjaguar.djbotdiscord.roll.RollCommandEvent
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.events.EventDispatcher

class DJBot(private val config: DJBotConfig) {
    private val client = ClientBuilder()
        .withToken(config.token)
        .build()

    init {
        registerListeners(client.dispatcher!!)

        client.login()
    }

    private fun registerListeners(dispatcher: EventDispatcher) {
        // Create and add all listeners here
        dispatcher.registerListener(createCommandEventManager())
    }

    private fun createCommandEventManager(): Any {
        return CommandEventManager(
            config,
            RollCommandEvent(),
            ClassDiscordCommandEvent()
        )
    }
}

fun main(args: Array<String>) {
    val config = DJBotConfig()
    JCommander.newBuilder().addObject(config).build().parse(*args)

    DJBot(config)
}
