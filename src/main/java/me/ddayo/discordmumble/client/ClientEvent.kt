package me.ddayo.discordmumble.client

import me.ddayo.discordmumble.client.discord.DiscordAPI
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.apache.logging.log4j.LogManager

class ClientEvent {
    companion object {
        val logger = LogManager.getLogger()
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRender(event: RenderGameOverlayEvent.Pre) {
        DiscordAPI.tick()
    }
}