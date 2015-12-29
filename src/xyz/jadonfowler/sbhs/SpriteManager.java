package xyz.jadonfowler.sbhs;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class SpriteManager {
    public static HashMap<String, HashMap<String, BufferedImage>> SPRITES = new HashMap<String, HashMap<String, BufferedImage>>();

    public static void addCharacterSpriteTab(JTabbedPane pane, String name, int offset) throws Exception {
        //@formatter:off
        SPRITES.put(name, new HashMap<String, BufferedImage>());
        JTabbedPane t = new JTabbedPane();
        // When I try to condense the equations into numbers, I get weird
        // errors. Probably has to do with the fact that SEGA is so idiotic that
        // they put giant spaces in between certain animations for no reason
        // whatsoever. It was nice for the first few animations, but then I
        // noticed that not all of them had it, which makes me confused as
        // to why they were there to begin with. Thank you SEGA, for making
        // a game more confusing that Minecraft's protocol.
        t.addTab("Idle", null, createSpritePanel(name, "Idle", //6
                offset, 32 * 7 - 8), 
                "Edit " + name + " Idle Sprite");
        // Why the hell there is a "jog" animation?! JUST GO INTO RUNNING
        // ALREADY SEGA, YOU SURE HAVEN'T DONE THAT IN A WHILE.
        t.addTab("Jog", null, createSpritePanel(name, "Jog", //1
                offset + (64 * 2 * 64) + (64 * 16), 32),
                "Edit " + name + " Jog Sprite");
        t.addTab("Run", null, createSpritePanel(name, "Run", //8
                offset + (64 * 3 * 64) + (64 * 24), 32 * 9),
                "Edit " + name + " Run Sprite");
        t.addTab("Halt", null, createSpritePanel(name, "Halt", //4
                offset + (64 * 5 * 64) + (64 * 40), 32 * 4 + 16),
                "Edit " + name + " Halt Sprite");
        t.addTab("Dash", null, createSpritePanel(name, "Dash", //7
                offset + (64 * 6 * 64) + (64 * 48), 32 * 8),
                "Edit " + name + " Dash Sprite");
        t.addTab("Turn", null, createSpritePanel(name, "Turn", //3
                offset + (64 * 9 * 64), 32 * 4 - 16),
                "Edit " + name + " Turn Sprite");
        t.addTab("Change Direction", null, createSpritePanel(name, "Change Direction", //4
                offset + (64 * 10 * 64) + (64 * 8), 32 * 5 - 16),
                "Edit " + name + " Change Direction Sprite");
        t.addTab("Fall", null, createSpritePanel(name, "Fall", //4
                offset + (64 * 11 * 64) + (64 * 16), 32 * 5 - 16),
                "Edit " + name + " Fall Sprite");
        t.addTab("Jump", null, createSpritePanel(name, "Jump", //5
                offset + (64 * 12 * 64) + (64 * 24), 32 * 6 - 16),
                "Edit " + name + " Jump Sprite");
        t.addTab("Land", null, createSpritePanel(name, "Land",
                offset + (64 * 14 * 64) + (64 * 38), 32 * 4 - 16), //3
                "Edit " + name + " Land Sprite");
        t.addTab("Double Jump", null, createSpritePanel(name, "Double Jump",
                offset + (64 * 15 * 64) + (64 * 48), 32 * 8), //7
                "Edit " + name + " Double Jump");
        // These are the normal `B` attacks
        t.addTab("Attack", null, createSpritePanel(name, "Attack 1",
                offset + (64 * 18 * 64), 32 * 7 - 8), //6
                "Edit " + name + " Attack 1");
        t.addTab("Attack 2", null, createSpritePanel(name, "Attack 2",
                offset + (64 * 20 * 64) + (64 * 16), 32 * 7 - 8), //6
                "Edit " + name + " Attack 2");
        t.addTab("Attack 3", null, createSpritePanel(name, "Attack 3",
                offset + (64 * 22 * 64) + (64 * 32), 32 * 8 - 4), //7
                "Edit " + name + " Attack 3");
        t.addTab("Big Attack", null, createSpritePanel(name, "Big Attack",
                /* This one is a little weird */
                offset + (64 * 24 * 64) + (64 * 28), 32 * 19 + 8), //17
                "Edit " + name + " Big Attack");
        // When you press the direction away from where you're pointing and B
        t.addTab("Back Attack", null, createSpritePanel(name, "Back Attack",
                offset + (64 * 29 * 64) + (64 * 16), 32 * 10), //9
                "Edit " + name + " Back Attack");
        pane.addTab(name, null, t, "Edit " + name + " Sprite");
        //@formatter:on
    }

    public static void addSpriteTab(JTabbedPane pane, String name, int offset, int amount) throws Exception {
        SPRITES.put(name, new HashMap<String, BufferedImage>());
        pane.addTab(name, null, createSpritePanel(name, "Idle", offset, amount), "Edit " + name + " Sprite");
    }

    public static JPanel createSpritePanel(String name, String state, int offset, int amount) throws Exception {
        BufferedImage img = new BufferedImage(8 * 4, 8 * (int) Math.ceil(amount / 4), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.dispose();
        int x = 1, y = 1, s = 0;
        for (int i = offset, size = 4, rows = 8; i < offset + (size * rows * amount); i++) {
            SBHS.raf.seek(i);
            int v = SBHS.raf.read();
            String value = SBHS.reverse((v < 0x10 ? "0" : "") + Integer.toString(v, 16));
            Color o1 = Color.black, o2 = Color.white;
            //System.out.print(value + " : ");
            int v1 = Integer.parseInt(value.toCharArray()[0] + "", 16);
            int v2 = Integer.parseInt(value.toCharArray()[1] + "", 16);
            //System.out.println(v1 + " " + v2);
            try {
                //printPalette(PaletteManager.PALETTES.get(name));
                String s1 = PaletteManager.PALETTES.get(name)[v1];
                if (s1 == null) s1 = "0000";
                o1 = GBAColor.fromGBA(s1);
                String s2 = PaletteManager.PALETTES.get(name)[v2];
                if (s2 == null) s2 = "0000";
                o2 = GBAColor.fromGBA(s2);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
            int col1 = o1.getRGB();
            int col2 = o2.getRGB();
            // if (v1 == 0) System.out.println(o1);
            // System.out.println(
            // " S: " + s + " X: " + x + " Y: " + y + " MaxX: " + img.getWidth()
            // + " MaxY: " + img.getHeight());
            try {
                img.setRGB(x - 1, y - 1, col1);
            }
            catch (Exception e) {
                System.out.println("ERROR @ " + x + " " + y + " / " + img.getWidth() + " " + img.getHeight());
                e.printStackTrace();
                System.exit(-1);
            }
            x++;
            // System.out.println(
            // " S: " + s + " X: " + x + " Y: " + y + " MaxX: " + img.getWidth()
            // + " MaxY: " + img.getHeight());
            try {
                img.setRGB(x - 1, y - 1, col2);
            }
            catch (Exception e) {
                System.out.println("ERROR @ " + x + " " + y + " / " + img.getWidth() + " " + img.getHeight());
                e.printStackTrace();
                System.exit(-1);
            }
            if (x != 0 && x % 8 == 0) {
                x -= 8;
                if (y != 0 && y % 8 == 0) {
                    y -= 8;
                    x += 8;
                    s++;
                    if (s == 4) {
                        s = 0;
                        x = 0;
                        y += 8;
                    }
                }
                y++;
            }
            x++;
        }
        SPRITES.get(name).put(state, img);
        JButton write = new JButton("Write to ROM");
        write.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                writeImage(name, state, offset, amount);
            }
        });
        JButton save = new JButton("Save sprites");
        save.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println("Saving image " + name + ": " + state);
                BufferedImage i = SPRITES.get(name).get(state);
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(SBHS.frame) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File o = fc.getSelectedFile();
                        ImageIO.write(i, "png", o);
                    }
                    catch (IOException x) {
                        x.printStackTrace();
                    }
                }
            }
        });
        JButton upload = new JButton("Upload sprites");
        upload.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println("Uploading image " + name + ": " + state);
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(SBHS.frame) == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage i = ImageIO.read(fc.getSelectedFile());
                        SPRITES.get(name).put(state, i);
                    }
                    catch (IOException x) {
                        x.printStackTrace();
                    }
                }
            }
        });
        JPanel jp = new JPanel();
        int h = (int) Math.ceil(amount / 4);
        int sw = 200 / h * 16;
        int sh = SBHS.frame.getHeight() - 64;
        ImageIcon spriteSheet = new ImageIcon(scale(img, sw, sh));
        jp.add(write);
        jp.add(save);
        jp.add(upload);
        jp.add(new JLabel(spriteSheet));
        return jp;
    }
    
    /**
     * Writes uncompressed sprite to ROM
     * @param img Image to write
     * @param offset Offset of image in ROM
     * @param amount Amount of 8x8 squares
     */
    public static void writeImage(String name, String state, int offset, int amount) {
        BufferedImage img = SPRITES.get(name).get(state);
        printPalette(PaletteManager.PALETTES.get(name));
        int x = 1, y = 1, s = 0;
        for (int i = offset, size = 4, rows = 8; i < offset + (size * rows * amount); i++) {
            Color c1 = getColorFromImage(img, x - 1, y - 1);
            // This gets the index of the color in the palette,
            // which is the value we need to write to the ROM
            //System.out.println("Palette contains " + c1 + ": " + Arrays.asList(PaletteManager.PALETTES.get(name)).contains(c1));
            int v1 = getIndexFromPalette(name, c1);
            x++;
            Color c2 = getColorFromImage(img, x - 1, y - 1);
            if (x != 0 && x % 8 == 0) {
                x -= 8;
                if (y != 0 && y % 8 == 0) {
                    y -= 8;
                    x += 8;
                    s++;
                    if (s == 4) {
                        s = 0;
                        x = 0;
                        y += 8;
                    }
                }
                y++;
            }
            x++;
            //System.out.println("Palette contains " + c2 + ": " + Arrays.asList(PaletteManager.PALETTES.get(name)).contains(c2));
            int v2 = getIndexFromPalette(name, c2);
            // Writing to ROM
//            if (v1 < 0) v1 = 0;
//            if (v2 < 0) v2 = 0;
            int v = Integer.parseInt(Integer.toString(v2, 16) + "" + Integer.toString(v1, 16), 16);
            try {
                SBHS.raf.seek(i);
                SBHS.raf.write(v);
                System.out.println("Colors: " + c1 + " " + c2 + " Wrote: " + Integer.toString(v, 16) + " @ 0x"
                        + Integer.toString(i, 16));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        printPalette(PaletteManager.PALETTES.get(name));
    }

    /**
     * Scales image to new size
     * @param src Image to scale
     * @param w Width of new image
     * @param h Height of new image
     * @return Scaled image
     */
    public static BufferedImage scale(BufferedImage src, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        for (x = 0; x < w; x++) {
            for (y = 0; y < h; y++) {
                int col = src.getRGB(x * ww / w, y * hh / h);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }
    
    /**
     * Convert RGB from image pixel to GBA Color
     * @param img Source image
     * @param x X of pixel
     * @param y Y of pixel
     * @return GBA Color of pixel's color
     */
    public static Color getColorFromImage(BufferedImage img, int x, int y) {
        int rgb = img.getRGB(x, y);
        Color c = new Color(rgb);
        System.out.print(" " + c + "(" + GBAColor.toGBA(c) + ") ");
        return c;
    }

    protected static void printPalette(String[] s) {
        System.out.print("[");
        for (int i = 0; i < s.length; i++) {
            System.out.print(s[i] + "(");
            System.out.print(GBAColor.fromGBA(s[i]) + ")");
            if (i < s.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    public static int getIndexFromPalette(String name, Color color) {
        ArrayList<String> p = new ArrayList<String>(Arrays.asList(PaletteManager.PALETTES.get(name)));
        for (String s : p) {
            int i = p.indexOf(s);
            Color pc = GBAColor.fromGBA(s);
            if (pc.equals(color)) return i;
        }
        System.out.print("Error finding color " + color);
        return 0;
    }
}