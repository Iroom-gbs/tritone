package me.iroom.tritone

abstract class DiscordEvent {
    open fun serverListReloaded(l: Array<String>) {}
    open fun nativeInitialized(){}
    open fun voiceConnected(){}
    open fun lobbyMoved(lobby: Long){}
    abstract fun getMCName(): String
    open fun addVoicePlayer(name: String, id: Long){}
    open fun clearVoicePlayerList(){}
}