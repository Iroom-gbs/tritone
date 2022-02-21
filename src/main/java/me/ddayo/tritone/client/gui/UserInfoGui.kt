package me.ddayo.tritone.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.tritone.client.util.MinecraftStringUtil
import me.iroom.tritone.DiscordAPI
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent


class UserInfoGui(val name: String): Screen(StringTextComponent.EMPTY) {
    val discordId = MinecraftStringUtil.getIdByName(name)

    init {
        if(DiscordAPI.userVolume[discordId] == null)
            DiscordAPI.userVolume[discordId] = 100.0
    }

    val userVolume = DiscordAPI.userVolume[discordId]

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {

        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }
}