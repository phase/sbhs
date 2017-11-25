package xyz.jadonfowler.sbhs

import java.awt.Color
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTabbedPane

object EffectManager {

    fun createEffectPanel(name: String, pane: JTabbedPane, offset: Int, sectionCount: Int, width: Int = 4) {
        val panel = JPanel()

        val image = readImage(offset, PaletteManager.PALETTES["Sonic"]?.toList().orEmpty().map { GBAColor.fromGBA(it).rgb }, sectionCount, width)
        val imageContainer = JLabel(ImageIcon(image))
        panel.add(imageContainer)

        pane.addTab(name, null, panel, "Edit $name")
    }

    fun readImage(offset: Int, palette: List<Int>, sectionCount: Int, width: Int): BufferedImage {
        val sectionSize = 8
        val sections: Array<Array<Array<Int>>> = Array(sectionCount) {
            Array(sectionSize) {
                Array(sectionSize) { x ->
                    x
                }
            }
        }

        var sectionOffset = 0
        (0 until sectionCount).forEach { sectionIndex ->
            var x = 0
            for (y in (0 until sectionSize)) {
                var index = 0
                while (x < sectionSize) {
                    SBHS.raf.seek(offset.toLong() + sectionOffset)
                    val v = SBHS.raf.read()
                    val value = ((if (v < 0x10) "0" else "") + Integer.toString(v, 16)).reversed()
                    val v1 = Integer.parseInt(value[0].toString(), 16)
                    val v2 = Integer.parseInt(value[1].toString(), 16)
                    println("sections[$sectionIndex][$x][$y]=$v2 | sectionSize=$sectionSize | i=$index")
                    sections[sectionIndex][x++][y] = v1
                    println("sections[$sectionIndex][$x][$y]=$v2 | sectionSize=$sectionSize | i=$index")
                    sections[sectionIndex][x++][y] = v2
                    index++
                }
                x = 0
            }
            sectionOffset += sectionSize * sectionSize
        }

        val imgWidth = width * sectionSize
        val imgHeight = (Math.floor(sectionCount / width.toDouble()).toInt() + 1) * sectionSize
        println("$imgWidth x $imgHeight")
        val img = BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB)

        sections.forEachIndexed { s, section ->
            section.forEachIndexed { x, row ->
                row.forEachIndexed { y, v ->
                    val sx = s % width
                    val sy = Math.floor(s.toDouble() / width).toInt()
                    val ix = x + sx * sectionSize // + width * sectionSize * animationIndex
                    val iy = y + sy * sectionSize
                    println("img.setRGB(ix=$ix, iy=$iy, palette[v]=${Color(palette[v])})")
                    img.setRGB(ix, iy, palette[v])
                }
            }
        }

        return img
    }

    fun writeImage(img: BufferedImage, offset: Int, palette: List<Int>, sectionCount: Int, width: Int) {
        val size = 8
        val bytes = mutableListOf<Int>()

        (0 until sectionCount).forEach { s ->
            var x = 0
            while (x < size) {
                (0 until size).forEach { y ->
                    val getValue = { _x: Int ->
                        val sx = s % width
                        val sy = Math.floor(s.toDouble() / width).toInt()
                        val ix = sx * size + _x
                        val iy = sy * size + y
                        palette.indexOf(img.getRGB(ix, iy))
                    }

                    val a = getValue(x)
                    val b = getValue(++x)
                    val v = ((b shl 4) and a)
                    bytes.add(v)
                }
            }
        }

        bytes.forEachIndexed { i, b ->
            SBHS.raf.seek(offset + i.toLong())
            SBHS.raf.writeByte(b)
        }
    }

}
