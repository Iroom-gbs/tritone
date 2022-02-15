package me.ddayo.tritone.client

import me.ddayo.tritone.client.discord.DiscordAPI
import me.ddayo.tritone.client.util.MathUtil
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

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRender(event: RenderGameOverlayEvent.Pre) {
        val cloned = HashSet<String>(DiscordAPI.managedPlayer).toMutableSet()
        for(x in Minecraft.getInstance().world?.players!!) {
            //logger.info(x.position)
            //logger.info(x.uniqueID.toString())
            //logger.info(MathUtil.getVolume(MathUtil.getDistance(x.positionVec)))
            if (cloned.contains(x.uniqueID.toString())) {
                cloned.remove(x.uniqueID.toString())
                //logger.info(MathUtil.getVolume(MathUtil.getDistance(x.positionVec)))
                DiscordAPI.setVoiceLevel(DiscordAPI.voicePlayerList[x.uniqueID.toString()]!!, MathUtil.getVolume(MathUtil.getDistance(x.positionVec)))
            } else if (DiscordAPI.voicePlayerList.containsKey(x.uniqueID.toString())) {
                DiscordAPI.managedPlayer.add(x.uniqueID.toString())
                DiscordAPI.setVoiceLevel(DiscordAPI.voicePlayerList[x.uniqueID.toString()]!!, MathUtil.getVolume(MathUtil.getDistance(x.positionVec)))
            }
        }

        for(p in cloned)
            DiscordAPI.setVoiceLevel(DiscordAPI.voicePlayerList[p]!!, 0)
        DiscordAPI.managedPlayer.removeAll(cloned)
        DiscordAPI.tick()
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRd(event: net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Pre) {
        DiscordAPI.tick()
    }
}