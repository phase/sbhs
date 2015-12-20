package xyz.jadonfowler.sbhs;

import java.awt.Color;

public class GBAColor {
    public static Color fromGBA(String hex) {
        assert hex.length() == 4 : "Woah! This string isn't 4 characters wide!";
        int h = Integer.parseInt(hex, 16), r = 0, g = 0, b = 0;
        while (h - (4 * 256) >= 0) {
            b += 8;
            h -= (4 * 256);
        }
        while (h - 32 >= 0) {
            g += 8;
            h -= 32;
        }
        r = h * 8;
        int t = 255;
        r = r > t ? t : r;
        g = g > t ? t : g;
        b = b > t ? t : b;
        Color c = new Color(r, g, b);
        return c;
    }

    public static String toGBA(int r, int g, int b) {
        r = (int) r / 8;
        g = ((int) g / 8) * 32;
        b = ((int) b / 8) * 256 * 4;
        int i = r + g + b;
        return (i < Math.pow(16, 3) ? "0" : "") + (i < 256 ? "0" : "") + (i < 16 ? "0" : "")
                + Integer.toHexString(r + g + b);
    }

    public static String toGBA(Color c) {
        return toGBA(c.getRed(), c.getGreen(), c.getBlue());
    }

    public static int[] toInts(String s) {
        assert s.length() == 4 : "Woah! This string isn't 4 characters wide!";
        char[] c = s.toCharArray();
        int[] i = new int[2];
        i[0] = Integer.parseInt(c[0] + "" + c[1], 16);
        i[1] = Integer.parseInt(c[2] + "" + c[3], 16);
        return i;
    }

}