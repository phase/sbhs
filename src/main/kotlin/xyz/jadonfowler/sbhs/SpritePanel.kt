package xyz.jadonfowler.sbhs

import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.image.BufferedImage
import javax.swing.JPanel

class SpritePanel(val img: BufferedImage) : JPanel(true), MouseMotionListener, MouseListener {

    val imgW = img.width
    val imgH = img.height
    var selectedColor = 0

    init {
        addMouseListener(this)
        addMouseMotionListener(this)
    }

    override fun paintComponent(g: Graphics?) {
        g?.drawImage(img, 0, 0, width, height, null)
    }

    override fun mouseClicked(e: MouseEvent?) {

    }

    override fun mouseDragged(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mouseMoved(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

}
