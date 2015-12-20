package xyz.jadonfowler.sbhs;

import java.awt.*;
import java.io.*;
import javax.swing.*;

/**
 * @author https://github.com/phase
 */
public class SBHS {
    public static final String VERSION = "1.1";
    public static String gameLocation = "res/sonic.gba";
    public static RandomAccessFile raf;
    public static final int SpritesStart = 0x47B800 + (64 * (-98 / 2));
    public static final int SpritesEnd = 0xA8C600;
    public static JFrame frame;

    public static void main(String[] args) throws Exception {
        // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame = new JFrame("Sonic Battle Hack Suite " + VERSION + " - By Phase");
        frame.setSize(700, 600);
        frame.setResizable(true);
        // gameLocation = getFile();
        raf = new RandomAccessFile(gameLocation, "rw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane mainTabs = new JTabbedPane();
        {
            // Palette Editor
            JTabbedPane paletteTabs = new JTabbedPane();
            PaletteManager.addPaletteTab(paletteTabs, "Sonic", 0x47AFB8);
            PaletteManager.addPaletteTab(paletteTabs, "Tails", 0x5283F8);
            PaletteManager.addPaletteTab(paletteTabs, "Knuckles", 0x4CADD8);
            PaletteManager.addPaletteTab(paletteTabs, "Amy", 0x636458);
            PaletteManager.addPaletteTab(paletteTabs, "Shadow", 0x58D818);
            PaletteManager.addPaletteTab(paletteTabs, "Rouge", 0x5F3E38);
            PaletteManager.addPaletteTab(paletteTabs, "E-102", 0x681A78);
            PaletteManager.addPaletteTab(paletteTabs, "Chaos", 0x7336B8);
            PaletteManager.addPaletteTab(paletteTabs, "Emerl", 0x787CFA);
            PaletteManager.addPaletteTab(paletteTabs, "Fake Emerl", 0x47AB78);
            PaletteManager.addPaletteTab(paletteTabs, "Eggman", 0x7822D8);
            mainTabs.addTab("Palette Editor", null, paletteTabs, "Palette Editor");
        }
        {
            // TODO Text editor
            JTabbedPane textTabs = new JTabbedPane();
            TextManager.addTextTab(textTabs, "Sonic", 0x1DB3FC, 0x1E1467);
            /*
             * TextManager.addTextTab(textTabs, "Tails", 0x1E146A, 0x1E6FA7);
             * TextManager.addTextTab(textTabs, "Rouge", 0x1E6FA8, 0x1ED2C3);
             * TextManager.addTextTab(textTabs, "Knuckles", 0x1ED2C4, 0x1F417F);
             * TextManager.addTextTab(textTabs, "Amy", 0x1F4180, 0x1F9CDB);
             * TextManager.addTextTab(textTabs, "Cream", 0x1F9CDC, 0x1FE86F);
             * TextManager.addTextTab(textTabs, "Shadow", 0x1FE870, 0x206103);
             * TextManager.addTextTab(textTabs, "Emerl", 0x206104, 0x20B131);
             */
            mainTabs.addTab("Text Editor", null, textTabs, "Text Editor");
        }
        {
            // Sprite Editor
            JTabbedPane spriteTabs = new JTabbedPane();
            SpriteManager.addSpriteTab(spriteTabs, "Ground Shadow", 0x47ABB8, 31);
            SpriteManager.addSpriteTab(spriteTabs, "Sonic", 0x47CA00, 50);
            mainTabs.addTab("Sprite Editor", null, spriteTabs, "Sprite Editor");
        }
        {
            // About Page
            JTextPane t = new JTextPane();
            t.setText("Sonic Battle Hack Suite " + VERSION + " was made by Phase.\n"
                    + "You can find the source at https://github.com/phase/sbhs");
            t.setEditable(false);
            mainTabs.addTab("About", null, t, "About Page");
        }
        frame.getContentPane().add(mainTabs);
        frame.setVisible(true);
        // frame.pack();
    }

    public static int findFreeSpace(int amount) {
        int currentOffset = -1, a = 0;
        try {
            // 00 00 00 01 10 -(read())-> 0 0 0 1 16
            for (int i = 0xF9EE30; i < 0xFFFF0; i++) {
                raf.seek(i);
                int value = raf.read();
                if (value != 0) {
                    currentOffset = -1;
                    a = 0;
                    continue;
                }
                if (a == amount && currentOffset != -1) return currentOffset;
                if (currentOffset == -1 && value == 0) currentOffset = i;
                else a++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getFile() {
        JFileChooser fc = new JFileChooser();
        return (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) ? fc.getSelectedFile().toString() : "";
    }

    public static Color getColorInput(Color previousColor) {
        Color c = JColorChooser.showDialog(frame, "Pick Color", previousColor);
        return c == null ? previousColor : c;
    }

    public static String hex(int i) {
        return (i < 10 ? "0" : "") + Integer.toHexString(i);
    }

    public static String reverse(String s) {
        return new StringBuilder().append(s).reverse().toString();
    }

    public static String spaceout(String s) {
        String f = "";
        for (char c : s.toCharArray())
            f += c + " ";
        return f;
    }

    public static String join(String[] a) {
        /*
         * String f = null; for (int i = 0; i < a.length; i++) { try{ String h =
         * a[i], j = a[++i], k = a[++i], l = a[++i]; String g = join(join(h, j),
         * join(k, l)); f = (f == null ? g : join(f, g)) + "\n";
         * System.out.println(f+"\nn"); }catch(Exception e){ //less than 4
         * elemnts left } }
         */
        String l = null, f = null;
        for (String s : a) {
            if (l == null) {
                l = s;
                continue;
            }
            if (f == null) {
                f = join(l, s);
                l = null;
                continue;
            }
            String k = join(l, s);
            l = null;
            f = join(f, k);
        }
        return f;
    }

    public static String join(String a, String b) {
        String f = "";
        String[] aa = a.split("\n");
        String[] ba = b.split("\n");
        for (int i = 0; i < aa.length; i++)
            f += aa[i] + ba[i] + "\n";
        return f;
    }

    public static char getChar(String d, int x, int y) {
        String e = d.split("\n")[y];
        return e.toCharArray()[x];
    }

    public static String atosHex(int[] i) {
        String f = "[";
        for (int j : i) {
            f += Integer.toHexString(j) + ", ";
        }
        f += "]";
        return f;
    }
}