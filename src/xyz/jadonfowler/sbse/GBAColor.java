package xyz.jadonfowler.sbse;

import java.awt.Color;

public class GBAColor {
    public static Color fromGBA(int hex) {
        return new Color(0, 0, 0);
    }

    public static String toGBA(int r, int g, int b) {
        r = (int) r / 8;
        g = ((int) g / 8) * 32;
        b = ((int) b / 8) * 256 * 4;
        return Integer.toHexString(r + g + b);
    }

    public static String toGBA(Color c) {
        return toGBA(c.getRed(), c.getGreen(), c.getBlue());
    }
}
