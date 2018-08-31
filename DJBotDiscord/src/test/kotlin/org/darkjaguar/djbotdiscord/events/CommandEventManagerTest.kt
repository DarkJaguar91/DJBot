package org.darkjaguar.djbotdiscord.events

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.darkjaguar.djbotdiscord.config.DJBotConfig
import org.darkjaguar.djbotdiscord.events.interfaces.CommandEvent
import org.junit.jupiter.api.Test
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IMessage
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommandEventManagerTest {
    @Test
    internal fun `commandEventManager Should call event When message for event received`() {
        val eventName = "test"
        var eventFired = false
        val commandEventManager = CommandEventManager(DJBotConfig(COMMAND_PREFIX = "!"), object : CommandEvent {
            override val commands: List<String>
                get() = listOf(eventName)

            override fun onEvent(event: MessageReceivedEvent, args: List<String>) {
                eventFired = true
            }
        })

        val mockMessage = mock<IMessage> {
            on { content } doReturn "!$eventName"
        }

        val mockEvent = mock<MessageReceivedEvent> {
            on { message } doReturn mockMessage
        }

        commandEventManager.onMessageReceivedEvent(mockEvent)

        assertTrue(eventFired, "Event $eventName not fired as expected.")
    }

    @Test
    internal fun `commandEventManager With commandEvent that has multiple key words Should call event When message for event received`() {
        val eventNames = listOf("test", "tst", "testerize", "check")
        var countEventFired = 0
        val commandEventManager = CommandEventManager(DJBotConfig(COMMAND_PREFIX = "!"), object : CommandEvent {
            override val commands: List<String>
                get() = eventNames

            override fun onEvent(event: MessageReceivedEvent, args: List<String>) {
                countEventFired++
            }
        })

        val mockMessage = mock<IMessage> {
            on { content } doReturn eventNames.map { "!$it" }.toList()
        }
        val mockIncorrectMessage = mock<IMessage> {
            on { content } doReturn eventNames.map { "!notThisCommand" }.toList()
        }

        val mockEvent = mock<MessageReceivedEvent> {
            on { message } doReturn mockMessage
        }
        val mockIncorrectEvent = mock<MessageReceivedEvent> {
            on { message } doReturn mockIncorrectMessage
        }

        commandEventManager.onMessageReceivedEvent(mockEvent)
        commandEventManager.onMessageReceivedEvent(mockEvent)
        commandEventManager.onMessageReceivedEvent(mockIncorrectEvent)
        commandEventManager.onMessageReceivedEvent(mockEvent)
        commandEventManager.onMessageReceivedEvent(mockEvent)

        assertEquals(4, countEventFired, "Event fired the incorrect number of times!")
    }
}
