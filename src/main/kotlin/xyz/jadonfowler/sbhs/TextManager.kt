package xyz.jadonfowler.sbhs

import java.awt.event.*
import javax.swing.*

object TextManager {
    @Throws(Exception::class)
    fun addTextTab(pane: JTabbedPane, name: String, to: Int, from: Int) {
        pane.addTab(name, null, createTextPanel(name, to, from), "Edit $name Story Text")
    }

    @Throws(Exception::class)
    fun createTextPanel(name: String, to: Int, from: Int): JPanel {
        // TODO
        // System.out.println(name);
        val textArea = JTextArea(100, 50)
        textArea.rows = 40
        textArea.columns = 70
        /*
         * JScrollPane scroll = new JScrollPane(textArea,
         * JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
         * JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
         */
        textArea.isEditable = true
        var last = 0
        var s = ""
        val orig = IntArray(from - to)
        for (i in to..from - 1) {
            SBHS.raf.seek(i.toLong())
            val value = SBHS.raf.read()
            orig[i - to] = value
            if (value == 0 && last == 0) {
                s += " "
                last = -1
                continue
            } else if (value == 0) {
                last = 0
            } else {
                s += SBString.convert(last, value)
                last = value
            }
        }
        s = s.replace("]", "").replace("[", "")
        s = s.replace("{", "{^")
        // System.out.println(s);
        //int[] conv = SBString.to(s);
        // System.out.println(orig.length + "\n" + conv.length + "\n" + "They
        // are "
        // + (Arrays.equals(orig, conv) ? "equal" : "NOT equal"));
        // s = SBString.from(SBString.to(s));
        textArea.text = s
        // System.out.println(hex(Math.abs(from - to - SBString.to(s).length)));
        val write = JButton("Write to ROM")
        write.addActionListener { e: ActionEvent ->
            try {
                var j = 0
                val text = SBString.to(textArea.text)
                for (i in to..from - 1) {
//                    println("0x" + Integer.toHexString(i))
                    SBHS.raf.seek(i.toLong())
                    SBHS.raf.write(text[j])
                    j++
                }
            } catch (E: Exception) {
                E.printStackTrace()
            }

        }
        // p.add(write);
        val sp = JScrollPane(textArea)
        // sp.setBounds(23, 3, 394, 20);
        val bp = JPanel()
        bp.add(write)
        val globalPanel = JPanel()
        globalPanel.add(bp)
        globalPanel.add(sp)
        return globalPanel
    }
}
