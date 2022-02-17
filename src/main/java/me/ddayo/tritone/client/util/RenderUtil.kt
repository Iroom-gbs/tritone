package me.ddayo.tritone.client.util

import me.ddayo.tritone.Tritone
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL21.*

class RenderUtil {
    companion object {
        @JvmStatic
        fun bindTexture(texture: String) = Minecraft.getInstance().textureManager.bindTexture(ResourceLocation(Tritone.MOD_ID, "textures/$texture"))

        @JvmStatic
        fun renderRect(x: Int, y: Int, w: Int, h: Int) {
            glBegin(GL_QUADS)
            run {
                glTexCoord2i(1, 0)
                glVertex2i(x + w, y)
                glTexCoord2i(0, 0)
                glVertex2i(x, y)
                glTexCoord2i(0, 1)
                glVertex2i(x, y + h)
                glTexCoord2i(1, 1)
                glVertex2i(x + w, y + h)
            }
            glEnd()
        }
    }
}