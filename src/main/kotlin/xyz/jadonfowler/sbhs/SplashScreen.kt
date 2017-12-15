package xyz.jadonfowler.sbhs

import java.awt.Color
import java.awt.Graphics
import javax.swing.JWindow

class SplashScreen : JWindow() {

    init {
        setSize(490, 364)
        isVisible = true
    }

    var timer = System.nanoTime()

    var message = "JFrame"
        set(x) {
            val time = System.nanoTime() - timer
            println("$message took ${time / 1000000000.0}s")
            field = x
            timer = System.nanoTime()
            repaint()
        }

    override fun paint(g: Graphics?) {
        if (g == null) return
        g.drawImage(SBHS.splashImage, 0, 0, null)
        val textWidth = (width * (1.0 / 10)).toInt()
        val textHeight = (height * (14.0 / 15)).toInt()
        g.color = Color.white
        g.drawString("Loading $message...", textWidth, textHeight)
    }

}
