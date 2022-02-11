package me.ddayo.discordmumble.client.key

import me.ddayo.discordmumble.client.discord.DiscordAPI
import me.ddayo.discordmumble.client.gui.ParticipantGui
import me.ddayo.discordmumble.client.gui.VoiceChannelListGui
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import me.ddayo.discordmumble.client.util.MinecraftStringUtil.Companion.keyString
import me.ddayo.discordmumble.client.util.MinecraftStringUtil.Companion.keyDiscordCategory
import me.ddayo.discordmumble.client.util.MinecraftStringUtil.Companion.keyGuiCategory
import net.minecraft.client.Minecraft
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.lwjgl.glfw.GLFW

class Key {

    companion object {
        private val keys = emptyList<KeyBinding>().toMutableList()
        private val openParticipantGui = OptionKey(keyString("participateGui"), GLFW.GLFW_KEY_O, keyGuiCategory)
        private val openVoiceChannelGui = OptionKey(keyString("voiceChannelGui"), GLFW.GLFW_KEY_V, keyGuiCategory)
        private val muteKey = OptionKey(keyString("mute"), GLFW.GLFW_KEY_M, keyDiscordCategory)
        private val muteInputKey = OptionKey(keyString("muteInput"), GLFW.GLFW_KEY_I, keyDiscordCategory)
        private var initialized = false

        fun registerKeys() {
            if(initialized)
                throw IllegalAccessException("Key need to initialized only once!!")
            initialized = true
            keys.add(openParticipantGui)
            keys.add(openVoiceChannelGui)
            keys.add(muteKey)
            keys.add(muteInputKey)
            for(k in keys)
                ClientRegistry.registerKeyBinding(k)
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onClientTick(event: ClientTickEvent) {
        if(openParticipantGui.isKeyDown)
            Minecraft.getInstance().displayGuiScreen(ParticipantGui())

        if(openVoiceChannelGui.isKeyDown)
            Minecraft.getInstance().displayGuiScreen(VoiceChannelListGui())

        if(muteKey.isKeyDown)
            DiscordAPI.inverseMuteStatus()

        if(muteInputKey.isKeyDown)
            DiscordAPI.inverseInputMuteStatus()
    }
}