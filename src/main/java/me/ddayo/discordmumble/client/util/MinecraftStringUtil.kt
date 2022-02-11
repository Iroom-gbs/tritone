package me.ddayo.discordmumble.client.util

import me.ddayo.discordmumble.DiscordMumble

class MinecraftStringUtil {
    companion object {
        fun keyString(name: String) = "KEY.${DiscordMumble.MOD_ID}.$name"
        const val keyGuiCategory = "KEY.${DiscordMumble.MOD_ID}.gui"
        const val keyDiscordCategory = "KEY.${DiscordMumble.MOD_ID}.discord"
    }
}