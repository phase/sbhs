package xyz.jadonfowler.sbse;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

/**
 * @author https://github.com/phase
 */
public class SBHS {
    public static String gameLocation = "res/sonic.gba";
    public static RandomAccessFile raf;
    public static final int SpritesStart = 0x47B800 + (64 * (12 / 2));
    public static final int SpritesEnd = 0xA8C600;
    public static JFrame frame;
    public static HashMap<String, String[]> PALETTES = new HashMap<String, String[]>() {
        {
            String[] g = { "Background (useless)", "Eye color and shoe/glove reflection",
                    "Above the eye, shoe/glove color", "Outside of shoe/glove", "Outline of shoe/glove",
                    "In-ball shine", "Primary fur color", "Secondary fur color", "Third fur color/outline",
                    "Primary skin color", "Secondary skin color", "Third skin color", "Primary shoe color",
                    "Secondary shoe color", "Third shoe color", "Eye Color" };
            put("Sonic", g);
            put("Knuckles", g);
        }
    };

    public static void main(String[] args) throws Exception {
        frame = new JFrame("Sonic Battle Hack Suite");
        frame.setSize(700, 600);
        // frame.setResizable(false);
        gameLocation = getFile();
        raf = new RandomAccessFile(gameLocation, "rw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane mainTabs = new JTabbedPane();
        {
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
            JTextPane t = new JTextPane();
            t.setText("Not in yet!");
            mainTabs.addTab("Sprite Editor", null, t, "Sprite Editor");
        }
        {
            JTextPane t = new JTextPane();
            t.setText("Sonic Battle Hack Suite was made by Phase. "
                    + "You can find the source at https://github.com/phase/sonicbattle");
            mainTabs.addTab("About", null, t, "About Page");
        }
        frame.getContentPane().add(mainTabs);
        frame.setVisible(true);
        // frame.pack();
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
                    + (PALETTES.get(name) == null ? "Something?" : PALETTES.get(name).length < i ? "Something?"
                            : PALETTES.get(name)[i - 1]));
            jb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String name = jb.getText();
                    int i = -1 + (Integer.parseInt(name.split(" ")[2].replace(":", "")));
                    colors[i] = GBAColor.toGBA(getColorInput(GBAColor.fromGBA(colors[i])));
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
}
