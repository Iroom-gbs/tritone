package me.ddayo.tritone.client.util

import me.ddayo.tritone.Tritone

class MinecraftStringUtil {
    companion object {
        fun keyString(name: String) = "KEY.${Tritone.MOD_ID}.$name"
        const val keyGuiCategory = "KEY.${Tritone.MOD_ID}.gui"
        const val keyDiscordCategory = "KEY.${Tritone.MOD_ID}.discord"
    }
}