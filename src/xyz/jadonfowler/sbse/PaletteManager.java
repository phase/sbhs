package xyz.jadonfowler.sbse;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class PaletteManager {
    public static final HashMap<String, String[]> PALETTE_INFO = new HashMap<String, String[]>() {
        {
            String[] g = { "Background (useless)", "Eye color and shoe/glove reflection",
                    "Above the eye, shoe/glove color", "Outside of shoe/glove", "Outline of shoe/glove",
                    "In-ball shine", "Primary fur color", "Secondary fur color", "Third fur color/outline",
                    "Primary skin color", "Secondary skin color", "Third skin color", "Primary shoe color",
                    "Secondary shoe color", "Third shoe color", "Eye Color" };
            put("Sonic", g);
            put("Knuckles", g);
            put("Tails",
                    new String[] { "Background (useless)", "Eye color and shoe/glove reflection",
                            "Above the eye, tail/hand color", "Primary outline", "Secondary outline", "In-ball shine",
                            "Primary fur color", "Secondary fur color", "Third fur color/outline", "Primary skin color",
                            "Secondary skin color", "Third skin color", "Primary shoe color & Weapons",
                            "Secondary shoe color & Weapons", "Third shoe color & Weapons", "Eye Color" });
            put("Amy", new String[] { "Background (useless)", "Eye color and shoe/hammer reflection",
                    "Above the eye, shoe/glove color" });
        }
    };
    public static HashMap<String, String[]> PALETTES = new HashMap<String, String[]>();

    public static void addPaletteTab(JTabbedPane pane, String name, int offset) throws Exception {
        pane.addTab(name, null, createPalettePanel(name, offset), "Edit " + name + " Palette");
    }

    public static JPanel createPalettePanel(String name, int hex) throws Exception {
        String[] colors = new String[16];
        int color = 0;
        String f = "";
        for (int i = hex; i <= hex + 32; i++) {
            SBHS.raf.seek(i);
            int value = SBHS.raf.read();
            // System.out.print(Integer.toHexString(value));
            if (f.length() == 4) {
                f = f.split("(?<=\\G.{2})")[1] + f.split("(?<=\\G.{2})")[0];
                colors[color++] = f;
                f = SBHS.hex(value);
            }
            else {
                f += SBHS.hex(value);
            }
        }
        PALETTES.put(name, colors);
        // System.out.println(Arrays.toString(colors));
        int i = 0;
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        for (String s : colors) {
            i++;
            JButton jb = new JButton(name + " Color " + i + ": " + (PALETTE_INFO.get(name) == null ? "Something?"
                    : PALETTE_INFO.get(name).length < i ? "Something?" : PALETTE_INFO.get(name)[i - 1]));
            jb.addActionListener(new ActionListener() {
                final String character = name;

                public void actionPerformed(ActionEvent e) {
                    String name = jb.getText();
                    int i = -1 + (Integer.parseInt(name.split(" ")[2].replace(":", "")));
                    colors[i] = GBAColor.toGBA(SBHS.getColorInput(GBAColor.fromGBA(colors[i])));
                    PALETTES.remove(character);
                    PALETTES.put(character, colors);
                    try {
                        int h1 = Integer.parseInt(colors[i].split("(?<=\\G.{2})")[0], 16);
                        int h2 = Integer.parseInt(colors[i].split("(?<=\\G.{2})")[1], 16);
                        // colors[i] = Integer.toHexString(h2) +
                        // Integer.toHexString(h1) + "";
                        SBHS.raf.seek(hex + (i * 2));
                        SBHS.raf.write(h2);
                        SBHS.raf.seek(hex + (i * 2) + 1);
                        SBHS.raf.write(h1);
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    // System.out.println(Arrays.toString(colors));
                }
            });
            jp.add(jb);
        }
        return jp;
    }
}
