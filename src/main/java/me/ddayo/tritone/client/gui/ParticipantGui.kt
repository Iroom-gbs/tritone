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
import kotlin.math.max
import kotlin.math.min

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

    private val debugList = listOf(*DiscordAPI.voicePlayerList.keys.mapNotNull { MinecraftStringUtil.nameCache[UUID.fromString(it)] }.toTypedArray(), "hello", "hellooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a")
    //val debugList = listOf(*DiscordAPI.voicePlayerList.keys.mapNotNull{MinecraftStringUtil.nameCache[UUID.fromString(it)]}.toTypedArray())
    private var barY = 1.0
    private val barHeight: Double
        get() = if(th - 70 > DiscordUserButton.HEIGHT * debugList.size) th - 70.0 else (th - 70.0) * (th - 70) / (DiscordUserButton.HEIGHT * debugList.size)

    var isBarLocationEditMod = false

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

            var renderPos = 35 - barY * DiscordUserButton.HEIGHT * debugList.size / (th - 70)
            for(d in debugList) {
                if(renderPos > 35 - DiscordUserButton.HEIGHT && renderPos < th - 35) {
                    DiscordUserButton(d).render(5.0, renderPos)
                }
                renderPos += DiscordUserButton.HEIGHT
            }

            bindTexture("testblue.png")
            renderRect(5 + DiscordUserButton.WIDTH - 10.0, barY + 35, 10.0, barHeight)
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

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mx = mouseX - (width / 2.0 - tw / 2)
        val my = mouseY - (height / 2.0 - th / 2)
        if(mx >= 5 + DiscordUserButton.WIDTH - 10 && mx <= 5 + DiscordUserButton.WIDTH
                && my >= 35 && my <= th - 35) {
            barY = min(max(0.0, my - 35 - barHeight / 2), th - 70.0 - barHeight)
            isBarLocationEditMod = true
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
        val mx = mouseX - (width / 2.0 - tw / 2)
        val my = mouseY - (height / 2.0 - th / 2)
        if(isBarLocationEditMod)
            barY = min(max(0.0, my - 35 - barHeight / 2), th - 70.0 - barHeight)

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        isBarLocationEditMod = false
        return super.mouseReleased(mouseX, mouseY, button)
    }
}