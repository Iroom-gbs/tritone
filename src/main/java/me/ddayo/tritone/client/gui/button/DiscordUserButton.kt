package me.ddayo.tritone.client.gui.button

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.tritone.Tritone
import me.ddayo.tritone.client.gui.ParticipantGui
import me.ddayo.tritone.client.util.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation


class DiscordUserButton(private val mcName: String) {
    companion object {
        const val HEIGHT = 20
        const val WIDTH = ParticipantGui.tw - 10
        const val MAX_TEXT_WIDTH = WIDTH - 50
    }

    private val stringRender = Minecraft.getInstance().fontRenderer

    fun render(x: Int, y: Int) {
        RenderUtil.bindTexture("testred.png")
        RenderUtil.renderRect(x, y, WIDTH, HEIGHT)
        val matrix = MatrixStack()
        matrix.push()
        if(stringRender.getStringWidth(mcName) > MAX_TEXT_WIDTH)
            stringRender.drawString(matrix, "${stringRender.trimStringToWidth(mcName, MAX_TEXT_WIDTH)}...", x + 5.0f, y + 2.0f, 0xffffff)
        else stringRender.drawString(matrix, mcName, x + 5.0f, y + 2.0f, 0xffffff)
    }
}