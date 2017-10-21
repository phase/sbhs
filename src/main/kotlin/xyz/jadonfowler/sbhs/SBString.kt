package xyz.jadonfowler.sbhs

import java.util.*

object SBString {
    val TABLE: HashMap<Char, Int> = object : HashMap<Char, Int>() {
        init {
            put('!', 0x01)
            put('"', 0x02)
            put('#', 0x03)
            put('$', 0x04)
            put('%', 0x05)
            put('&', 0x06)
            put('\'', 0x07)
            put('(', 0x08)
            put(')', 0x09)
            put('*', 0x0A)
            put('+', 0x0B)
            put(',', 0x0C)
            put('-', 0x0D)
            put('.', 0x0E)
            put('/', 0x0F)
            put('0', 0x10)
            put('1', 0x11)
            put('2', 0x12)
            put('3', 0x13)
            put('4', 0x14)
            put('5', 0x15)
            put('6', 0x16)
            put('7', 0x17)
            put('8', 0x18)
            put('9', 0x19)
            put(':', 0x1A)
            put(';', 0x1B)
            put('<', 0x1C)
            put('=', 0x1D)
            put('>', 0x1E)
            put('?', 0x1F)
            put('@', 0x20)
            put('A', 0x21)
            put('B', 0x22)
            put('C', 0x23)
            put('D', 0x24)
            put('E', 0x25)
            put('F', 0x26)
            put('G', 0x27)
            put('H', 0x28)
            put('I', 0x29)
            put('J', 0x2A)
            put('K', 0x2B)
            put('L', 0x2C)
            put('M', 0x2D)
            put('N', 0x2E)
            put('O', 0x2F)
            put('P', 0x30)
            put('Q', 0x31)
            put('R', 0x32)
            put('S', 0x33)
            put('T', 0x34)
            put('U', 0x35)
            put('V', 0x36)
            put('W', 0x37)
            put('X', 0x38)
            put('Y', 0x39)
            put('Z', 0x3A)
            put('a', 0x41)
            put('b', 0x42)
            put('c', 0x43)
            put('d', 0x44)
            put('e', 0x45)
            put('f', 0x46)
            put('g', 0x47)
            put('h', 0x48)
            put('i', 0x49)
            put('j', 0x4A)
            put('k', 0x4B)
            put('l', 0x4C)
            put('m', 0x4D)
            put('n', 0x4E)
            put('o', 0x4F)
            put('p', 0x50)
            put('q', 0x51)
            put('r', 0x52)
            put('s', 0x53)
            put('t', 0x54)
            put('u', 0x55)
            put('v', 0x56)
            put('w', 0x57)
            put('x', 0x58)
            put('y', 0x59)
            put('z', 0x5A)
            put(' ', 0x00)
            // Special Stuff
            put('^', 0xFF)
            put('[', 0xFD)
            put(']', 0xFE)
            put('{', 0xFB)
        }
    }
    val REVERSE_TABLE: HashMap<Int, Char> = object : HashMap<Int, Char>() {
        init {
            for (c in TABLE.keys)
                put(TABLE[c] ?: 0, c)
        }
    }

    fun to(s: String): IntArray {
        var s = s
        s = s.replace("\n------\n", "]^")
        s = s.replace("\n", "[^")
        println(s)
        val to = IntArray(s.length * 2 - 1)
        var i = 0
        var k = 0
        val chars = s.toCharArray()
        while (k < chars.size) {
            val c = chars[k++]
            if (!TABLE.keys.contains(c)) {
                continue
            }
            val b = TABLE[c]!!
            to[i] = b
            try { // Should fail for the last one
                if (b == 0xFB || b == 0xFD || b == 0xFE || b == 0xFF) {
                    i--
                } else {
                    to[i + 1] = 0 // 0x00
                }
            } catch (e: Exception) {
            }

            i += 2
        }
        println("Produced: ${to.map { Integer.toHexString(it).toUpperCase() }.joinToString(" ")}")
        return to
    }

    fun from(t: IntArray): String {
        var s = ""
        var last = -1
        for (i in t) {
            if (i == 0 && last == 0) {
                s += " "
                last = -1
                continue
            } else if (i == 0) {
                last = 0
                continue
            } else if (i == 0xFF) {
                when (last) {
                    0xFE -> s += "\n------\n"
                    0xFD -> s += "\n"
                    else -> {
                    }
                }
            } else {
                if (REVERSE_TABLE.containsKey(i)) {
                    s += REVERSE_TABLE[i]
                    last = -1
                    continue
                }
            }
            last = i
        }
        return s.replace("]]", "[").replace("]", "\n------\n").replace("[", "\n").replace("\\s\\s\\s".toRegex(), "")
    }

    fun convert(last: Int, i: Int): String {
        if (i == 0 && last == 0) {
            return " "
        } else if (i == 0xFF) {
            when (last) {
                0xFE -> return "\n------\n"
                0xFD -> return "\n"
                else -> {
                }
            }
        } else {
            if (REVERSE_TABLE.containsKey(i)) return REVERSE_TABLE[i]!! + ""
        }
        return ""
    }
}
