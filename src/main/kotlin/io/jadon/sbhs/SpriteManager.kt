package io.jadon.sbhs

import java.awt.Color
import java.awt.ScrollPane
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.BoxLayout
import javax.swing.filechooser.FileNameExtensionFilter

object SpriteManager {

    class SpriteSection(val values: Array<Array<Int>>)

    var SPRITES = HashMap<String, BufferedImage>()

    fun addCharacterSpriteTab(pane: JTabbedPane, name: String, paletteOffset: Int, spriteData: List<Pair<Int, Int>>) {
        pane.addTab(name, null, createSpritePanel(name, readImage(name, spriteData, true), spriteData, paletteOffset, true), "Edit $name Sprite")
    }

    fun addSpriteTab(pane: JTabbedPane, name: String, offset: Int, amount: Int) {
        val data = listOf(Pair(offset, amount))
        pane.addTab(name, null, createSpritePanel(name, readImage(name, data, false, 4), data, -1, false, 4), "Edit $name Sprite")
    }

    fun addSpritePageTab(pane: JTabbedPane, name: String, offset: Int, width: Int, height: Int, leftover: Int = 0) {
        val data = mutableListOf<Pair<Int, Int>>()
        (0 until width).forEach { w ->
            // If we're on the last column, remove the leftover frames
            val l = if (w == width - 1) leftover else 0
            data.add(Pair(offset + (0x200 * w * height), height - l))
        }
        pane.addTab(name, null, createSpritePanel(name, readImage(name, data, false, 4), data, -1, false, 4), "Edit $name Sprite")
    }

    fun createSpritePanel(name: String, initialImage: BufferedImage, spriteData: List<Pair<Int, Int>>, paletteOffset: Int, sortSprites: Boolean, size: Int = 6): JPanel {
        SPRITES[name] = initialImage

        val write = JButton("Write to ROM")
        val save = JButton("Save sprites")
        val upload = JButton("Upload sprites")


        val jp = JPanel()

        val buttons = JPanel()
        buttons.layout = BoxLayout(buttons, BoxLayout.Y_AXIS)
        buttons.add(write)
        buttons.add(save)
        buttons.add(upload)
        jp.add(buttons)

//        Scaling the image
//        val sw = ((SBHS.frame.width - 64) * 0.7).toInt()
//        val sh = (sw * img.height) / img.width
//        val spriteSheet = ImageIcon(scale(img, sw, sh))
        val scrollPane = ScrollPane()
        scrollPane.setBounds(0, 0, (SBHS.frame.width * 0.9).toInt(), (SBHS.frame.height * 0.9).toInt())
        scrollPane.add(JLabel(ImageIcon(initialImage)))

        write.addActionListener {
            writeImage(name, spriteData, paletteOffset, sortSprites, size)
            println("Done writing to $name.")

            // Replace the sprite in the window
            val i = readImage(name, spriteData, sortSprites, size)
            scrollPane.remove(0)
            scrollPane.add(JLabel(ImageIcon(i)))
        }

        save.addActionListener {
            println("Saving Image: $name")
            val i = SPRITES[name]!!
            val fc = JFileChooser()
            fc.fileFilter = FileNameExtensionFilter(".png", "png")
            if (fc.showSaveDialog(SBHS.frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    var o = fc.selectedFile
                    if (o.extension != "png")
                        o = File(o.absolutePath + ".png")
                    ImageIO.write(i, "png", o)
                } catch (x: IOException) {
                    x.printStackTrace()
                }

            }
        }

        upload.addActionListener {
            println("Uploading Image: $name")
            val file = SBHS.getFile(".png", "png")
            val i = ImageIO.read(file)
            SPRITES[name] = i

            // Replace the sprite in the window
            scrollPane.remove(0)
            scrollPane.add(JLabel(ImageIcon(i)))
        }

        jp.add(scrollPane)
        return jp
    }

    // The purples used for the background
    val PURPLE_1 = Color(255, 0, 250)
    val PURPLE_2 = Color(185, 0, 255)
    val PURPLE_3 = Color(185, 0, 185) // no frame

    /**
     * Reads uncompressed sprite from ROM
     *
     * @param spriteData List of animation data, which contain the offset and the frame count
     */
    fun readImage(name: String, spriteData: List<Pair<Int, Int>>, sortSprites: Boolean, size: Int = 6): BufferedImage {
        val maxFrames = spriteData.map { it.second }.max() ?: 8 /*default frame count, though this should never be null*/

        val imgWidth = 8 * size * spriteData.size
        val imgHeight = 8 * size * maxFrames
        val img = BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB)

        val g = img.createGraphics()
        g.color = PURPLE_3
        g.fillRect(0, 0, img.width, img.height)
        g.dispose()

        spriteData.forEachIndexed { animationIndex, animationData ->
            val offset = animationData.first
            var currentFrame = 0
            while (currentFrame < animationData.second) {
                val sections = Array(size * size, { SpriteSection(Array(8, { Array(8, { -1 }) })) })

                run {
                    // variables used for sorting bytes
                    var currentSection = 0
                    var x = 1
                    var y = 1

                    // variables used for retrieving bytes
                    val frameOffset = offset + size * size * currentFrame * 32
                    var i = frameOffset
                    val frameSize = frameOffset + size * size * 32

                    while (i < frameSize) {
                        // Getting the values
                        SBHS.raf.seek(i.toLong())
                        val v = SBHS.raf.read()
                        val v1 = v and 0b00001111
                        val v2 = (v and 0b11110000) shr 4

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
                }

                /**
                0123OP
                4567QR
                89ABST
                CDEFUV
                GHIJWX
                KLMNYZ
                 */
                val sortedSections = if (sortSprites) {
                    arrayOf(
                            sections[0], sections[1], sections[2], sections[3], sections[24], sections[25],
                            sections[4], sections[5], sections[6], sections[7], sections[26], sections[27],
                            sections[8], sections[9], sections[10], sections[11], sections[28], sections[29],
                            sections[12], sections[13], sections[14], sections[15], sections[30], sections[31],
                            sections[16], sections[17], sections[18], sections[19], sections[32], sections[33],
                            sections[20], sections[21], sections[22], sections[23], sections[34], sections[35]
                    )
                } else sections

                var sy = 0
                sortedSections.forEachIndexed { i, spriteSection ->
                    spriteSection.values.forEachIndexed { y, values ->
                        values.forEachIndexed { x, v ->
                            val s: String = PaletteManager.PALETTES[name]?.get(v) ?: "0000"
                            val c: Color = if (v == 0) {
                                if (animationIndex % 2 == 0) {
                                    if (currentFrame % 2 == 0) PURPLE_1
                                    else PURPLE_2
                                } else {
                                    if (currentFrame % 2 == 0) PURPLE_2
                                    else PURPLE_1
                                }
                            } else GBAColor.fromGBA(s)

                            val ix = x + (i % size) * 8 + (8 * size * animationIndex)
                            // TODO: Fix this gross hack
                            val iy = y + sy * 8 + size * (size + (when (size) { 6 -> 2;4 -> 4; else -> 0
                            })) * currentFrame
                            try {
                                img.setRGB(ix, iy, c.rgb)
                            } catch (e: Exception) {
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
        PaletteManager.PALETTES[name]?.forEachIndexed { i, s ->
            val color = GBAColor.fromGBA(s)
            img.setRGB(i, 0, color.rgb)
        }

        return img
    }

    /**
     * Writes uncompressed sprite to ROM
     *
     * @param spriteData List of animation data, which contain the offset and the frame count
     */
    fun writeImage(name: String, spriteData: List<Pair<Int, Int>>, paletteOffset: Int, sortSprites: Boolean, size: Int = 6) {
        val oldImage = SPRITES[name] ?: throw Exception("Null Image for $name")

        // Get the palette from the image
        val palette = (0..15).map { Color(oldImage.getRGB(it, 0)) }
        // Convert the palette to GBA colors and back
        val newPalette = palette.map { GBAColor.fromGBA(GBAColor.toGBA(it)) }
        // Remove the palette so we can write the image
        (1..15).forEach { oldImage.setRGB(it, 0, palette[0].rgb) }

        // Save it to memory
        PaletteManager.PALETTES[name] = palette.map { GBAColor.toGBA(it) }.toTypedArray()

        // Write new palette to rom
        if (paletteOffset > 0) {
            (0..15).forEach {
                val c = GBAColor.toGBA(palette[it])
                val colorParts = listOf("${c[0]}${c[1]}", "${c[2]}${c[3]}")
                val h1 = Integer.parseInt(colorParts[0], 16)
                val h2 = Integer.parseInt(colorParts[1], 16)
                SBHS.raf.seek((paletteOffset + it * 2).toLong())
                SBHS.raf.write(h2)
                SBHS.raf.seek((paletteOffset + it * 2 + 1).toLong())
                SBHS.raf.write(h1)
            }
        }

        val img = convertImageToGBAColors(oldImage, palette, newPalette)

        spriteData.forEachIndexed { animationIndex, animationData ->
            val offset = animationData.first
            println("$name a$animationIndex 0x" + Integer.toHexString(offset))
            val frames = animationData.second
            var currentFrame = 0
            while (currentFrame < frames) {
                val sections = Array(size * size, { SpriteSection(Array(8, { Array(8, { -1 }) })) })

                (0 until size).forEach { sy ->
                    (0 until size).forEach { sx ->
                        (0 until 8).forEach { y ->
                            (0 until 8).forEach { x ->
                                val ix = sx * 8 + x + (8 * size * animationIndex)
                                val iy = currentFrame * size * 8 + sy * 8 + y
                                sections[sx + sy * size].values[y][x] = try {
                                    val color = Color(img.getRGB(ix, iy))
                                    var value = newPalette.indexOf(color)
                                    if (value < 0) {
//                                        System.err.println("Can't find color $color in the palette.")
                                        value = 0
                                    }
                                    value
                                } catch (e: Exception) {
                                    println("i($ix,$iy)/(${img.width},${img.height});")
                                    println("sx * 8 + x = ${sx * 8 + x}")
                                    println("sy * 8 + y = ${sy * 8 + y}")
                                    println("cf: $currentFrame/$frames for $animationIndex sx: $sx sy: $sy size: $size | ${sx + sy * size} -> ($x, $y)")
                                    e.printStackTrace()
                                    System.exit(-1)
                                    0
                                }
                            }
                        }
                    }
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
                val sortedSections = if (sortSprites) {
                    arrayOf(
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
                } else sections

                fun <T> Array<Array<T>>.f(): List<T> {
                    val m = mutableListOf<T>()
                    this.forEach { m.addAll(it) }
                    return m
                }

                val valuesToWrite = mutableListOf<Byte>()
                sortedSections.forEach {
                    val values = it.values.f()
                    val appendedValues = mutableListOf<Byte>()

                    var i = 0
                    while (i < values.size) {
                        val v1 = values[i]
                        val v2 = values[i + 1]
                        val v = (v2 shl 4) or v1
                        appendedValues.add(v.toByte())
                        i += 2
                    }

                    valuesToWrite.addAll(appendedValues)
                }

                val o = offset + (currentFrame * size * size * 32)
                SBHS.raf.seek(o.toLong())
                SBHS.raf.write(valuesToWrite.toByteArray())

                if (name == "Emerl/Chaos") {
                    println(Integer.toHexString(o + valuesToWrite.size))
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
        var x = 0
        var y: Int
        val ww = src.width
        val hh = src.height
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

    fun convertImageToGBAColors(img: BufferedImage, palette: List<Color>, newPalette: List<Color>): BufferedImage {
        // Copy the image so we don't modify the new one
        val newImage = img.copy()
        println("iw${newImage.width}h${newImage.height};")

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
