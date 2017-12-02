package xyz.jadonfowler.sbhs

import java.awt.ScrollPane
import java.awt.image.BufferedImage
import javax.swing.*

enum class Character(
        val paletteOffset: Int,
        val textOffsets: Pair<Int, Int>,
        val spriteOffset: Int,
        val spriteFrames: List<Int>
) {
    Sonic(0x47AFB8, Pair(0x1DB3FC, 0x1E1467), 0x47AFD8, listOf(8, 4, 8, 4, 8, 4, 4, 4, 8, 4, 8, 8, 8, 8, 16, 12, 12, 8, 12, 8, 8, 16, 8, 12, 8, 8, 4, 8, 4, 4, 8, 8, 4, 8, 4, 8, 4, 4)),
    Knuckles(0x4CADD8, Pair(0x1ED2C4, 0x1F417F), 0x4CADF8, listOf(8, 4, 8, 4, 8, 4, 4, 4, 8, 4, 8, 8, 8, 12, 16, 12, 12, 8, 12, 8, 8, 8, 8, 8, 12, 8, 8, 4, 8, 8, 12, 8, 4, 8, 4, 4, 8, 8, 4, 8, 4, 4, 8, 4, 4)),
    Tails(0x5283F8, Pair(0x1E146A, 0x1E6FA7), 0x528418, listOf(8, 4, 8, 4, 8, 4, 4, 4, 8, 4, 8, 8, 8, 8, 28, 12, 12, 8, 8, 8, 8, 20, 8, 20, 16, 8, 8, 4, 8, 8, 8, 8, 4, 8, 8, 4, 8, 8, 8, 8, 8, 4, 4)),
    Shadow(0x58D818, Pair(0x1FE870, 0x206103), 0x58D838, listOf(8, 4, 28, 12, 8, 4, 4, 8, 8, 8, 4, 8, 8, 8, 12, 24, 16, 20, 8, 4, 8, 12, 12, 8, 24, 8, 12, 8, 4, 8, 4, 4, 8, 8, 12, 4, 4, 4, 4, 4)),
    Rouge(0x5F3E38, Pair(0x1E6FA8, 0x1ED2C3), 0x5F3E58, listOf(8, 4, 8, 4, 8, 4, 8, 4, 8, 4, 8, 12, 16, 12, 8, 8, 12, 8, 4, 12, 8, 4, 8, 4, 4, 8, 8, 12, 4, 4, 4, 4, 4)),
    Amy(0x636458, Pair(0x1F4180, 0x1F9CDB), 0x636478, listOf(8, 4, 8, 4, 8, 4, 4, 4, 4, 4, 4, 4, 8, 8, 8, 16, 16, 8, 8, 12, 12, 8, 8, 8, 12, 8, 4, 8, 8, 8, 8, 8, 4, 4, 8, 4, 4)),
    E102(0x681A78, Pair(-1, -1), 0x681A98, listOf(8, 4, 8, 4, 4, 4, 4, 4, 4, 8, 4, 8, 8, 8, 12, 16, 12, 12, 8, 12, 8, 8, 16, 12, 12, 16, 12, 12, 28, 4, 4, 20, 40, 4, 8, 4, 4, 4, 4, 8, 4, 4, 8, 4, 8, 4, 4)),
    Cream(0x6F6A98, Pair(0x1F9CDC, 0x1FE86F), 0x6F6AB8, listOf(8, 4, 20, 4, 4, 8, 8, 12, 8, 8, 8, 16, 8, 12, 8, 16, 12, 4, 16, 12, 4, 8, 4, 4)),
    Chaos(0x7336B8, Pair(-1, -1), 0x7336D8, listOf(8, 4, 8, 8, 12, 4, 8, 8, 4, 8, 8, 12, 16, 16, 8, 8, 8, 8, 20, 8, 8, 12, 8, 4, 8, 8, 8, 8, 8, 4, 4, 8, 4, 4)),
    Emerl(0x47AB38, Pair(0x206104, 0x20B131), 0x787D18, listOf(-1)),
    Eggman(0x7822D8, Pair(-1, -1), 0x7822F8, listOf(4, 4, 4, 4, 4));

    /**
     * [(offset, frames), (offset, frames)...]
     */
    val spriteData: List<Pair<Int, Int>>

    init {
        spriteData = mutableListOf()
        var o = 0
        spriteFrames.forEach {
            spriteData.add(Pair(spriteOffset + 0x480 * o, it))
            o += it
        }
    }

    fun hasStory(): Boolean = textOffsets.first != -1

    fun readImage(): BufferedImage = SpriteManager.readImage(name, spriteData)

    fun updateSpriteTab() {
        val img = readImage()
        println("Character#updateSpriteTab(): $name#$ordinal ${SBHS.spriteTabs.tabCount}")
        val scrollPane = (SBHS.spriteTabs.getComponentAt(ordinal) as JPanel).getComponent(1) as ScrollPane
        scrollPane.remove(0)
        scrollPane.add(JLabel(ImageIcon(img)))
    }
}
