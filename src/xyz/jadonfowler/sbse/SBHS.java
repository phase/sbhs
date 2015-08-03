package xyz.jadonfowler.sbse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.RandomAccessFile;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

/**
 * @author https://github.com/phase
 */
public class SBHS {
    public static final int SpritesStart = 0x47B800 + (64 * (12 / 2));
    public static final int SpritesEnd = 0xA8C600;
    public static JFrame frame;
    public static final String gameLocation = "res/sonic.gba";

    public static void main(String[] args) throws Exception {
        frame = new JFrame("Sonic Battle Hack Suite");
        frame.setSize(700, 600);
        frame.setVisible(true);
        final Container contentPane = frame.getContentPane();
        final JButton go = new JButton("Show JColorChooser");
        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contentPane.setBackground(getColorInput(contentPane.getBackground()));
            }
        });
        contentPane.add(go, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RandomAccessFile raf = new RandomAccessFile(gameLocation, "rw");
        String[] colors = new String[16];
        int color = 0;
        String f = "";
        for (int i = 0x47AFB8; i <= 0x47AFB8 + 32; i++) {
            raf.seek(i);
            int value = raf.read();
            // System.out.print(Integer.toHexString(value));
            if (f.length() == 4) {
                colors[color++] = f;
                f = (value < 16 ? "0" : "") + Integer.toHexString(value);
            }
            else {
                f += (value < 16 ? "0" : "") + Integer.toHexString(value);
                System.out.println(f);
            }
        }
        System.out.println(Arrays.toString(colors));
        raf.close();
    }

    public static Color getColorInput(Color previousColor) {
        return JColorChooser.showDialog(frame, "Pick Color", previousColor);
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
