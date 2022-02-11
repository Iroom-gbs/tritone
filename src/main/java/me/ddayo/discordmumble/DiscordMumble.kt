package me.ddayo.discordmumble

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.common.MinecraftForge
import me.ddayo.discordmumble.DiscordMumble
import me.ddayo.discordmumble.DiscordMumble.Companion.MOD_ID
import me.ddayo.discordmumble.client.ClientEvent
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.InterModComms.IMCMessage
import java.util.stream.Collectors
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.event.RegistryEvent.Register
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
class DiscordMumble {
    companion object {
        // Directly reference a log4j logger.
        private val LOGGER = LogManager.getLogger()
        const val MOD_ID = "discordmumble"
    }

    init {
        FMLJavaModLoadingContext.get().modEventBus.addListener { event: FMLCommonSetupEvent -> setup(event) }
        MinecraftForge.EVENT_BUS.register(this)
        FMLJavaModLoadingContext.get().modEventBus.register(ClientEvent())
    }

    private fun setup(event: FMLCommonSetupEvent) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    fun onServerStarting(event: FMLServerStartingEvent) {

    }
}