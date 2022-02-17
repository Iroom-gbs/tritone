package me.ddayo.tritone.client.gui.button

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.tritone.client.gui.ParticipantGui
import me.ddayo.tritone.client.util.RenderUtil
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.util.text.StringTextComponent

class SwitchUserListModButton(x: Int, y: Int, val w: Int, val h: Int): Button(x, y, w, h, StringTextComponent.EMPTY, IPressable {
    ParticipantGui.mod = !ParticipantGui.mod
}) {
    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        RenderUtil.bindTexture("testred.png")
        RenderUtil.renderRect(x, y, w, h)
    }
}