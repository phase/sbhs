package xyz.jadonfowler.sbhs

import java.awt.*
import java.awt.image.*
import java.io.*
import java.util.*
import javax.imageio.*
import javax.swing.*
import javax.swing.BoxLayout


object SpriteManager {
    var SPRITES = HashMap<String, HashMap<String, BufferedImage>>()

    @Throws(Exception::class)
    fun addCharacterSpriteTab(pane: JTabbedPane, name: String, offset: Int) {
        //@formatter:off
        SPRITES.put(name, HashMap<String, BufferedImage>())
        val t = JTabbedPane()
        t.addTab("Idle", null, createSpritePanel(name, "Idle", //6
                offset, 32 * 7 - 8),
                "Edit $name Idle Sprite")
        t.addTab("Jog", null, createSpritePanel(name, "Jog", //1
                offset + 64 * 2 * 64 + 64 * 16, 32),
                "Edit $name Jog Sprite")
        t.addTab("Run", null, createSpritePanel(name, "Run", //8
                offset + 64 * 3 * 64 + 64 * 24, 32 * 9),
                "Edit $name Run Sprite")
        t.addTab("Halt", null, createSpritePanel(name, "Halt", //4
                offset + 64 * 5 * 64 + 64 * 40, 32 * 4 + 16),
                "Edit $name Halt Sprite")
        t.addTab("Dash", null, createSpritePanel(name, "Dash", //7
                offset + 64 * 6 * 64 + 64 * 48, 32 * 8),
                "Edit $name Dash Sprite")
        t.addTab("Turn", null, createSpritePanel(name, "Turn", //3
                offset + 64 * 9 * 64, 32 * 4 - 16),
                "Edit $name Turn Sprite")
        t.addTab("Change Direction", null, createSpritePanel(name, "Change Direction", //4
                offset + 64 * 10 * 64 + 64 * 8, 32 * 5 - 16),
                "Edit $name Change Direction Sprite")
        t.addTab("Fall", null, createSpritePanel(name, "Fall", //4
                offset + 64 * 11 * 64 + 64 * 16, 32 * 5 - 16),
                "Edit $name Fall Sprite")
        t.addTab("Jump", null, createSpritePanel(name, "Jump", //5
                offset + 64 * 12 * 64 + 64 * 24, 32 * 6 - 16),
                "Edit $name Jump Sprite")
        t.addTab("Land", null, createSpritePanel(name, "Land",
                offset + 64 * 14 * 64 + 64 * 38, 32 * 4 - 16), //3
                "Edit $name Land Sprite")
        t.addTab("Double Jump", null, createSpritePanel(name, "Double Jump",
                offset + 64 * 15 * 64 + 64 * 48, 32 * 8), //7
                "Edit $name Double Jump")
        // These are the normal `B` attacks
        t.addTab("Attack 1", null, createSpritePanel(name, "Attack 1",
                offset + 64 * 18 * 64, 32 * 7 - 8), //6
                "Edit $name Attack 1")
        t.addTab("Attack 2", null, createSpritePanel(name, "Attack 2",
                offset + 64 * 20 * 64 + 64 * 16, 32 * 7 - 8), //6
                "Edit $name Attack 2")
        t.addTab("Attack 3", null, createSpritePanel(name, "Attack 3",
                offset + 64 * 22 * 64 + 64 * 32, 32 * 8 - 4), //7
                "Edit $name Attack 3")
        t.addTab("Big Attack", null, createSpritePanel(name, "Big Attack",
                /* This one is a little weird */
                offset + 64 * 24 * 64 + 64 * 28, 32 * 19 + 8), //17
                "Edit $name Big Attack")
        // When you press the direction away from where you're pointing and B
        t.addTab("Back Attack", null, createSpritePanel(name, "Back Attack",
                offset + 64 * 29 * 64 + 64 * 16, 32 * 10), //9
                "Edit $name Back Attack")
        pane.addTab(name, null, t, "Edit $name Sprite")
        //@formatter:on
    }

    @Throws(Exception::class)
    fun addSpriteTab(pane: JTabbedPane, name: String, offset: Int, amount: Int) {
        SPRITES.put(name, HashMap<String, BufferedImage>())
        pane.addTab(name, null, createSpritePanel(name, "Idle", offset, amount), "Edit $name Sprite")
    }

    @Throws(Exception::class)
    fun createSpritePanel(name: String, state: String, offset: Int, amount: Int): JPanel {
        val img = BufferedImage(8 * 4, 8 * Math.ceil((amount / 4).toDouble()).toInt(), BufferedImage.TYPE_INT_RGB)
        val g = img.createGraphics()
        g.color = Color.RED
        g.fillRect(0, 0, img.width, img.height)
        g.dispose()
        var x = 1
        var y = 1
        var s = 0
        run {
            var i = offset
            val size = 4
            val rows = 8
            while (i < offset + size * rows * amount) {
                SBHS.raf.seek(i.toLong())
                val v = SBHS.raf.read()
                val value = SBHS.reverse((if (v < 0x10) "0" else "") + Integer.toString(v, 16))
                var o1 = Color.black
                var o2 = Color.white
                //System.out.print(value + " : ");
                val v1 = Integer.parseInt(value.toCharArray()[0] + "", 16)
                val v2 = Integer.parseInt(value.toCharArray()[1] + "", 16)
                //System.out.println(v1 + " " + v2);
                try {
                    //printPalette(PaletteManager.PALETTES.get(name));
                    var s1: String? = PaletteManager.PALETTES[name]!![v1]
                    if (s1 == null) s1 = "0000"
                    o1 = GBAColor.fromGBA(s1)
                    var s2: String? = PaletteManager.PALETTES[name]!![v2]
                    if (s2 == null) s2 = "0000"
                    o2 = GBAColor.fromGBA(s2)
                } catch (e: Exception) {
                    e.printStackTrace()
                    System.exit(-1)
                }

                val col1 = o1.rgb
                val col2 = o2.rgb
                // if (v1 == 0) System.out.println(o1);
                // System.out.println(
                // " S: " + s + " X: " + x + " Y: " + y + " MaxX: " + img.getWidth()
                // + " MaxY: " + img.getHeight());
                try {
                    img.setRGB(x - 1, y - 1, col1)
                } catch (e: Exception) {
                    println("ERROR @ " + x + " " + y + " / " + img.width + " " + img.height)
                    e.printStackTrace()
                    System.exit(-1)
                }

                x++
                // System.out.println(
                // " S: " + s + " X: " + x + " Y: " + y + " MaxX: " + img.getWidth()
                // + " MaxY: " + img.getHeight());
                try {
                    img.setRGB(x - 1, y - 1, col2)
                } catch (e: Exception) {
                    println("ERROR @ " + x + " " + y + " / " + img.width + " " + img.height)
                    e.printStackTrace()
                    System.exit(-1)
                }

                if (x != 0 && x % 8 == 0) {
                    x -= 8
                    if (y != 0 && y % 8 == 0) {
                        y -= 8
                        x += 8
                        s++
                        if (s == 4) {
                            s = 0
                            x = 0
                            y += 8
                        }
                    }
                    y++
                }
                x++
                i++
            }
        }
        SPRITES[name]?.put(state, img)
        val write = JButton("Write to ROM")
        write.addActionListener { writeImage(name, state, offset, amount) }
        val save = JButton("Save sprites")
        save.addActionListener {
            println("Saving image $name: $state")
            val i = SPRITES[name]?.get(state)
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
            println("Uploading image $name: $state")
            val fc = JFileChooser()
            if (fc.showOpenDialog(SBHS.frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    val i = ImageIO.read(fc.selectedFile)
                    SPRITES[name]?.put(state, i)
                } catch (x: IOException) {
                    x.printStackTrace()
                }

            }
        }
        val jp = JPanel()
        val sw = ((SBHS.frame.width - 64) * 0.5).toInt()
        val sh = (sw * img.height) / img.width
        val spriteSheet = ImageIcon(scale(img, sw, sh))

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
     * @param img Image to write
     * *
     * @param offset Offset of image in ROM
     * *
     * @param amount Amount of 8x8 squares
     */
    fun writeImage(name: String, state: String, offset: Int, amount: Int) {
        val img = SPRITES[name]?.get(state)
        printPalette(PaletteManager.PALETTES[name]!!)
        var x = 1
        var y = 1
        var s = 0
        var i = offset
        val size = 4
        val rows = 8
        while (i < offset + size * rows * amount) {
            val c1 = getColorFromImage(img!!, x - 1, y - 1)
            // This gets the index of the color in the palette,
            // which is the value we need to write to the ROM
            //System.out.println("Palette contains " + c1 + ": " + Arrays.asList(PaletteManager.PALETTES.get(name)).contains(c1));
            val v1 = getIndexFromPalette(name, c1)
            x++
            val c2 = getColorFromImage(img, x - 1, y - 1)
            if (x != 0 && x % 8 == 0) {
                x -= 8
                if (y != 0 && y % 8 == 0) {
                    y -= 8
                    x += 8
                    s++
                    if (s == 4) {
                        s = 0
                        x = 0
                        y += 8
                    }
                }
                y++
            }
            x++
            //System.out.println("Palette contains " + c2 + ": " + Arrays.asList(PaletteManager.PALETTES.get(name)).contains(c2));
            val v2 = getIndexFromPalette(name, c2)
            // Writing to ROM
            //            if (v1 < 0) v1 = 0;
            //            if (v2 < 0) v2 = 0;
            val v = Integer.parseInt(Integer.toString(v2, 16) + "" + Integer.toString(v1, 16), 16)
            try {
                SBHS.raf.seek(i.toLong())
                SBHS.raf.write(v)
                println("Colors: " + c1 + " " + c2 + " Wrote: " + Integer.toString(v, 16) + " @ 0x"
                        + Integer.toString(i, 16))
            } catch (e: IOException) {
                e.printStackTrace()
            }

            i++
        }
        printPalette(PaletteManager.PALETTES[name]!!)
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

    /**
     * Convert RGB from image pixel to GBA Color
     * @param img Source image
     * *
     * @param x X of pixel
     * *
     * @param y Y of pixel
     * *
     * @return GBA Color of pixel's color
     */
    fun getColorFromImage(img: BufferedImage, x: Int, y: Int): Color {
        val rgb = img.getRGB(x, y)
        val c = Color(rgb)
        print(" " + c + "(" + GBAColor.toGBA(c) + ") ")
        return c
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
        print("Error finding color " + color)
        return 0
    }
}