package xyz.jadonfowler.sbhs

import java.awt.Color

object GBAColor {
    fun fromGBA(hex: String): Color {
        assert(hex.length == 4) { "Woah! This string isn't 4 characters wide!" }
        var h = Integer.parseInt(hex, 16)
        var g = 0
        var b = 0
        while (h - 4 * 256 >= 0) {
            b += 8
            h -= 4 * 256
        }
        while (h - 32 >= 0) {
            g += 8
            h -= 32
        }
        var r = h * 8
        val t = 255
        r = if (r > t) t else r
        g = if (g > t) t else g
        b = if (b > t) t else b
        val c = Color(r, g, b)
        return c
    }

    fun toGBA(r: Int, g: Int, b: Int): String {
        val dr = r / 8
        val dg = g * 4
        val db = b * 128
        val i = dr + dg + db
        return (if (i < Math.pow(16.0, 3.0)) "0" else "") +
                (if (i < 256) "0" else "") +
                (if (i < 16) "0" else "") +
                Integer.toHexString(i)
    }

    fun toGBA(c: Color): String {
        return toGBA(c.red, c.green, c.blue)
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