package me.ddayo.tritone.client.gui

import me.ddayo.tritone.Tritone
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL21
import org.lwjgl.stb.STBImage
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import java.nio.ByteBuffer

object RenderUtil {
    private fun bindTexture(tex: String) = Minecraft.getInstance().textureManager.bindTexture(ResourceLocation(Tritone.MOD_ID, "textures/$tex"))
    private val images = emptyMap<String, Int>().toMutableMap()

    private var iab = false

    private fun withValidate(x: () -> Unit) {
        if(iab) throw IllegalStateException("Trying to overwrite texture")
        iab = true
        x()
        iab = false
    }

    fun useTexture(tex: Int, x: () -> Unit) {
        GL21.glBindTexture(GL21.GL_TEXTURE_2D, tex)
        withValidate(x)
        GL21.glBindTexture(GL21.GL_TEXTURE_2D, 0)
    }

    fun useTexture(str: String, x: () -> Unit) {
        bindTexture(str)
        withValidate(x)
    }

    private fun registerBufferedImage(image: BufferedImage): Int {
        val tex = GL21.glGenTextures()
        useTexture(tex) {
            GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_WRAP_S, GL21.GL_CLAMP_TO_EDGE)
            GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_WRAP_T, GL21.GL_CLAMP_TO_EDGE)
            GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_MIN_FILTER, GL21.GL_LINEAR)
            GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_MAG_FILTER, GL21.GL_LINEAR)

            val bytes = (image.raster.dataBuffer as DataBufferByte).data
            val buf = ByteBuffer.allocateDirect(bytes.size)
            buf.put(bytes)
            buf.flip()
            GL21.glTexImage2D(
                GL21.GL_TEXTURE_2D,
                0,
                GL21.GL_RGB,
                image.width,
                image.height,
                0,
                GL21.GL_RGB,
                GL21.GL_UNSIGNED_BYTE,
                buf
            )
        }
        return tex
    }

    fun bindGrayscaleBuffer(buf: ByteBuffer, width: Int, height: Int): Int {
        val tex = GL21.glGenTextures()
        useTexture(tex) {
            GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_WRAP_S, GL21.GL_CLAMP_TO_EDGE)
            GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_WRAP_T, GL21.GL_CLAMP_TO_EDGE)
            GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_MIN_FILTER, GL21.GL_LINEAR)
            GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_MAG_FILTER, GL21.GL_LINEAR)
            GL21.glPixelStorei(GL21.GL_UNPACK_ALIGNMENT, 1)

            GL21.glTexImage2D(GL21.GL_TEXTURE_2D, 0, GL21.GL_ALPHA, width, height, 0, GL21.GL_ALPHA, GL21.GL_UNSIGNED_BYTE, buf)
        }
        return tex
    }

    fun resourceExists(tex: String) = Minecraft.getInstance().resourceManager.hasResource(ResourceLocation(Tritone.MOD_ID, tex))
    fun extResourceExists(tex: String) = File(Minecraft.getInstance().gameDir, "assets/images/$tex").exists()

    fun render() = render(0, 0, 1920, 1080)

    fun render(x: Int, y: Int, w: Int, h: Int) = render(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())

    fun render(x: Double, y: Double, w: Double, h: Double) = render(x, y, w, h, 0.0, 1.0, 0.0, 1.0)

    fun render(x: Double, y: Double, w: Double, h: Double, th1: Double, th2: Double, tv1: Double, tv2: Double) {
        GL21.glBegin(GL21.GL_QUADS)
        GL21.glTexCoord2d(th2, tv1)
        GL21.glVertex2d(x + w, y)
        GL21.glTexCoord2d(th1, tv1)
        GL21.glVertex2d(x, y)
        GL21.glTexCoord2d(th1, tv2)
        GL21.glVertex2d(x, y + h)
        GL21.glTexCoord2d(th2, tv2)
        GL21.glVertex2d(x + w, y + h)
        GL21.glEnd()
    }

    fun push(x: () -> Unit) {
        GL21.glPushMatrix()
        x()
        GL21.glPopMatrix()
    }

    fun FHDScale(width: Int, height: Int, x: () -> Unit) = FHDScale(width.toDouble(), height.toDouble(), x)

    fun FHDScale(gui: Screen, x: () -> Unit) = FHDScale(gui.height, gui.width, x)

    fun FHDScale(width: Double, height: Double, x: () -> Unit) {
        push {
            GL21.glTranslated((width - height * 16.0 / 9) / 2, 0.0, 0.0)
            GL21.glScaled(height / 1080.0, height / 1080.0, height / 1080.0)
            x()
        }
    }

    fun removeAllTextures() {
        GL21.glDeleteTextures(images.values.toIntArray())
        images.clear()
    }

    fun removeTexture(tex: String) = GL21.glDeleteTextures(images[tex]!!)
}