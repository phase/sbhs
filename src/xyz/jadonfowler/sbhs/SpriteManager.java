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
        BufferedImage img = new BufferedImage(8 * 4 * 2, 8 * 4 * (int) Math.ceil(amount / 4),
                BufferedImage.TYPE_INT_RGB);
        int x = 1, y = 1, s = 0;
        for (int i = offset, size = 4, rows = 8; i < offset + (size * rows * amount); i++) {
            SBHS.raf.seek(i);
            int v = SBHS.raf.read();
            String value = SBHS.reverse((v < 10 ? "0" : "") + v);
            final int mid = value.length() / 2;
            int[] parts = { Integer.parseInt(value.substring(0, mid), 16),
                    Integer.parseInt(value.substring(mid), 16), };
            Color o1 = Color.black, o2 = Color.black;
            try {
                o1 = GBAColor.fromGBA(PaletteManager.PALETTES.get("Sonic")[parts[0]]);
                o2 = GBAColor.fromGBA(PaletteManager.PALETTES.get("Sonic")[parts[1]]);
            }
            catch (Exception e) {}
            int col1 = (o1.getRed() << 16) | (o1.getGreen() << 8) | o1.getBlue();
            int col2 = (o2.getRed() << 16) | (o2.getGreen() << 8) | o2.getBlue();
            System.out.println(
                    "S: " + s + " X: " + x + " Y: " + y + " MaxX: " + img.getWidth() + " MaxY: " + img.getHeight());
            img.setRGB(x - 1, y - 1, col1);
            x++;
            System.out.println(
                    "S: " + s + " X: " + x + " Y: " + y + " MaxX: " + img.getWidth() + " MaxY: " + img.getHeight());
            img.setRGB(x - 1, y - 1, col2);
            x++;
            if (x != 0 && x % 9 == 0) {
                x -= 8;
                y++;
                if (y != 0 && y % 9 == 0) {
                    y -= 8;
                    x += 8;
                    s++;
                    if (s == 4) {
                        s = 0;
                        x = 0;
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
