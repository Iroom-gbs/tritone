package me.ddayo.tritone.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.tritone.client.gui.button.AudibleButton
import me.ddayo.tritone.client.gui.button.DiscordUserButton
import me.ddayo.tritone.client.gui.button.MuteButton
import me.ddayo.tritone.client.gui.button.SwitchUserListModButton
import me.ddayo.tritone.client.util.MinecraftStringUtil
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.GL21.*
import me.ddayo.tritone.client.util.RenderUtil.Companion.renderRect
import me.ddayo.tritone.client.util.RenderUtil.Companion.bindTexture
import me.iroom.tritone.DiscordAPI
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW
import java.util.*

class ParticipantGui: Screen(StringTextComponent("")) {
    companion object {
        private val logger = LogManager.getLogger()
        const val tw = 150
        const val th = 240
        const val NEAR = false
        const val ALL = true
        var mod = NEAR
        var handleOpen = true
    }

    val debugList = listOf(*DiscordAPI.voicePlayerList.keys.mapNotNull { MinecraftStringUtil.nameCache[UUID.fromString(it)] }.toTypedArray(), "hello", "hellooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo")
    var listStartPos = 35


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

            var renderPos = listStartPos
            for(d in debugList) {
                if(renderPos > 35 - DiscordUserButton.HEIGHT && renderPos < th - 35) {
                    DiscordUserButton(d).render(5, renderPos)
                }
                renderPos += DiscordUserButton.HEIGHT
            }
        }
        glPopMatrix()
        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(keyCode == GLFW.GLFW_KEY_O) {
            Minecraft.getInstance().displayGuiScreen(null)
            handleOpen = false
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }
}