package xyz.jadonfowler.sbse;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
        frame.setResizable(false);
        frame.setVisible(true);
        final Container contentPane = frame.getContentPane();
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
            }
        }
        System.out.println(Arrays.toString(colors));
        int i = 0;
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        for (String s : colors) {
            i++;
            JButton jb = new JButton("Edit color " + i + " of Sonic");
            jb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String name = jb.getText();
                    int i = -1 + (Integer.parseInt(name.split(" ")[2]));
                    colors[i] = GBAColor.toGBA(getColorInput(GBAColor.fromGBA(colors[i])));
                    try {
                        int h1 = Integer.parseInt(colors[i].split("(?<=\\G.{2})")[0], 16);
                        int h2 = Integer.parseInt(colors[i].split("(?<=\\G.{2})")[1], 16);
                        //colors[i] = Integer.toHexString(h2) + Integer.toHexString(h1) + "";
                        raf.seek(0x47AFB8 + (i * 2));
                        raf.write(h2);
                        raf.seek(0x47AFB8 + (i * 2) + 1);
                        raf.write(h1);
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println(Arrays.toString(colors));
                }
            });
            jp.add(jb);
        }
        contentPane.add(jp);
        frame.pack();
    }

    public static Color getColorInput(Color previousColor) {
        Color c = JColorChooser.showDialog(frame, "Pick Color", previousColor);
        return c == null ? previousColor : c;
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
