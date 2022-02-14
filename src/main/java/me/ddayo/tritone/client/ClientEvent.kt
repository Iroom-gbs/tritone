package me.ddayo.tritone.client

import me.ddayo.tritone.client.discord.DiscordAPI
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

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRd(event: net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Pre) {
        DiscordAPI.tick()
    }
}