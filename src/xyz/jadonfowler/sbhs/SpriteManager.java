package xyz.jadonfowler.sbhs;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

public class SpriteManager {
    public static void addSpriteTab(JTabbedPane pane, String name, int offset, int amount) throws Exception {
        pane.addTab(name, null, createSpritePanel(name, offset, amount), "Edit " + name + " Sprite");
    }

    public static JPanel createSpritePanel(String name, int offset, int amount) throws Exception {
        JPanel jp = new JPanel();
        BufferedImage img = new BufferedImage(8 * 8 * 8, 8 * 2 * (int) Math.ceil(amount / 4),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.dispose();
        int x = 1, y = 1, s = 0;
        for (int i = offset, size = 4, rows = 8; i < offset + (size * rows * amount); i++) {
            SBHS.raf.seek(i);
            int v = SBHS.raf.read();
            String value = SBHS.reverse((v < 10 ? "0" : "") + Integer.toString(v, 16));
            // System.out.println(value);
            Color o1 = Color.black, o2 = Color.white;
            int v1 = Integer.parseInt(value.split("")[1], 16);
            int v2 = Integer.parseInt(value.split("")[1], 16);
            try {
                // System.out.println(v1 + " " + v2);
                o1 = GBAColor.fromGBA(PaletteManager.PALETTES.get(name)[v1]);
                o2 = GBAColor.fromGBA(PaletteManager.PALETTES.get(name)[v2]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            int col1 = (o1.getRed() << 16) | (o1.getGreen() << 8) | o1.getBlue();
            int col2 = (o2.getRed() << 16) | (o2.getGreen() << 8) | o2.getBlue();
            //if (v1 == 0) System.out.println(o1);
            System.out.println(
                    " S: " + s + " X: " + x + " Y: " + y + " MaxX: " + img.getWidth() + " MaxY: " + img.getHeight());
            try {
                img.setRGB(x-1, y-1, col1);
            }
            catch (Exception e) {
                System.out.println("ERROR @ " + x + " " + y + " / " + img.getWidth() + " " + img.getHeight());
                e.printStackTrace();
                System.exit(-1);
            }
            x++;
            System.out.println(
                    " S: " + s + " X: " + x + " Y: " + y + " MaxX: " + img.getWidth() + " MaxY: " + img.getHeight());
            try {
                img.setRGB(x-1, y-1, col2);
            }
            catch (Exception e) {
                System.out.println("ERROR @ " + x + " " + y + " / " + img.getWidth() + " " + img.getHeight());
                e.printStackTrace();
                System.exit(-1);
            }
            x++;
            if (x != 0 && x % 9 == 0) {
                x -= 8;
                y++;
                if (y != 0 && y % 9 == 0) {
                    System.out.print(x + " " + y + " => ");
                    y -= 8;
                    x += 9;
                    System.out.println(x + " " + y);
                    s++;
                    if (s == 4) {
                        s = 0;
                        x = 1;
                        y += 8;
                    }
                }
            }
        }
        jp.add(new JLabel(new ImageIcon(img)));
        return jp;
    }

    public static void spriteTest() throws Exception {
        RandomAccessFile raf = new RandomAccessFile(SBHS.gameLocation, "r");
        // for (int i = SpritesStart, j = 0; i <= SpritesEnd; i++) {
        String f = "";
        for (int i = SBHS.SpritesStart, j = 1, k = 0, size = 4, rows = 8; i < SBHS.SpritesStart
                + (size * rows * 2); i++) {
            if (j == 1) System.out.print(SBHS.hex(i) + ": ");
            raf.seek(i);
            int value = raf.read();
            f += (char) (value <= 20 ? '.' : value);
            System.out.print(SBHS.spaceout(SBHS.reverse(SBHS.hex(value))));
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
}
