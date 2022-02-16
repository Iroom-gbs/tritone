package me.ddayo.tritone.client.discord

import net.minecraft.client.Minecraft
import net.minecraft.util.text.StringTextComponent
import org.apache.logging.log4j.LogManager


/***************************************
                 Warning

   Do not remove unused method here!!!
 ***************************************/

class DiscordAPI {
    companion object {
        val logger = LogManager.getLogger()

        @JvmStatic
        external fun initialize()

        @JvmStatic
        external fun tick()

        @JvmStatic
        external fun isMuted(): Boolean

        @JvmStatic
        private external fun setMute()

        @JvmStatic
        private external fun setUnmute()

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun serverListReloaded(l: Array<String>) {
            for(x in l)
                logger.info(x)
        }

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun nativeInitialized() {
            logger.info("Native Initialized")
        }

        @JvmStatic
        var currentLobby: Long = 0

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun lobbyMovedPre(lobby: Long) {
            currentLobby = lobby
        }

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun voiceConnected() {
            Minecraft.getInstance().player?.sendMessage(StringTextComponent("음성채팅에 연결되었습니다!"), Minecraft.getInstance().player?.uniqueID)
            logger.info("Voice connected")
        }

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun lobbyMoved() {
            logger.info("Lobby: $currentLobby has moved")
            getServerList()
        }

        @JvmStatic
        external fun joinLobby(name: String)

        @JvmStatic
        external fun getServerList()

        @JvmStatic
        external fun isInputMuted(): Boolean

        @JvmStatic
        private external fun setInputMute()

        @JvmStatic
        private external fun setInputUnmute()

        fun inverseMuteStatus() {
            LogManager.getLogger().info(isMuted())
            if(isMuted()) setUnmute() else setMute()
        }

        fun inverseInputMuteStatus() = if(isInputMuted()) setInputUnmute() else setInputMute()

        @JvmStatic
        external fun setVoiceLevel(id: Long, level: Int)

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun getMCName(): String {
            return Minecraft.getInstance().player!!.uniqueID.toString()
        }

        val voicePlayerList = emptyMap<String, Long>().toMutableMap()

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun addVoicePlayer(name: String, id: Long) {
            logger.info("$name with $id added")
            voicePlayerList[name] = id
            setVoiceLevel(id, 0) //mute by default
        }

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun clearVoicePlayerList() {
            logger.info("cleared")
            voicePlayerList.clear()
        }

        val managedPlayer = emptySet<String>().toMutableSet()
    }
}