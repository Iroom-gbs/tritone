package me.ddayo.tritone.client.util

import net.minecraft.client.Minecraft
import net.minecraft.util.math.vector.Vector3d
import kotlin.math.pow

class MathUtil {
    companion object {
        fun getDistance(pos: Vector3d): Double {
            val p = Minecraft.getInstance().player!!.positionVec
            return ((p.x - pos.x).pow(2.0) + (p.y - pos.y).pow(2) + (p.z - pos.z).pow(2)).pow(0.5)
        }

        private const val PRESET = 4

        fun getVolume(dist: Double): Int {
            return if((dist / PRESET) < 1) 100
            else (10.0).pow(-(dist / PRESET) + 3).toInt()
        }
    }
}