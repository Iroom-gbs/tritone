package me.ddayo.tritone

import com.google.gson.Gson
import me.ddayo.tritone.Tritone.Companion.MOD_ID
import me.ddayo.tritone.client.ClientFMLEvent
import me.ddayo.tritone.client.data.Config
import me.ddayo.tritone.client.discord.ForgeDiscordEvent
import me.iroom.tritone.DiscordAPI
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.commons.lang3.SystemUtils
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileReader
import java.lang.reflect.Field

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
class Tritone {
    companion object {
        // Directly reference a log4j logger.
        private val logger = LogManager.getLogger()
        const val MOD_ID = "tritone"
        val configFile = File(Minecraft.getInstance().gameDir, "mods/tritone/config.json")
        val config = if(configFile.exists()) Gson().fromJson(FileReader(configFile), Config::class.java) else Config()
        val CLIENT_KEY = config.clientId
    }

    init {
        if(SystemUtils.IS_OS_WINDOWS) {
            System.load(File(Minecraft.getInstance().gameDir, "mods/tritone/discord_game_sdk.dll").canonicalPath)
            System.load(File(Minecraft.getInstance().gameDir, "mods/tritone/native.dll").canonicalPath)
        }
        else if(SystemUtils.IS_OS_LINUX) {
            System.load(File(Minecraft.getInstance().gameDir, "mods/tritone/libnative.so").canonicalPath)
        }
        else throw IllegalStateException("Not supported OS")

        DiscordAPI.initialize(CLIENT_KEY, ForgeDiscordEvent())
        FMLJavaModLoadingContext.get().modEventBus.register(ClientFMLEvent())

    }

    private fun setEnv(key: String, value: String) {
        try {
            val env = System.getenv()
            val cl: Class<*> = env.javaClass
            val field: Field = cl.getDeclaredField("m")
            field.isAccessible = true
            val writableEnv = field.get(env) as MutableMap<String, String>
            writableEnv[key] = value
        } catch (e: Exception) {
            throw IllegalStateException("Failed to set environment variable", e)
        }
    }
}