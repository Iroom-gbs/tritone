package me.ddayo.tritone.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.tritone.client.gui.button.AudibleButton
import me.ddayo.tritone.client.gui.button.MuteButton
import me.ddayo.tritone.client.gui.button.SwitchUserListModButton
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.GL21.*
import me.ddayo.tritone.client.util.RenderUtil.Companion.renderRect
import me.ddayo.tritone.client.util.RenderUtil.Companion.bindTexture

class ParticipantGui: Screen(StringTextComponent("")) {
    companion object {
        private val logger = LogManager.getLogger()
        const val tw = 150
        const val th = 240
        const val NEAR = false
        const val ALL = true
        var mod = NEAR
    }

    override fun init() {
        addButton(MuteButton(5 + (width - tw) / 2, th - 25 + (height - th) / 2, 20, 20))
        addButton(AudibleButton(30 + (width - tw) / 2, th - 25 + (height - th) / 2, 20, 20))
        addButton(SwitchUserListModButton(tw - 50 + (width - tw) / 2, th - 25 + (height / th), 45, 20))
        super.init()
    }

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        renderBackground(matrixStack)

        glPushMatrix()
        run {
            glEnable(GL_TEXTURE_2D)
            glEnable(GL_BLEND)

            glTranslated(width / 2.0 - tw / 2, height / 2.0 - th / 2, 0.0)

            bindTexture("testwhite.png")
            renderRect(0, 0, tw, th)

            bindTexture("testred.png")
            renderRect(5, 5, tw - 10, 25) //Search

            bindTexture("testred.png")
            renderRect(5, 35, tw - 10, th - 70) //Main
        }
        glPopMatrix()
        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }
}