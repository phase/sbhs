package xyz.jadonfowler.sbhs

import java.awt.Color
import java.awt.Frame
import java.awt.Image
import java.io.File
import java.io.RandomAccessFile
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * @author https://github.com/phase
 */
object SBHS {
    val VERSION = BuildInfo.VERSION
    var gameLocation = ""
    var raf: RandomAccessFile = // Start with null file
            if (System.getProperty("os.name").toLowerCase().contains("win"))
                RandomAccessFile("NUL", "r")
            else RandomAccessFile("/dev/null", "r") // If not windows, assume unix.
    val WIDTH = 1280
    val HEIGHT = 840

    var frame: JFrame = JFrame("Sonic Battle Hack Suite $VERSION - By Phase")
    lateinit var spriteTabs: JTabbedPane

    fun getImage(name: String): Image = ImageIcon(SBHS::class.java.getResource("/$name")
            ?: File("src/main/resources/$name").toURL()).image

    val iconImage = getImage("icon.png")
    val splashImage = getImage("splash.png")

    @JvmStatic
    fun main(args: Array<String>) {
        val timer = System.nanoTime()
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

        val splash = SplashScreen()

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(WIDTH, HEIGHT)
        frame.isResizable = true
        frame.setLocationRelativeTo(null) // Center of screen
        frame.extendedState = Frame.MAXIMIZED_BOTH // Full screen
        frame.iconImage = iconImage

        splash.message = "Config"

        val config = File("config.txt")
        gameLocation = if (config.exists() && config.isFile) config.readText()
        else getFile("GBA ROM", "gba").toString()

        try {
            raf = RandomAccessFile(gameLocation, "rw")
        } catch (e: Exception) {
            e.printStackTrace()
            System.exit(-1)
        }

        val mainTabs = JTabbedPane()
        run {
            // Palette Editor
            val paletteTabs = JTabbedPane()
            Character.values().forEach {
                splash.message = "${it.name} Palette"
                PaletteManager.addPaletteTab(paletteTabs, it.name, it.paletteOffset)
            }
            PaletteData.values().forEach {
                splash.message = "${it.name} Palette"
                PaletteManager.addPaletteTab(paletteTabs, it.name, it.paletteOffset)
            }
            mainTabs.addTab("Palettes", null, paletteTabs, "Palette Editor")
        }
        run {
            val textTabs = JTabbedPane()
            Character.values().filter(Character::hasStory).forEach {
                splash.message = "Text Editor (${it.name})"
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
                if (it == Character.Emerl) return@forEach
                splash.message = "${it.name} Sprites"
                SpriteManager.addCharacterSpriteTab(spriteTabs, it.name, it.paletteOffset, it.spriteData)
            }

            val emerl = Character.Emerl
            val emerlTabs = JTabbedPane()
            var characterByteOffset = 0
            Character.values().forEachIndexed { i: Int, char: Character ->
                if (char == Character.Emerl || char == Character.Eggman) return@forEachIndexed
                splash.message = "Emerl Sprites (${char.name})"
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
            splash.message = "About Page"
            // About Page
            val t = JTextPane()
            t.text = "Sonic Battle Hack Suite $VERSION was made by Phase.\n" +
                    "You can find the source at https://github.com/phase/sbhs\n" +
                    "Current ROM open: $gameLocation\n\n" +
                    "Build Info:\n" +
                    "JVM: ${BuildInfo.JVM_INFO}\n" +
                    "Gradle: ${BuildInfo.GRADLE_INFO}\n"
            t.isEditable = false
            mainTabs.addTab("About", null, t, "About Page")
        }
        frame.contentPane.add(mainTabs)
        splash.message = "nothing"
        splash.isVisible = false
        frame.isVisible = true
        val time = System.nanoTime() - timer
        println("Total time: ${time / 1000000000.0}s")
    }

    fun findFreeSpace(amount: Int): Int {
        var currentOffset = -1
        var a = 0
        try {
            // 00 00 00 01 10 -(read())-> 0 0 0 1 16
            for (i in 0xF9EE30..0x1048559) {
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

    fun getFile(extensionDescription: String, extension: String): File {
        val fc = JFileChooser()
        fc.fileFilter = FileNameExtensionFilter(extensionDescription, extension)
        return if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
            fc.selectedFile else throw Exception("File not found?!")
    }

    fun getColorInput(previousColor: Color): Color {
        val c = JColorChooser.showDialog(frame, "Pick Color", previousColor)
        return c ?: previousColor
    }

    fun hex(i: Int): String {
        return (if (i < 0x10) "0" else "") + Integer.toHexString(i)
    }

}
