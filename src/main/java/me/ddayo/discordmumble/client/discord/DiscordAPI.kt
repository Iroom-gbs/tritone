package me.ddayo.discordmumble.client.discord


class DiscordAPI {
    companion object {
        @JvmStatic
        external fun initialize(): Int

        @JvmStatic
        external fun isMuted(): Boolean

        @JvmStatic
        private external fun setMute()

        @JvmStatic
        private external fun setUnmute()

        @JvmStatic
        external fun isInputMuted(): Boolean

        @JvmStatic
        private external fun setInputMute()

        @JvmStatic
        private external fun setInputUnmute()

        fun inverseMuteStatus() = if(isMuted()) setUnmute() else setMute()

        fun inverseInputMuteStatus() = if(isInputMuted()) setInputUnmute() else setInputMute()

        @JvmStatic
        external fun setVoiceLevel(id: Long, level: Int)

        fun moveVoiceChannelAsync() {

        }
    }
}