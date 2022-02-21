package me.iroom.tritone

import me.iroom.tritone.util.MathUtil
import me.iroom.tritone.util.Vector3D
import org.apache.logging.log4j.LogManager

/***************************************
                 Warning

   Do not remove unused method here!!!
 ***************************************/

class DiscordAPI {
    companion object {
        lateinit var event: DiscordEvent
        var lock = false

        fun initialize(clientKey: Long, event: DiscordEvent) {
            this.event = event
            initialize(clientKey)
        }

        private val logger = LogManager.getLogger()

        @JvmStatic
        private external fun initialize(clientKey: Long)

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
            event.serverListReloaded(l)
            for(x in l)
                logger.info(x)
        }

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun nativeInitialized() {
            event.nativeInitialized()
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
            event.voiceConnected()
            logger.info("Voice connected")
        }

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun lobbyMoved() {
            event.lobbyMoved(currentLobby)
            logger.info("Lobby: $currentLobby has moved")
            lock = false
        }

        @JvmStatic
        private external fun joinLobby(name: String)

        fun tryJoinLobby(name: String): Boolean {
            if(lock) return false
            lock = true
            joinLobby(name)
            return true
        }

        @JvmStatic
        external fun getServerList()

        @JvmStatic
        external fun isDeafened(): Boolean

        @JvmStatic
        private external fun setDeafened()

        @JvmStatic
        private external fun setUndeafened()

        fun inverseMuteStatus() = if(isMuted()) setUnmute() else setMute()

        fun inverseDeafStatus() = if(isDeafened()) setUndeafened() else setDeafened()

        @JvmStatic
        private external fun setVoiceLevel(id: Long, level: Int)

        fun setVolume(id: Long, player: Vector3D, other: Vector3D) {
            if(!userVolume.containsKey(id))
                userVolume[id] = 100.0
            setVoiceLevel(id, (MathUtil.getVolume(MathUtil.getDistance(player, other)) * userVolume[id]!!).toInt() / 100)
        }

        fun setVolumeZero(id: Long) = setVoiceLevel(id, 0)

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun getMCName(): String {
            return event.getMCName()//Minecraft.getInstance().player!!.uniqueID.toString()
        }

        val voicePlayerList = emptyMap<String, Long>().toMutableMap()

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun addVoicePlayer(name: String, id: Long) {
            event.addVoicePlayer(name, id)
            logger.info("$name with $id added")
            voicePlayerList[name] = id
            setVoiceLevel(id, 0) //mute by default
        }

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun clearVoicePlayerList() {
            event.clearVoicePlayerList()
            logger.info("cleared")
            voicePlayerList.clear()
        }

        val managedPlayer = emptySet<String>().toMutableSet()

        val userVolume = emptyMap<Long, Double>().toMutableMap()
    }
}