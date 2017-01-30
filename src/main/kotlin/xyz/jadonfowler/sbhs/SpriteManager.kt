package xyz.jadonfowler.sbhs

import java.awt.Color
import java.awt.ScrollPane
import java.awt.image.BufferedImage
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.BoxLayout


class Tuple<out A>(val a: A, val b: A) {
    operator fun get(i: Int): A {
        return when (i) {
            0 -> a
            1 -> b
            else -> throw IllegalArgumentException("$i is not in this Tuple.")
        }
    }
}

object SpriteManager {

    class SpriteSection(val values: Array<Array<Int>>)

    var SPRITES = HashMap<String, BufferedImage>()

    @Throws(Exception::class)
    fun addCharacterSpriteTab(pane: JTabbedPane, name: String, spriteOffset: Int, paletteOffset: Int) {
        val spriteData = listOf(
                // One frame is 0x480
                Tuple(spriteOffset, 8), // Idle 6
                Tuple(spriteOffset + 0x02400, 1), // Jog 1
                Tuple(spriteOffset + 0x03600, 8), // Run 8
                Tuple(spriteOffset + 0x05a00, 4), // Halt 4
                Tuple(spriteOffset + 0x06c00, 8), // Dash 7
                Tuple(spriteOffset + 0x09000, 4), // Turn 3
                Tuple(spriteOffset + 0x0a200, 4), // Change Direction 4
                Tuple(spriteOffset + 0x0b400, 4), // Fall 4
                Tuple(spriteOffset + 0x0c600, 8), // Jump 5
                Tuple(spriteOffset + 0x0ea00, 4), // Land 3
                Tuple(spriteOffset + 0x0fc00, 8), // Double Jump 7
                Tuple(spriteOffset + 0x12000, 8), // Attack 1 6
                Tuple(spriteOffset + 0x14400, 8), // Attack 2 6
                Tuple(spriteOffset + 0x16800, 8), // Attack 3 7
                Tuple(spriteOffset + 0x18c00, 16), // Big Attack 17
                Tuple(spriteOffset + 0x1d400, 12), // Back Attack 9
                Tuple(spriteOffset + 0x20a00, 12),
                Tuple(spriteOffset + 0x24000, 8),
                Tuple(spriteOffset + 0x26400, 12),
                Tuple(spriteOffset + 0x29a00, 8),
                Tuple(spriteOffset + 0x2be00, 8),
                Tuple(spriteOffset + 0x2e200, 16),
                Tuple(spriteOffset + 0x32a00, 8),
                Tuple(spriteOffset + 0x34e00, 12),
                Tuple(spriteOffset + 0x38400, 8),
                Tuple(spriteOffset + 0x3a800, 8),
                Tuple(spriteOffset + 0x3cc00, 4),
                Tuple(spriteOffset + 0x3de00, 8),
                Tuple(spriteOffset + 0x40200, 4),
                Tuple(spriteOffset + 0x41400, 1),
                Tuple(spriteOffset + 0x42600, 8),
                Tuple(spriteOffset + 0x44a00, 8),
                Tuple(spriteOffset + 0x46e00, 12),
                Tuple(spriteOffset + 0x4a400, 4),
                Tuple(spriteOffset + 0x4b600, 8),
                Tuple(spriteOffset + 0x4da00, 1),
                Tuple(spriteOffset + 0x4ec00, 4)
        )
        pane.addTab(name, null, createSpritePanel(name, spriteData, paletteOffset), "Edit $name Sprite")
    }

    @Throws(Exception::class)
    fun addSpriteTab(pane: JTabbedPane, name: String, offset: Int, amount: Int) {
        pane.addTab(name, null, createSpritePanel(name, listOf(Tuple(offset, amount)), -1, 4), "Edit $name Sprite")
    }

    @Throws(Exception::class)
    fun createSpritePanel(name: String, spriteData: List<Tuple<Int>>, paletteOffset: Int, size: Int = 6): JPanel {
        val maxFrames = spriteData.map { it[1] }.max() ?: 6 /*default frame count, though this should never be null*/

        val imgWidth = 8 * size * spriteData.size
        val imgHeight = 8 * size * maxFrames
        val img = BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB)

        val g = img.createGraphics()
        g.color = GBAColor.fromGBA(PaletteManager.PALETTES[name]!![0])
        g.fillRect(0, 0, img.width, img.height)
        g.dispose()

        spriteData.forEachIndexed { animationIndex, animationData ->
            val offset = animationData[0]
            var currentFrame = 0
            while (currentFrame < animationData[1]) {
                val sections = Array(36, { SpriteSection(Array(8, { Array(8, { -1 }) })) })

                var currentSection = 0
                var x = 1
                var y = 1
                val frameOffset = offset + size * size * currentFrame * 32
                var i = frameOffset
                while (i < frameOffset + 36 * 32) {
                    // Getting the values
                    SBHS.raf.seek(i.toLong())
                    val v = SBHS.raf.read()
                    val value = ((if (v < 0x10) "0" else "") + Integer.toString(v, 16)).reversed()
                    val v1 = Integer.parseInt(value[0].toString(), 16)
                    val v2 = Integer.parseInt(value[1].toString(), 16)

                    // Setting the values
                    sections[currentSection].values[y - 1][x - 1] = v1
                    x++
                    sections[currentSection].values[y - 1][x - 1] = v2

                    // Check bounds
                    if (x != 0 && x % 8 == 0) {
                        x -= 8
                        if (y != 0 && y % 8 == 0) {
                            x = 0
                            y = 0
                            currentSection++
                        }
                        y++
                    }
                    x++
                    i++
                }

                /**
                0123OP
                4567QR
                89ABST
                CDEFUV
                GHIJWX
                KLMNYZ
                 */
                val sortedSections = arrayOf(
                        sections[0], sections[1], sections[2], sections[3], sections[24], sections[25],
                        sections[4], sections[5], sections[6], sections[7], sections[26], sections[27],
                        sections[8], sections[9], sections[10], sections[11], sections[28], sections[29],
                        sections[12], sections[13], sections[14], sections[15], sections[30], sections[31],
                        sections[16], sections[17], sections[18], sections[19], sections[32], sections[33],
                        sections[20], sections[21], sections[22], sections[23], sections[34], sections[35]
                )

                var sy = 0
                sortedSections.forEachIndexed { i, spriteSection ->
                    spriteSection.values.forEachIndexed { y, values ->
                        values.forEachIndexed { x, v ->
                            var s: String? = PaletteManager.PALETTES[name]!![v]
                            if (s == null) s = "0000"
                            val c = GBAColor.fromGBA(s)
                            val ix = x + (i % size) * 8 + (8 * size * animationIndex)
                            val iy = y + sy * 8 + size * (size + 2) * currentFrame
                            try {
                                img.setRGB(ix, iy, c.rgb)
                            } catch(e: Exception) {
//                                println("error @ ($ix, $iy) / (${img.width}, ${img.height})")
//                                e.printStackTrace()
//                                System.exit(1)
                            }
                        }

                    }
                    if (i != 0 && (i + 1) % size == 0) sy++
                }
                currentFrame++
            }
        }

        // Write palette to upper left corner
        PaletteManager.PALETTES[name]!!.forEachIndexed { i, s ->
            val color = GBAColor.fromGBA(s)
            img.setRGB(i, 0, color.rgb)
        }

        SPRITES[name] = img
        val write = JButton("Write to ROM")
        write.addActionListener { writeImage(name, spriteData, paletteOffset); println("Done writing to $name.") }
        val save = JButton("Save sprites")
        save.addActionListener {
            println("Saving Image: $name")
            val i = SPRITES[name]!!
            val fc = JFileChooser()
            if (fc.showSaveDialog(SBHS.frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    val o = fc.selectedFile
                    ImageIO.write(i, "png", o)
                } catch (x: IOException) {
                    x.printStackTrace()
                }

            }
        }
        val upload = JButton("Upload sprites")
        upload.addActionListener {
            println("Uploading Image: $name")
            val fc = JFileChooser()
            if (fc.showOpenDialog(SBHS.frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    val i = ImageIO.read(fc.selectedFile)
                    SPRITES[name] = i
                } catch (x: IOException) {
                    x.printStackTrace()
                }

            }
        }
        val jp = JPanel()
        val sw = ((SBHS.frame.width - 64) * 0.7).toInt()
        val sh = (sw * img.height) / img.width
//        val spriteSheet = ImageIcon(scale(img, sw, sh))
        val spriteSheet = ImageIcon(img)

        val buttons = JPanel()
        buttons.layout = BoxLayout(buttons, BoxLayout.Y_AXIS)
        buttons.add(write)
        buttons.add(save)
        buttons.add(upload)
        jp.add(buttons)

        val spriteLabel = JLabel(spriteSheet)
        val scrollPane = ScrollPane()
        scrollPane.setBounds(0, 0, (SBHS.frame.width * 0.9).toInt(), (SBHS.frame.height * 0.9).toInt())
        scrollPane.add(spriteLabel)
        jp.add(scrollPane)
        return jp
    }

    /**
     * Writes uncompressed sprite to ROM
     *
     * @param spriteData List of animation data, which contain the offset and the frame count
     */
    fun writeImage(name: String, spriteData: List<Tuple<Int>>, paletteOffset: Int, size: Int = 6) {
        val oldImage = SPRITES[name] ?: throw Exception("Null Image for $name")

        // Get the palette from the image
        val palette = (0..15).map { Color(oldImage.getRGB(it, 0)) }
        // Remove the palette so we can write the image
        (1..15).forEach { oldImage.setRGB(it, 0, palette[0].rgb) }

        // Write new palette to rom
        (0..15).forEach {
            val c = GBAColor.toGBA(palette[it])
            val h1 = Integer.parseInt(c.split("(?<=\\G.{2})".toRegex()).dropLastWhile(String::isEmpty)[0], 16)
            val h2 = Integer.parseInt(c.split("(?<=\\G.{2})".toRegex()).dropLastWhile(String::isEmpty)[1], 16)
            SBHS.raf.seek((paletteOffset + it * 2).toLong())
            SBHS.raf.write(h2)
            SBHS.raf.seek((paletteOffset + it * 2 + 1).toLong())
            SBHS.raf.write(h1)
        }

        val img = convertImageToGBAColors(oldImage, palette)

        spriteData.forEachIndexed { animationIndex, animationData ->
            val offset = animationData[0]
            println("0x" + Integer.toHexString(offset))
            val frames = animationData[1]
            var currentFrame = 0
            while (currentFrame < frames) {
                val sections = Array(size * size, { SpriteSection(Array(8, { Array(8, { -1 }) })) })

                var sy = 0
                while (sy < size) {
                    var sx = 0
                    while (sx < size) {
                        var y = 0
                        while (y < 8) {
                            var x = 0
                            while (x < 8) {
                                val ix = sx * 8 + x + (8 * size * animationIndex)
                                val iy = currentFrame * size * 8 + sy * 8 + y
                                sections[sx + sy * size].values[y][x] = try {
                                    val color = Color(img.getRGB(ix, iy))
                                    var value = palette.indexOf(color)
                                    if (value < 0) {
                                        System.err.println("Can't find color $color in the palette.")
                                        value = 0
                                    }
                                    value
                                } catch(e: Exception) {
                                    print("i($ix,$iy)/(${img.width},${img.height});")
                                    println("sx * 8 + x = ${sx * 8 + x}")
                                    println("sy * 8 + y = ${sy * 8 + y}")
                                    println("cf: $currentFrame/$frames for $animationIndex sx: $sx sy: $sy size: $size | ${sx + sy * size} -> ($x, $y)")
                                    e.printStackTrace()
                                    System.exit(-1)
                                    0
                                }
                                x++
                            }
                            y++
                        }
                        sx++
                    }
                    sy++
                }

                /**
                0123OP  012345  00 01 02 03  04 05
                4567QR  6789AB  06 07 08 09  10 11
                89ABST  CDEFGH  12 13 14 15  16 17
                CDEFUV  IJKLMN  18 19 20 21  22 23
                GHIJWX  OPQRST  24 25 26 27  28 29
                KLMNYZ  UVWXYZ  30 31 32 33  34 35

                0, 1, 2, 3,
                6, 7, 8, 9,
                12, 13, 14, 15,
                18, 19, 20, 21,
                24, 25, 26, 27,
                30, 31, 32, 33,
                4, 5, 10, 11,
                16, 17, 22, 23,
                28, 29, 34, 35
                 */
                val sortedSections = arrayOf(
                        sections[0], sections[1], sections[2], sections[3],
                        sections[6], sections[7], sections[8], sections[9],
                        sections[12], sections[13], sections[14], sections[15],
                        sections[18], sections[19], sections[20], sections[21],
                        sections[24], sections[25], sections[26], sections[27],
                        sections[30], sections[31], sections[32], sections[33],
                        sections[4], sections[5], sections[10], sections[11],
                        sections[16], sections[17], sections[22], sections[23],
                        sections[28], sections[29], sections[34], sections[35]
                )

                fun <T> Array<Array<T>>.f(): List<T> {
                    val m = mutableListOf<T>()
                    this.forEach { m.addAll(it) }
                    return m
                }

                val valuesToWrite = mutableListOf<Int>()
                sortedSections.forEachIndexed { s, spriteSection ->
                    val values = spriteSection.values.f().map { Integer.toHexString(it) }
                    val appendedValues = mutableListOf<Int>()

                    var i = 0
                    while (i < values.size) {
                        val v1 = values[i]
                        val v2 = values[i + 1]
                        val v = Integer.parseInt(v2 + v1, 16)
                        appendedValues.add(v)
                        i += 2
                    }

                    valuesToWrite.addAll(appendedValues)
                }

                valuesToWrite.forEachIndexed { i, value ->
                    val o = offset + (currentFrame * size * size * 32) + i.toLong()
                    SBHS.raf.seek(o)
                    SBHS.raf.write(value)
                }

                currentFrame++
            }
        }
    }

    /**
     * Scales image to new size
     * @param src Image to scale
     * *
     * @param w Width of new image
     * *
     * @param h Height of new image
     * *
     * @return Scaled image
     */
    fun scale(src: BufferedImage, w: Int, h: Int): BufferedImage {
        val img = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
        var x: Int
        var y: Int
        val ww = src.width
        val hh = src.height
        x = 0
        while (x < w) {
            y = 0
            while (y < h) {
                val col = src.getRGB(x * ww / w, y * hh / h)
                img.setRGB(x, y, col)
                y++
            }
            x++
        }
        return img
    }

    fun BufferedImage.copy(): BufferedImage {
        val cm = this.colorModel
        val raster = this.copyData(this.raster.createCompatibleWritableRaster())
        return BufferedImage(cm, raster, cm.isAlphaPremultiplied, null)
    }

    // TODO: This function is poorly named
    fun BufferedImage.changeColors(l: (Color) -> Color) {
        var y = 0
        while (y < this.height) {
            var x = 0
            while (x < this.width) {
                this.setRGB(x, y, l(Color(this.getRGB(x, y))).rgb)
                x++
            }
            y++
        }
    }

    fun convertImageToGBAColors(img: BufferedImage, palette: List<Color>): BufferedImage {
        // Copy the image so we don't modify the new one
        val newImage = img.copy()
        println("iw${newImage.width}h${newImage.height};")

        // Convert the palette to GBA colors and back
        val newPalette = palette.map { GBAColor.fromGBA(GBAColor.toGBA(it)) }

        // Replace the colors that need to be replaced
        newImage.changeColors {
            if (palette.contains(it)) {
                val index = palette.indexOf(it)
                newPalette[index]
            } else it
        }

        return newImage
    }

    fun printPalette(s: Array<String>) {
        print("[")
        for (i in s.indices) {
            print(s[i] + "(")
            print(GBAColor.fromGBA(s[i]).toString() + ")")
            if (i < s.size - 1) print(", ")
        }
        println("]")
    }

    fun getIndexFromPalette(name: String, color: Color): Int {
        val p = ArrayList(Arrays.asList(*PaletteManager.PALETTES[name]!!))
        for (s in p) {
            val i = p.indexOf(s)
            val pc = GBAColor.fromGBA(s)
            if (pc == color) return i
        }
        print("Error finding color $color ")
        printPalette(PaletteManager.PALETTES[name]!!)
        return 0
    }
}