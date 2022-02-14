package me.ddayo.tritone.client

import me.ddayo.tritone.client.discord.DiscordAPI
import me.ddayo.tritone.client.util.MathUtil
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.apache.logging.log4j.LogManager

class ClientEvent {

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRender(event: RenderGameOverlayEvent.Pre) {
        val cloned = HashSet<String>(DiscordAPI.managedPlayer).toMutableSet()
        for(x in Minecraft.getInstance().world?.players!!)
            if(cloned.contains(x.name.string)) {
                cloned.remove(x.name.string)
                DiscordAPI.setVoiceLevel(DiscordAPI.voicePlayerList[x.name.string]!!, MathUtil.getVolume(MathUtil.getDistance(x.positionVec)))
            }
            else if(DiscordAPI.voicePlayerList.containsKey(x.name.string)) {
                DiscordAPI.managedPlayer.add(x.name.string)
                DiscordAPI.setVoiceLevel(DiscordAPI.voicePlayerList[x.name.string]!!, MathUtil.getVolume(MathUtil.getDistance(x.positionVec)))
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