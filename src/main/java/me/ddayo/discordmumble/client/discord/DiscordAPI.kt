package me.ddayo.discordmumble.client.discord

import org.apache.logging.log4j.LogManager


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
                logger.info(l);
        }

        //IMPORTANT: DO NOT REMOVE THIS FUNCTION. THIS WILL CALL BY RUST!!!!
        @JvmStatic
        fun nativeInitialized() {
            logger.info("Native Initialized")
        }

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

        fun moveVoiceChannelAsync() {

        }
    }
}