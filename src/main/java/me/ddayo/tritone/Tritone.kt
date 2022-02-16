package me.ddayo.tritone

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.common.MinecraftForge
import me.ddayo.tritone.Tritone.Companion.MOD_ID
import me.ddayo.tritone.client.ClientFMLEvent
import me.ddayo.tritone.client.discord.ForgeDiscordEvent
import me.iroom.tritone.DiscordAPI
import net.minecraft.client.Minecraft
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.common.Mod
import org.apache.commons.lang3.SystemUtils
import org.apache.logging.log4j.LogManager
import java.io.File

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
class Tritone {
    companion object {
        // Directly reference a log4j logger.
        private val logger = LogManager.getLogger()
        const val MOD_ID = "tritone"
        const val CLIENT_KEY = 941752061945581608
    }

    init {
        if(SystemUtils.IS_OS_WINDOWS) {
            System.load(File(Minecraft.getInstance().gameDir, "mods/discord_game_sdk.dll").canonicalPath)
            System.load(File(Minecraft.getInstance().gameDir, "mods/native.dll").canonicalPath)
        }
        else if(SystemUtils.IS_OS_LINUX) {
            System.load(File(Minecraft.getInstance().gameDir, "mods/discord_game_sdk.so").canonicalPath)
            System.load(File(Minecraft.getInstance().gameDir, "mods/libnative.so").canonicalPath)
            System.load(File(Minecraft.getInstance().gameDir, "mods/libdiscord_game_sdk.so").canonicalPath)
            System.load(File(Minecraft.getInstance().gameDir, "mods/native.so").canonicalPath)
        }
        else throw IllegalStateException("Not supported OS")

        DiscordAPI.initialize(CLIENT_KEY, ForgeDiscordEvent())
        FMLJavaModLoadingContext.get().modEventBus.register(ClientFMLEvent())
    }
}