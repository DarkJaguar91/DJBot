package org.darkjaguar.djbotdiscord

import org.darkjaguar.djbotdiscord.config.DJBotConfig
import org.darkjaguar.djbotdiscord.discord.ClassDiscordCommandEvent
import org.darkjaguar.djbotdiscord.events.CommandEventManager
import org.darkjaguar.djbotdiscord.roll.RollCommandEvent
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.events.EventDispatcher

class DJBot(token: String) {
    private val client = ClientBuilder()
            .withToken(token)
            .build()
    private val config = DJBotConfig()

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
    DJBot("NDgxMjI0OTUyMzU2Mjc0MTg3.Dmp23Q.C1D3HplptjlD29g55bBgCa21wxo")
}