package xyz.jadonfowler.sbhs

import java.awt.*
import java.io.*
import javax.swing.*

/**
 * @author https://github.com/phase
 */
object SBHS {
    val VERSION = "1.2-SNAPSHOT"
    var gameLocation = ""
    var raf: RandomAccessFile = RandomAccessFile("/dev/null", "r")
    var frame: JFrame = JFrame("Sonic Battle Hack Suite $VERSION - By Phase")
    val WIDTH = 1280
    val HEIGHT = 840

    @Throws(Exception::class)
    @JvmStatic fun main(args: Array<String>) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(WIDTH, HEIGHT)
        frame.isResizable = true
        frame.setLocationRelativeTo(null) // Center of screen
        frame.extendedState = Frame.MAXIMIZED_BOTH // Full screen
        frame.iconImage = ImageIcon(SBHS::class.java.getResource("/icon.png")).getImage()

        val config = File("config.txt")
        if (config.exists() && config.isFile)
            gameLocation = config.readText()
        else gameLocation = getFile()
        raf = RandomAccessFile(gameLocation, "rw")

        val mainTabs = JTabbedPane()
        run {
            // Palette Editor
            val paletteTabs = JTabbedPane()
            PaletteManager.addPaletteTab(paletteTabs, "Sonic", 0x47AFB8)
            PaletteManager.addPaletteTab(paletteTabs, "Tails", 0x5283F8)
            PaletteManager.addPaletteTab(paletteTabs, "Knuckles", 0x4CADD8)
            PaletteManager.addPaletteTab(paletteTabs, "Amy", 0x636458)
            PaletteManager.addPaletteTab(paletteTabs, "Shadow", 0x58D818)
            PaletteManager.addPaletteTab(paletteTabs, "Rouge", 0x5F3E38)
            PaletteManager.addPaletteTab(paletteTabs, "E-102", 0x681A78)
            PaletteManager.addPaletteTab(paletteTabs, "Chaos", 0x7336B8)
            PaletteManager.addPaletteTab(paletteTabs, "Emerl", 0x787CFA)
            PaletteManager.addPaletteTab(paletteTabs, "Fake_Emerl", 0x47AB78)
            PaletteManager.addPaletteTab(paletteTabs, "Eggman", 0x7822D8)
            PaletteManager.addPaletteTab(paletteTabs, "Dust_Cloud", 0xBF2058)
            PaletteManager.addPaletteTab(paletteTabs, "Sonic's_Mine", 0xBF20D8)
            PaletteManager.addPaletteTab(paletteTabs, "Tail's_Blaster", 0xBF2098)
            PaletteManager.addPaletteTab(paletteTabs, "Shield", 0xBF2078)
            mainTabs.addTab("Palette Editor", null, paletteTabs, "Palette Editor")
        }
        run {
            // TODO Text editor
            val textTabs = JTabbedPane()
            TextManager.addTextTab(textTabs, "Sonic", 0x1DB3FC, 0x1E1467)
            TextManager.addTextTab(textTabs, "Tails", 0x1E146A, 0x1E6FA7)
            TextManager.addTextTab(textTabs, "Rouge", 0x1E6FA8, 0x1ED2C3)
            TextManager.addTextTab(textTabs, "Knuckles", 0x1ED2C4, 0x1F417F)
            TextManager.addTextTab(textTabs, "Amy", 0x1F4180, 0x1F9CDB)
            TextManager.addTextTab(textTabs, "Cream", 0x1F9CDC, 0x1FE86F)
            TextManager.addTextTab(textTabs, "Shadow", 0x1FE870, 0x206103)
            TextManager.addTextTab(textTabs, "Emerl", 0x206104, 0x20B131)
            mainTabs.addTab("Text Editor", null, textTabs, "Text Editor")
        }
        run {
            // Sprite Editor
            val spriteTabs = JTabbedPane()
            SpriteManager.addSpriteTab(spriteTabs, "Ground Shadow", 0x47ABB8, 3)
            SpriteManager.addCharacterSpriteTab(spriteTabs, "Sonic", 0x47B078)
            mainTabs.addTab("Sprite Editor", null, spriteTabs, "Sprite Editor")
        }
        run {
            // About Page
            val t = JTextPane()
            t.text = "Sonic Battle Hack Suite $VERSION was made by Phase.\n" +
                    "You can find the source at https://github.com/phase/sbhs"
            t.isEditable = false
            mainTabs.addTab("About", null, t, "About Page")
        }
        frame.contentPane.add(mainTabs)
        frame.isVisible = true
        // frame.pack();
    }

    fun findFreeSpace(amount: Int): Int {
        var currentOffset = -1
        var a = 0
        try {
            // 00 00 00 01 10 -(read())-> 0 0 0 1 16
            for (i in 0xF9EE30..1048559) {
                raf.seek(i.toLong())
                val value = raf.read()
                if (value != 0) {
                    currentOffset = -1
                    a = 0
                    continue
                }
                if (a == amount && currentOffset != -1) return currentOffset
                if (currentOffset == -1 && value == 0)
                    currentOffset = i
                else
                    a++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return -1
    }

    fun getFile(): String {
        val fc = JFileChooser()
        return if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
            fc.selectedFile.toString() else ""
    }

    fun getColorInput(previousColor: Color): Color {
        val c = JColorChooser.showDialog(frame, "Pick Color", previousColor)
        return c ?: previousColor
    }

    fun hex(i: Int): String {
        return (if (i < 0x10) "0" else "") + Integer.toHexString(i)
    }

    fun spaceout(s: String): String {
        var f = ""
        for (c in s.toCharArray())
            f += c + " "
        return f
    }

    fun join(a: Array<String>): String {
        /*
         * String f = null; for (int i = 0; i < a.length; i++) { try{ String h =
         * a[i], j = a[++i], k = a[++i], l = a[++i]; String g = join(join(h, j),
         * join(k, l)); f = (f == null ? g : join(f, g)) + "\n";
         * System.out.println(f+"\nn"); }catch(Exception e){ //less than 4
         * elemnts left } }
         */
        var l: String? = null
        var f: String? = null
        for (s in a) {
            if (l == null) {
                l = s
                continue
            }
            if (f == null) {
                f = join(l, s)
                l = null
                continue
            }
            val k = join(l, s)
            l = null
            f = join(f, k)
        }
        return f ?: ""
    }

    fun join(a: String, b: String): String {
        var f = ""
        val aa = a.split("\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val ba = b.split("\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        for (i in aa.indices)
            f += aa[i] + ba[i] + "\n"
        return f
    }

    fun getChar(d: String, x: Int, y: Int): Char {
        val e = d.split("\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[y]
        return e.toCharArray()[x]
    }

    fun atosHex(i: IntArray): String {
        var f = "["
        for (j in i) {
            f += Integer.toHexString(j) + ", "
        }
        f += "]"
        return f
    }
}