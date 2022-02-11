package me.ddayo.discordmumble.client.key

import net.minecraft.client.settings.KeyBinding

class OptionKey(key: String, keyCode: Int, group: String): KeyBinding(key, keyCode, group) {
    var current = false
    override fun isKeyDown(): Boolean {
        if(super.isKeyDown() && !current) {
            current = true
            return true
        }
        else if(!super.isKeyDown())
            current = false
        return false
    }
}