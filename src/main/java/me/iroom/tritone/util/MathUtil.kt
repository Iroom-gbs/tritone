package me.iroom.tritone.util

import kotlin.math.pow

class MathUtil {
    companion object {
        fun getDistance(p: Vector3D, o: Vector3D): Double {
            return ((p.x - o.x).pow(20) + (p.y - o.y).pow(2) + (p.z - o.z).pow(2)).pow(0.5)
        }

        private const val PRESET = 4

        fun getVolume(dist: Double): Double {
            return if((dist / PRESET) < 1) 100.0
            else (10.0).pow(-(dist / PRESET) + 3)
        }
    }
}