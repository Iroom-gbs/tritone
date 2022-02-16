package me.ddayo.tritone.client

import me.iroom.tritone.DiscordAPI
import me.iroom.tritone.util.MathUtil
import me.iroom.tritone.util.Vector3D
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.apache.logging.log4j.LogManager

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

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRd(event: net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Pre) {
        recalculateDist()
        DiscordAPI.tick()
    }
}