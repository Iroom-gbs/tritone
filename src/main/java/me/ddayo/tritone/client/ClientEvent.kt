package me.ddayo.tritone.client

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.tritone.client.util.MinecraftStringUtil
import me.iroom.tritone.DiscordAPI
import me.iroom.tritone.util.Vector3D
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.GL21.*

class ClientEvent {

    companion object {
        val logger = LogManager.getLogger()
    }

    fun recalculateDist() {
        if(Minecraft.getInstance().player != null) {
            val cloned = HashSet<String>(DiscordAPI.managedPlayer).toMutableSet()
            for (x in Minecraft.getInstance().world?.players!!) {
                if(x.uniqueID == Minecraft.getInstance().player!!.uniqueID) continue
                val p = Minecraft.getInstance().player!!.positionVec
                val o = x.positionVec

                if (cloned.contains(x.uniqueID.toString())) {
                    cloned.remove(x.uniqueID.toString())
                    DiscordAPI.setVolume(DiscordAPI.voicePlayerList[x.uniqueID.toString()]!!, Vector3D(p.x, p.y, p.z), Vector3D(o.x, o.y, o.z))
                } else if (DiscordAPI.voicePlayerList.containsKey(x.uniqueID.toString())) {
                    MinecraftStringUtil.nameCache[x.uniqueID] = x.name.string
                    DiscordAPI.managedPlayer.add(x.uniqueID.toString())
                    DiscordAPI.setVolume(DiscordAPI.voicePlayerList[x.uniqueID.toString()]!!, Vector3D(p.x, p.y, p.z), Vector3D(o.x, o.y, o.z))
                }
            }

            for (p in cloned)
                DiscordAPI.setVolumeZero(DiscordAPI.voicePlayerList[p]!!)
            DiscordAPI.managedPlayer.removeAll(cloned)
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRender(event: RenderGameOverlayEvent.Pre) {
        recalculateDist()
        DiscordAPI.tick()
    }

    private fun renderOverlay() {
        if(DiscordAPI.currentLobby == 0L) {
            RenderUtil.bindTexture("testwhite.png")
            RenderUtil.renderRect(0, 0, 60, 25)
            Minecraft.getInstance().fontRenderer.drawString(MatrixStack(), MinecraftStringUtil.getString("connecting"), 0.0f, 0.0f, 0x000000)
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRd(event: GuiScreenEvent.DrawScreenEvent.Pre) {
        recalculateDist()
        DiscordAPI.tick()
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun renderOverlay(event: RenderGameOverlayEvent.Post) {
        if(event.type == RenderGameOverlayEvent.ElementType.AIR)
            renderOverlay()
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun renderOverlay(event: GuiScreenEvent.DrawScreenEvent.Post) = renderOverlay()
}