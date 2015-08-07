package xyz.jadonfowler.sbse;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
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
    public static HashMap<String, String[]> PALETTE_INFO = new HashMap<String, String[]>() {
        {
            String[] g = { "Background (useless)", "Eye color and shoe/glove reflection",
                    "Above the eye, shoe/glove color", "Outside of shoe/glove", "Outline of shoe/glove",
                    "In-ball shine", "Primary fur color", "Secondary fur color", "Third fur color/outline",
                    "Primary skin color", "Secondary skin color", "Third skin color", "Primary shoe color",
                    "Secondary shoe color", "Third shoe color", "Eye Color" };
            put("Sonic", g);
            put("Knuckles", g);
            put("Tails",
                    new String[] { "Background (useless)", "Eye color and shoe/glove reflection",
                            "Above the eye, tail/hand color", "Primary outline", "Secondary outline", "In-ball shine",
                            "Primary fur color", "Secondary fur color", "Third fur color/outline", "Primary skin color",
                            "Secondary skin color", "Third skin color", "Primary shoe color & Weapons",
                            "Secondary shoe color & Weapons", "Third shoe color & Weapons", "Eye Color" });
        }
    };
    public static HashMap<String, String[]> PALETTES = new HashMap<String, String[]>();

    public static void main(String[] args) throws Exception {
        frame = new JFrame("Sonic Battle Hack Suite " + VERSION + " - By Phase");
        frame.setSize(700, 600);
        // frame.setResizable(false);
        gameLocation = getFile();
        raf = new RandomAccessFile(gameLocation, "rw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane mainTabs = new JTabbedPane();
        {
            // Palette Editor
            JTabbedPane paletteTabs = new JTabbedPane();
            addPaletteTab(paletteTabs, "Sonic", 0x47AFB8);
            addPaletteTab(paletteTabs, "Tails", 0x5283F8);
            addPaletteTab(paletteTabs, "Knuckles", 0x4CADD8);
            addPaletteTab(paletteTabs, "Amy", 0x636458);
            addPaletteTab(paletteTabs, "Shadow", 0x58D818);
            addPaletteTab(paletteTabs, "Rouge", 0x5F3E38);
            addPaletteTab(paletteTabs, "E-102", 0x681A78);
            addPaletteTab(paletteTabs, "Chaos", 0x7336B8);
            addPaletteTab(paletteTabs, "Emerl", 0x787CFA);
            addPaletteTab(paletteTabs, "Fake Emerl", 0x47AB78);
            addPaletteTab(paletteTabs, "Eggman", 0x7822D8);
            mainTabs.addTab("Palette Editor", null, paletteTabs, "Palette Editor");
        }
        {
            // Text editor
            JTabbedPane textTabs = new JTabbedPane();
            addTextTab(textTabs, "Sonic", 0x1DB3FC, 0x1E1467);
            addTextTab(textTabs, "Tails", 0x1E146A, 0x1E6FA7);
            addTextTab(textTabs, "Rouge", 0x1E6FA8, 0x1ED2C3);
            addTextTab(textTabs, "Knuckles", 0x1ED2C4, 0x1F417F);
            addTextTab(textTabs, "Amy", 0x1F4180, 0x1F9CDB);
            addTextTab(textTabs, "Cream", 0x1F9CDC, 0x1FE86F);
            addTextTab(textTabs, "Shadow", 0x1FE870, 0x206103);
            addTextTab(textTabs, "Emerl", 0x206104, 0x20B131);
            mainTabs.addTab("Text Editor", null, textTabs, "Text Editor");
        }
        {
            // Sprite Editor
            int amount = 75;
            BufferedImage img = new BufferedImage(8 * 4, 8 * (int)Math.ceil(amount / 4), BufferedImage.TYPE_INT_RGB);
            int x = 0, y = 0;
            for (int i = SpritesStart, j = 1, k = 0, size = 4, rows = 8; i < SpritesStart + (size * rows * amount); i++) {
                // if (j == 1) all += (hex(i) + ": ");
                raf.seek(i);
                int value = raf.read();
                String d = reverse(hex(value));
                //System.out.println(Integer.toHexString(value));
                Color o = Color.black;
                try {
                    o = GBAColor.fromGBA(PALETTES.get("Sonic")[value]);
                }
                catch (Exception e) {}
                int col = (o.getRed() << 16) | (o.getGreen() << 8) | o.getBlue();
                img.setRGB(x, y, col);
                x++;
                if(x >= 8 * 4){
                    x = 0;
                    y++;
                }
            }
            /*// System.out.print(Arrays.toString(dump));
            String d = join(dump).replace(" ", "");
            int x = 0, y = 0, offset = 0;
            for (String s : d.split("\n")) {
                for (char c : s.toCharArray()) {
                    int h = Integer.parseInt(c + "", 16);
                    // System.out.println("x " + x + " y " + y + " w " + img.getWidth() + " h " + img.getHeight());
                    img.setRGB(x, y + offset, col);
                    x++;
                    /*if (x % 8 == 0) {
                        y++;
                        x = 0;
                    }* /
                }
                y++;
                x = 0;
            }*/
            /*StyledDocument doc = new DefaultStyledDocument();
            JTextPane t = new JTextPane(doc);
            t.setText(d);
            for (int i = 0; i < t.getDocument().getLength(); i++) {
                SimpleAttributeSet set = new SimpleAttributeSet();
                char c = t.getText().toCharArray()[i];
                try {
                    int j = Integer.parseInt(c + "", 16);
                    if (j == 0) StyleConstants.setBackground(set, Color.white);
                    else StyleConstants.setBackground(set, GBAColor.fromGBA(PALETTES.get("Sonic")[j]));
                    StyleConstants.setFontSize(set, 3);
                    StyleConstants.setAlignment(set, 2);
                    doc.setCharacterAttributes(i, 1, set, true);
                }
                catch (Exception e) {}
            }*/
            mainTabs.addTab("Sprite Editor", null, new JLabel(new ImageIcon(img)), "Sprite Editor");
        }
        {
            // About Page
            JTextPane t = new JTextPane();
            t.setText("Sonic Battle Hack Suite " + VERSION + " was made by Phase.\n"
                    + "You can find the source at https://github.com/phase/sonicbattle");
            mainTabs.addTab("About", null, t, "About Page");
        }
        frame.getContentPane().add(mainTabs);
        frame.setVisible(true);
        // frame.pack();
    }

    
    public static void addTextTab(JTabbedPane pane, String name, int to, int from) throws Exception {
        pane.addTab(name, null, createTextPanel(name, to, from), "Edit " + name + " Story Text");
    }
    
    public static JPanel createTextPanel(String name, int to, int from) throws Exception {
        //System.out.println(name);
        JPanel p = new JPanel();
        JTextArea t = new JTextArea();
        JScrollPane scroll = new JScrollPane(t, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        t.setEditable(true);
        int last = 0;
        String s = "";
        for (int i = to; i < from; i++) {
            raf.seek(i);
            int value = raf.read();
            s += SBString.convert(last, value);
            if (value == 0 && last == 0) last = -1;
            else last = value;
        }
        t.setText(s);
        p.add(scroll);
        return p;
    }
    
    public static void addPaletteTab(JTabbedPane pane, String name, int offset) throws Exception {
        pane.addTab(name, null, createPalettePanel(name, offset), "Edit " + name + " Palette");
    }

    public static JPanel createPalettePanel(String name, int hex) throws Exception {
        String[] colors = new String[16];
        int color = 0;
        String f = "";
        for (int i = hex; i <= hex + 32; i++) {
            raf.seek(i);
            int value = raf.read();
            // System.out.print(Integer.toHexString(value));
            if (f.length() == 4) {
                f = f.split("(?<=\\G.{2})")[1] + f.split("(?<=\\G.{2})")[0];
                colors[color++] = f;
                f = (value < 16 ? "0" : "") + Integer.toHexString(value);
            }
            else {
                f += (value < 16 ? "0" : "") + Integer.toHexString(value);
            }
        }
        PALETTES.put(name, colors);
        // System.out.println(Arrays.toString(colors));
        int i = 0;
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        for (String s : colors) {
            i++;
            JButton jb = new JButton(name
                    + " color "
                    + i
                    + ": "
                    + (PALETTE_INFO.get(name) == null ? "Something?" : PALETTE_INFO.get(name).length < i ? "Something?"
                            : PALETTE_INFO.get(name)[i - 1]));
            jb.addActionListener(new ActionListener() {
                final String character = name;

                public void actionPerformed(ActionEvent e) {
                    String name = jb.getText();
                    int i = -1 + (Integer.parseInt(name.split(" ")[2].replace(":", "")));
                    colors[i] = GBAColor.toGBA(getColorInput(GBAColor.fromGBA(colors[i])));
                    PALETTES.remove(character);
                    PALETTES.put(character, colors);
                    try {
                        int h1 = Integer.parseInt(colors[i].split("(?<=\\G.{2})")[0], 16);
                        int h2 = Integer.parseInt(colors[i].split("(?<=\\G.{2})")[1], 16);
                        // colors[i] = Integer.toHexString(h2) +
                        // Integer.toHexString(h1) + "";
                        raf.seek(hex + (i * 2));
                        raf.write(h2);
                        raf.seek(hex + (i * 2) + 1);
                        raf.write(h1);
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    // System.out.println(Arrays.toString(colors));
                }
            });
            jp.add(jb);
        }
        return jp;
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
                if (a == amount) return currentOffset;
                if (currentOffset == -1 && value == 0) currentOffset = i;
                else a++;
            }
        }
        catch (Exception e) {}
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

    public static void spriteTest() throws Exception {
        RandomAccessFile raf = new RandomAccessFile(gameLocation, "r");
        // for (int i = SpritesStart, j = 0; i <= SpritesEnd; i++) {
        String f = "";
        for (int i = SpritesStart, j = 1, k = 0, size = 4, rows = 8; i < SpritesStart + (size * rows * 2); i++) {
            if (j == 1) System.out.print(hex(i) + ": ");
            raf.seek(i);
            int value = raf.read();
            f += (char) (value <= 20 ? '.' : value);
            System.out.print(spaceout(reverse(hex(value))));
            if (j < size) j++;
            else {
                j = 1;
                System.out.println(f);
                f = "";
            }
            if (k < (size * rows) - 1) k++;
            else {
                k = 0;
                System.out.println("========");
            }
        }
        raf.close();
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
        /*String f = null;
        for (int i = 0; i < a.length; i++) {
            try{
                String h = a[i], j = a[++i], k = a[++i], l = a[++i];
                String g = join(join(h, j), join(k, l));
                f = (f == null ? g : join(f, g)) + "\n";
                System.out.println(f+"\nn");
            }catch(Exception e){
                //less than 4 elemnts left
            }
        }*/
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
