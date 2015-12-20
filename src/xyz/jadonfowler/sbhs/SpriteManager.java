package xyz.jadonfowler.sbhs;

import java.awt.*;
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
        JPanel jp = new JPanel();
        int h = (int) Math.ceil(amount / 4);
        int sw = 200 / h * 12;
        int sh = SBHS.frame.getHeight();
        ImageIcon spriteSheet = new ImageIcon(scale(img, sw, sh));
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
        int x = 1, y = 1, s = 0;
        for (int i = offset, size = 4, rows = 8; i < offset + (size * rows * amount); i++) {
            String c1 = getGBAColorFromImage(img, x - 1, y - 1);
            // This gets the index of the color in the palette,
            // which is the value we need to write to the ROM
            int v1 = ((ArrayList<String>) Arrays.asList(PaletteManager.PALETTES.get(name))).indexOf(c1);
            x++;
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
            String c2 = getGBAColorFromImage(img, x - 1, y - 1);
            x++;
            int v2 = ((ArrayList<String>) Arrays.asList(PaletteManager.PALETTES.get(name))).indexOf(c2);
            //Writing to ROM
            int v = Integer.parseInt(Integer.toString(v2, 16) + "" + Integer.toString(v1, 16), 16);
            try {
                SBHS.raf.seek(i);
                SBHS.raf.write(v);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public static String getGBAColorFromImage(BufferedImage img, int x, int y) {
        return GBAColor.toGBA(new Color(img.getRGB(x, y)));
    }
}