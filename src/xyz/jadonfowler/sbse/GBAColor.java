package xyz.jadonfowler.sbse;

import java.awt.Color;

public class GBAColor {
    public static Color fromGBA(String hex) {
        if (hex.length() != 4) throw new IllegalArgumentException("Woah! This string isn't 4 characters wide!");
        /*
         * System.out.print(hex + " : "); int h1 =
         * Integer.parseInt(hex.split("(?<=\\G.{2})")[1], 16); int h2 =
         * Integer.parseInt(hex.split("(?<=\\G.{2})")[0], 16); hex = (h1 < 10 ?
         * "0" : "") + Integer.toHexString(h1) + (h2 < 10 ? "0" : "") +
         * Integer.toHexString(h2); System.out.println(hex);
         */
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
        Color c = new Color(r
                , 
                g,
                b);
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
}
