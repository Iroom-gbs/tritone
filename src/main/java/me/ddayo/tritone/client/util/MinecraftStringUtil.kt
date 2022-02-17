package me.ddayo.tritone.client.util

import me.ddayo.tritone.Tritone
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.text.StringTextComponent

class MinecraftStringUtil {
    companion object {
        fun keyString(name: String) = "KEY.${Tritone.MOD_ID}.$name"
        const val keyGuiCategory = "KEY.${Tritone.MOD_ID}.gui"
        const val keyDiscordCategory = "KEY.${Tritone.MOD_ID}.discord"
        fun getString(key: String) = I18n.format("STRING.${Tritone.MOD_ID}.$key")

        fun sendLocalizedString(key: String) = Minecraft.getInstance().player!!.sendMessage(StringTextComponent(getString(key)), Minecraft.getInstance().player!!.uniqueID)
    }
}