package me.ddayo.discordmumble.client.discord

import org.apache.logging.log4j.LogManager


class DiscordAPI {
    companion object {
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

        @JvmStatic
        fun testCallback(l: List<String>) {
            
        }

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