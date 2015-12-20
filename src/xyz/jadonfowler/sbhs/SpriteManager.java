package xyz.jadonfowler.sbhs;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class SpriteManager {
    public static HashMap<String, HashMap<String, BufferedImage>> SPRITES = new HashMap<String, HashMap<String, BufferedImage>>() {
        private static final long serialVersionUID = 1L;

        {
            put("Ground Shadow", new HashMap<String, BufferedImage>());
            put("Sonic", new HashMap<String, BufferedImage>());
            put("Knuckles", new HashMap<String, BufferedImage>());
            put("Tails", new HashMap<String, BufferedImage>());
            put("Amy", new HashMap<String, BufferedImage>());
            //TODO finish
        }
    };

    public static void addCharacterSpriteTab(JTabbedPane pane, String name, int offset) throws Exception {
        JTabbedPane t = new JTabbedPane();
        t.addTab("Idle", null, createSpritePanel(name, "Idle", offset, 32 * 8), "Edit " + name + " Idle Sprite");
        pane.addTab(name, null, t, "Edit " + name + " Sprite");
    }

    public static void addSpriteTab(JTabbedPane pane, String name, int offset, int amount) throws Exception {
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
            int v1 = Integer.parseInt(value.toCharArray()[0] + "", 16);
            int v2 = Integer.parseInt(value.toCharArray()[1] + "", 16);
            try {
                o1 = GBAColor.fromGBA(PaletteManager.PALETTES.get(name)[v1]);
                o2 = GBAColor.fromGBA(PaletteManager.PALETTES.get(name)[v2]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            int col1 = (o1.getRed() << 16) | (o1.getGreen() << 8) | o1.getBlue();
            int col2 = (o2.getRed() << 16) | (o2.getGreen() << 8) | o2.getBlue();
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
                System.out.println("Save image");
            }
        });
        JButton upload = new JButton("Upload sprites");
        upload.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println("Upload image");
            }
        });
        JPanel jp = new JPanel();
        int h = (int) Math.ceil(amount / 4);
        int sw = 200 / h * 12;
        int sh = SBHS.frame.getHeight();
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

    private static void printPalette(String[] s) {
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