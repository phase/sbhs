package xyz.jadonfowler.sbhs;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class PaletteManager {
    public static final HashMap<String, String[]> PALETTE_INFO = new HashMap<String, String[]>() {
        private static final long serialVersionUID = 1L;
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
    
    /**
     * "sonic" -> ["23452345", "23452345", ...]
     */
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
        if (name.equals("Sonic")){
            String[] shadowColors = (String[]) Arrays.asList(colors).toArray();
            shadowColors[0] = GBAColor.toGBA(Color.white);
            shadowColors[1] = GBAColor.toGBA(Color.gray);
            PALETTES.put("Ground Shadow", shadowColors);
        }
        // System.out.println(Arrays.toString(colors));
        int i = 0;
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        for (@SuppressWarnings("unused") String s : colors) {
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
        JButton upload = new JButton("  Upload Palette");
        upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Uploading palette " + name);
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(SBHS.frame) == JFileChooser.APPROVE_OPTION) {
                    try {
                        String[] colors = new String[16];
                        BufferedImage img = ImageIO.read(fc.getSelectedFile());
                        for(int x = 0; x < 16; x++) {
                            Color c = new Color(img.getRGB(x, 0));
                            colors[x] = GBAColor.toGBA(c);
                        }
                        PALETTES.remove(name);
                        PALETTES.put(name, colors);
                    }
                    catch (IOException x) {
                        x.printStackTrace();
                    }
                }
            }
        });
        jp.add(upload);
        JButton save = new JButton("  Save Palette");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Saving palette " + name);
                //Create Image
                BufferedImage i = new BufferedImage(16, 1, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < 16; x++) {
                    Color c = GBAColor.fromGBA(PALETTES.get(name)[x]);
                    i.setRGB(x, 0, c.getRGB());
                }
                //Save Image
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(SBHS.frame) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File o = fc.getSelectedFile();
                        ImageIO.write(i, "png", o);
                    }
                    catch (IOException x) {
                        x.printStackTrace();
                    }
                }
            }
        });
        jp.add(save);
        return jp;
    }
}
