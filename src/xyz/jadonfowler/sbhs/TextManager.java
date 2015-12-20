package xyz.jadonfowler.sbhs;

import java.awt.event.*;
import javax.swing.*;

public class TextManager {
    public static void addTextTab(JTabbedPane pane, String name, int to, int from) throws Exception {
        pane.addTab(name, null, createTextPanel(name, to, from), "Edit " + name + " Story Text");
    }

    public static JPanel createTextPanel(String name, int to, int from) throws Exception {
        // TODO
        // System.out.println(name);
        JTextArea textArea = new JTextArea(100, 50);
        /*
         * JScrollPane scroll = new JScrollPane(textArea,
         * JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
         * JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
         */
        textArea.setEditable(true);
        int last = 0;
        String s = "";
        int[] orig = new int[from - to];
        for (int i = to; i < from; i++) {
            SBHS.raf.seek(i);
            int value = SBHS.raf.read();
            orig[i - to] = value;
            if (value == 0 && last == 0) {
                s += " ";
                last = -1;
                continue;
            }
            else if (value == 0) {
                last = 0;
            }
            else {
                s += SBString.convert(last, value);
                last = value;
            }
        }
        s = s.replace("]", "").replace("[", "");
        // System.out.println(s);
        //int[] conv = SBString.to(s);
        // System.out.println(orig.length + "\n" + conv.length + "\n" + "They
        // are "
        // + (Arrays.equals(orig, conv) ? "equal" : "NOT equal"));
        // s = SBString.from(SBString.to(s));
        textArea.setText(s);
        // System.out.println(hex(Math.abs(from - to - SBString.to(s).length)));
        JButton write = new JButton("Write to ROM");
        write.addActionListener(new ActionListener() {
            final byte t = (byte) to;
            final byte f = (byte) from;

            @Override public void actionPerformed(ActionEvent e) {
                try {
                    int j = 0;
                    int[] text = SBString.to(textArea.getText());
                    for (int i = t; i < f; i++) {
                        SBHS.raf.seek(i);
                        SBHS.raf.write(text[j]);
                        j++;
                    }
                }
                catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });
        // p.add(write);
        JScrollPane sp = new JScrollPane(textArea);
        sp.setBounds(23, 3, 394, 20);
        JPanel bp = new JPanel();
        bp.add(write);
        JPanel globalPanel = new JPanel();
        globalPanel.add(bp);
        globalPanel.add(sp);
        return globalPanel;
    }
}
