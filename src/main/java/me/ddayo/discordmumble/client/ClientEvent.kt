package me.ddayo.discordmumble.client

import me.ddayo.discordmumble.client.key.Key
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

class ClientEvent {
    @SubscribeEvent
    fun onClientStart(event: FMLClientSetupEvent) {
        Key.registerKeys()
        MinecraftForge.EVENT_BUS.register(Key())
    }
}