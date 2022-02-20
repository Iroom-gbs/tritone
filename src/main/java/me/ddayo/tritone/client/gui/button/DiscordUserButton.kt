package me.ddayo.tritone.client.gui.button

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.tritone.Tritone
import me.ddayo.tritone.client.gui.ParticipantGui
import me.ddayo.tritone.client.util.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation


class DiscordUserButton(private val mcName: String) {
    companion object {
        const val HEIGHT = 20.0
        const val WIDTH = ParticipantGui.tw - 10.0
        const val MAX_TEXT_WIDTH = WIDTH - 50
    }

    private val stringRender = Minecraft.getInstance().fontRenderer

    fun render(x: Double, y: Double) {
        RenderUtil.bindTexture("testred.png")
        RenderUtil.renderRect(x, y, WIDTH, HEIGHT)
        val matrix = MatrixStack()
        matrix.push()
        if(stringRender.getStringWidth(mcName) > MAX_TEXT_WIDTH)
            stringRender.drawString(matrix, "${stringRender.trimStringToWidth(mcName, MAX_TEXT_WIDTH.toInt())}...", (x + 5).toFloat(), (y + 2).toFloat(), 0xffffff)
        else stringRender.drawString(matrix, mcName, (x + 5).toFloat(), (y + 2).toFloat(), 0xffffff)
    }
}