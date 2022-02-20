package me.ddayo.tritone.client.discord

import me.iroom.tritone.DiscordEvent
import net.minecraft.client.Minecraft

class ForgeDiscordEvent: DiscordEvent() {
    override fun getMCName() = Minecraft.getInstance().session.playerID.replaceFirst(
            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)".toRegex(), "$1-$2-$3-$4-$5"
    )
}