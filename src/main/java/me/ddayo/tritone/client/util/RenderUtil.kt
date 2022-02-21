package me.ddayo.tritone.client.util

import com.google.gson.Gson
import com.google.gson.JsonParser
import me.ddayo.tritone.Tritone
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL21.*
import java.awt.image.BufferedImage
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

class RenderUtil {
    companion object {
        val skinData = emptyMap<String, BufferedImage>().toMutableMap()

        @JvmStatic
        fun tryGetSkin(uuid: String): BufferedImage {
            if(skinData.containsKey(uuid)) return skinData[uuid]!!
            val base64 = JsonParser().parse(URL("https://sessionserver.mojang.com/session/minecraft/profile/$uuid").readText()).asJsonObject.getAsJsonArray("properties").first { it.asJsonObject.get("name").asString == "textures" }.asJsonObject.get("value").asString
            val skin = ImageIO.read(URL(JsonParser().parse(URL(Base64.getDecoder().decode(base64).decodeToString()).readText()).asJsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN")["url"].asString))
            skinData[uuid] = skin.getSubimage(4, 4, 4, 4)
            return skinData[uuid]!!
        }

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

        @JvmStatic
        fun renderRect(x: Double, y: Double, w: Double, h: Double) {
            glBegin(GL_QUADS)
            run {
                glTexCoord2i(1, 0)
                glVertex2d(x + w, y)
                glTexCoord2i(0, 0)
                glVertex2d(x, y)
                glTexCoord2i(0, 1)
                glVertex2d(x, y + h)
                glTexCoord2i(1, 1)
                glVertex2d(x + w, y + h)
            }
            glEnd()
        }
    }
}