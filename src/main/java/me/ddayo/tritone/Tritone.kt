package me.ddayo.tritone

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.common.MinecraftForge
import me.ddayo.tritone.Tritone.Companion.MOD_ID
import me.ddayo.tritone.client.ClientFMLEvent
import me.ddayo.tritone.client.discord.DiscordAPI
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
class Tritone {
    companion object {
        // Directly reference a log4j logger.
        private val logger = LogManager.getLogger()
        const val MOD_ID = "tritone"
    }

    init {
        System.loadLibrary("discord_game_sdk")
        System.loadLibrary("native")
        DiscordAPI.initialize()
        FMLJavaModLoadingContext.get().modEventBus.addListener { event: FMLCommonSetupEvent -> setup(event) }
        MinecraftForge.EVENT_BUS.register(this)
        FMLJavaModLoadingContext.get().modEventBus.register(ClientFMLEvent())
    }

    private fun setup(event: FMLCommonSetupEvent) {

    }
}