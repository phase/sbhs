package xyz.jadonfowler.sbse;

import java.io.RandomAccessFile;

/**
 * @author https://github.com/phase
 */
public class SBSE {
    public static final int SpritesStart = 0x47B800 + (64 * (12 / 2));
    public static final int SpritesEnd = 0xA8C600;

    public static void main(String[] args) throws Exception {
        final String gameLocation = "res/sonic.gba";
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
    }

    public static String hex(int i) {
        return (i < 10 ? "0" : "") + Integer.toHexString(i);
    }

    public static String reverse(String s) {
        return new StringBuilder().append(s).reverse().toString();
    }

    public static String spaceout(String s) {
        String f = "";
        for (char c : s.toCharArray()) {
            f += c + " ";
        }
        return f;
    }
}
