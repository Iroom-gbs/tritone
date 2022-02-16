package me.ddayo.tritone.client

import me.ddayo.tritone.Tritone
import me.ddayo.tritone.client.key.Key
import me.iroom.tritone.DiscordAPI
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

class ClientFMLEvent {
    @SubscribeEvent
    fun onClientStart(event: FMLClientSetupEvent) {
        Key.registerKeys()
        MinecraftForge.EVENT_BUS.register(Key())
        MinecraftForge.EVENT_BUS.register(ClientEvent())
        DiscordAPI.tryJoinLobby(Tritone.config.lobbyName)
    }
}