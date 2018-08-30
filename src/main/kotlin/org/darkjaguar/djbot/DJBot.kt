package org.darkjaguar.djbot

import org.darkjaguar.djbot.commands.roll.RollEvent
import org.darkjaguar.djbot.events.CommandEventManager
import org.darkjaguar.djbot.logging.logger
import org.darkjaguar.djbot.music.MusicPlayer
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient

object DJBotRunner

class DJBot(token: String) {
    private val client: IDiscordClient = ClientBuilder()
            .withToken(token)
            .build()
    private val musicPlayer = MusicPlayer()

    init {
        logger.info("Starting with token: $token")
        registerCommandManager()
        client.login()
    }

    private fun registerCommandManager() {
        CommandEventManager.registerCommand(RollEvent)
        client.dispatcher.registerListener(CommandEventManager)
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        DJBotRunner.logger.error("Token required as first argument.")
    }

    DJBot(args[0])
}