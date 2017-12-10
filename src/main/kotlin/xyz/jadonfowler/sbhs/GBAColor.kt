package xyz.jadonfowler.sbhs

import java.awt.Color

object GBAColor {

    val fromCache = mutableMapOf<String, Color>()
    val toCache = mutableMapOf<Color, String>()
    var cacheCalls = 0

    fun fromGBA(hex: String): Color {
        assert(hex.length == 4) { "Woah! This string isn't 4 characters wide!" }

        if (fromCache.containsKey(hex)) {
            return fromCache[hex]!!
        }

        val bits = Integer.toBinaryString(Integer.parseInt(hex, 16)).padStart(16, '0')
        val b = Math.min(255, Integer.parseInt(bits.substring(1..5), 2) * 8 * (24 / 15))
        val g = Math.min(255, Integer.parseInt(bits.substring(6..10), 2) * 8 * (24 / 15))
        val r = Math.min(255, Integer.parseInt(bits.substring(11..15), 2) * 8 * (24 / 15))
        val c = Color(r, g, b)
        fromCache[hex] = c
        return c
    }

    fun toGBA(color: Color): String {
        if (toCache.containsKey(color)) {
            return toCache[color]!!
        }

        val dr = Math.min(31, Math.ceil(color.red / 8.0).toInt())
        val dg = Math.min(31, Math.ceil(color.green / 8.0).toInt())
        val db = Math.min(31, Math.ceil(color.blue / 8.0).toInt())
        val i = Math.min(0x7FFF, (db * 4 * 256) + (dg * 2 * 16) + dr)

        val hex = (if (i < Math.pow(16.0, 3.0)) "0" else "") +
                (if (i < 256) "0" else "") +
                (if (i < 16) "0" else "") +
                Integer.toHexString(i)

        toCache[color] = hex
        return hex
    }

    fun toInts(s: String): IntArray {
        assert(s.length == 4) { "Woah! This string isn't 4 characters wide!" }
        val c = s.toCharArray()
        val i = IntArray(2)
        i[0] = Integer.parseInt(c[0] + "" + c[1], 16)
        i[1] = Integer.parseInt(c[2] + "" + c[3], 16)
        return i
    }
}
