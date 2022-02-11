package me.ddayo.discordmumble.network

import me.ddayo.discordmumble.client.discord.DiscordUserData

class NetworkData {
    val participantList = emptyMap<Long, DiscordUserData>()
    val channelList = emptyMap<String, List<Long>>()
}