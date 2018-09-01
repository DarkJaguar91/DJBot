package org.darkjaguar.djbotdiscord.config

import com.beust.jcommander.Parameter

class DJBotConfig {
    @Parameter(names = ["-t", "-token"], description = "Discord access token for the DJBot.", required = true)
    var token: String? = null

    @Parameter(names = ["-prefix", "-commandPrefix"], description = "Command prefix for DJBot commands. Defaults to '/'")
    var commandPrefix: String = "/"
}
