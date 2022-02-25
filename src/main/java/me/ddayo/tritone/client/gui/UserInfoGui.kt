package me.ddayo.tritone.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.tritone.client.util.MinecraftStringUtil
import me.ddayo.tritone.client.util.RenderUtil.Companion.push
import me.ddayo.tritone.client.util.RenderUtil.Companion.renderRect
import me.ddayo.tritone.client.util.RenderUtil.Companion.bindTexture
import me.ddayo.tritone.client.util.RenderUtil.Companion.tryGetSkin
import me.iroom.tritone.DiscordAPI
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.GL21.*;
import org.lwjgl.opengl.GL33
import kotlin.math.max
import kotlin.math.min


class UserInfoGui(val name: String): Screen(StringTextComponent.EMPTY) {
    companion object {
        const val tw = 200
        const val th = 80
    }

    val discordId = MinecraftStringUtil.getIdByName(name)

    init {
        if(DiscordAPI.userVolume[discordId] == null)
            DiscordAPI.userVolume[discordId] = 100.0
    }

    var texture = -1

    fun initializeGl() {
        texture = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texture)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 8, 8, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8, tryGetSkin("f484e23af5084b739246186d8e06efbc"))
        GL33.glGenerateMipmap(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    var sliderX = 60 + DiscordAPI.userVolume[discordId]!! / 200.0 * 125

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        if(texture == -1)
            initializeGl()

        push {
            glTranslated(width / 2.0 - 100, height / 2.0 - 40, 0.0)
            bindTexture("testwhite.png")
            renderRect(0, 0, tw, th)

            push {
                glBindTexture(GL_TEXTURE_2D, texture)
                glEnable(GL_DEPTH_TEST)
                glEnable(GL_TEXTURE_2D)
                renderRect(15, 15, 40, 40)
                glBindTexture(GL_TEXTURE_2D, 0)
            }

            push {
                bindTexture("testred.png")
                renderRect(65, 53, 125, 4)
            }

            push {
                bindTexture("testblue.png")
                renderRect(sliderX, 50.0, 10.0, 10.0)
            }
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }

    private var isSliderMovingMode = false

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mx = mouseX - (width / 2.0 - tw / 2)
        val my = mouseY - (height / 2.0 - th / 2)
        if(mx in 60.0..195.0 && my in 50.0..60.0) {
            sliderX = mx - 5
            DiscordAPI.userVolume[discordId] = (sliderX - 60) * 200.0 / 125
            LogManager.getLogger().info((sliderX - 60) * 200.0 / 125)
            isSliderMovingMode = true
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
        val mx = mouseX - (width / 2.0 - tw / 2)
        if(isSliderMovingMode) {
            sliderX = min(max(mx - 5, 60.0), 185.0)
            DiscordAPI.userVolume[discordId] = (sliderX - 60) * 200.0 / 125
            LogManager.getLogger().info((sliderX - 60) * 200.0 / 125)
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        isSliderMovingMode = false
        return super.mouseReleased(mouseX, mouseY, button)
    }
}