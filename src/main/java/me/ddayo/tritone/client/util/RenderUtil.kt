package me.ddayo.tritone.client.util

import com.google.gson.JsonParser
import me.ddayo.tritone.Tritone
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.GL21.*
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

class RenderUtil {
    companion object {
        private val skinData = emptyMap<String, IntArray>().toMutableMap()

        @JvmStatic
        fun tryGetSkin(uuid: String): IntArray {
            if(skinData.containsKey(uuid)) return skinData[uuid]!!
            val base64 = Base64.getDecoder().decode(JsonParser().parse(URL("https://sessionserver.mojang.com/session/minecraft/profile/$uuid").readText()).asJsonObject.getAsJsonArray("properties").first { it.asJsonObject.get("name").asString == "textures" }.asJsonObject.get("value").asString).decodeToString()
            LogManager.getLogger().info(JsonParser().parse(base64).asJsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN")["url"].asString)
            val skin = ImageIO.read(URL(JsonParser().parse(base64).asJsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN")["url"].asString))
            val buf = IntArray(257)
            var i = 0
            for(x in 8 until 16)
                for(y in 8 until 16) {
                    val p = skin.getRGB(y, x)
                    buf[i] = p shl 8
                    i++
                    LogManager.getLogger().info("${((p and 0xff0000) ushr 16).toByte()} ${((p and 0xff00) ushr 8).toByte()} ${(p and 0xff).toByte()}")
                    LogManager.getLogger().info(p)
                }
            skinData[uuid] = buf
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

        fun push(f: () -> Unit) {
            glPushMatrix()
            f()
            glPopMatrix()
        }
    }
}