package me.ddayo.tritone.client.discord

import me.iroom.tritone.DiscordEvent
import net.minecraft.client.Minecraft

class ForgeDiscordEvent: DiscordEvent() {
    override fun getMCName() = Minecraft.getInstance().session.playerID
}