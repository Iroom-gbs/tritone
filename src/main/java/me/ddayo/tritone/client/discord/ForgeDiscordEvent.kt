package me.ddayo.tritone.client.discord

import me.iroom.tritone.DiscordEvent
import net.minecraft.client.Minecraft
import net.minecraft.util.text.StringTextComponent

class ForgeDiscordEvent: DiscordEvent() {
    override fun getMCName() = Minecraft.getInstance().player!!.uniqueID.toString()
    override fun voiceConnected() {
        Minecraft.getInstance().player?.sendMessage(StringTextComponent("음성채팅에 연결되었습니다!"), Minecraft.getInstance().player?.uniqueID)
        super.voiceConnected()
    }
}