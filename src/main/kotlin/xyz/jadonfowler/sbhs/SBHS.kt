package xyz.jadonfowler.sbhs

import java.awt.*
import java.io.*
import java.net.URL
import javax.swing.*

/**
 * @author https://github.com/phase
 */
object SBHS {
    val VERSION = "1.3"
    var gameLocation = ""
    var raf: RandomAccessFile = // Start with null file
            if (System.getProperty("os.name").toLowerCase().contains("win"))
                RandomAccessFile("NUL", "r")
            else RandomAccessFile("/dev/null", "r") // If not windows, assume unix.
    val WIDTH = 1280
    val HEIGHT = 840

    var frame: JFrame = JFrame("Sonic Battle Hack Suite $VERSION - By Phase")
    lateinit var spriteTabs: JTabbedPane

    @JvmStatic fun main(args: Array<String>) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(WIDTH, HEIGHT)
        frame.isResizable = true
        frame.setLocationRelativeTo(null) // Center of screen
        frame.extendedState = Frame.MAXIMIZED_BOTH // Full screen
        frame.iconImage = ImageIcon(SBHS::class.java.getResource("/icon.png") ?: File("src/resources/icon.png").toURL()).image

        val config = File("config.txt")
        if (config.exists() && config.isFile)
            gameLocation = config.readText()
        else gameLocation = getFile()
        try {
            raf = RandomAccessFile(gameLocation, "rw")
        } catch(e: Exception) {
            e.printStackTrace()
            System.exit(-1)
        }

        val mainTabs = JTabbedPane()
        run {
            // Palette Editor
            val paletteTabs = JTabbedPane()
            Character.values().forEach {
                PaletteManager.addPaletteTab(paletteTabs, it.name, it.paletteOffset)
            }
            PaletteManager.addPaletteTab(paletteTabs, "Fake_Emerl", 0x47AB78)
            PaletteManager.addPaletteTab(paletteTabs, "Dust_Cloud", 0xBF2058)
            PaletteManager.addPaletteTab(paletteTabs, "Sonic's_Mine", 0xBF20D8)
            PaletteManager.addPaletteTab(paletteTabs, "Tail's_Blaster", 0xBF2098)
            PaletteManager.addPaletteTab(paletteTabs, "Shield", 0xBF2078)
            mainTabs.addTab("Palettes", null, paletteTabs, "Palette Editor")
        }
        run {
            val textTabs = JTabbedPane()
            Character.values().filter(Character::hasStory).forEach {
                TextManager.addTextTab(textTabs, it.name, it.textOffsets.first, it.textOffsets.second)
            }
            mainTabs.addTab("Story", null, textTabs, "Story Editor")
        }
        run {
            // Sprite Editor
            spriteTabs = JTabbedPane()
            // TODO: Add this back in
//            SpriteManager.addSpriteTab(spriteTabs, "Ground Shadow", 0x47ABB8, 3)
            Character.values().forEach {
                if (it != Character.Emerl)
                    SpriteManager.addCharacterSpriteTab(spriteTabs, it.name, it.paletteOffset, it.spriteData)
            }

            val emerl = Character.Emerl
            val emerlTabs = JTabbedPane()
            var characterByteOffset = 0
            Character.values().forEachIndexed { i: Int, char: Character ->
                if (char == Character.Emerl || char == Character.Eggman) return@forEachIndexed
                val spriteData = mutableListOf<Pair<Int, Int>>()
                var o = 0
                char.spriteFrames.forEach {
                    // offset the palette data before the sprites
                    val paletteOffset = i * 32
                    spriteData.add(Pair(emerl.spriteOffset + 0x480 * o + paletteOffset + characterByteOffset, it))
                    o += it
                }

                // skip the sprites that have already been drawn
                val frames = char.spriteFrames.sum()
                characterByteOffset += frames * 0x480

                // Add the image to the tabs
                val image = SpriteManager.readImage("Emerl", spriteData)
                emerlTabs.addTab(char.name, null,
                        SpriteManager.createSpritePanel("Emerl/${char.name}", image, spriteData, emerl.paletteOffset),
                        "Edit Emerl/${char.name} Sprite")

            }
            spriteTabs.addTab("Emerl", null, emerlTabs, "Emerl")
            mainTabs.addTab("Sprites", null, spriteTabs, "Sprite Editor")
        }
        run {
            // About Page
            val t = JTextPane()
            t.text = "Sonic Battle Hack Suite $VERSION was made by Phase.\n" +
                    "You can find the source at https://github.com/phase/sbhs\n" +
                    "Current ROM open: $gameLocation"
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

}
