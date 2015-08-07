package xyz.jadonfowler.sbse;

import java.util.*;

public class SBString {
    public static final HashMap<Character, Integer> TABLE = new HashMap<Character, Integer>() {
        {
            put('!', 0x01);
            put('"', 0x02);
            put('#', 0x03);
            put('$', 0x04);
            put('%', 0x05);
            put('&', 0x06);
            put('\'', 0x07);
            put('(', 0x08);
            put(')', 0x09);
            put('*', 0x0A);
            put('+', 0x0B);
            put(',', 0x0C);
            put('-', 0x0D);
            put('.', 0x0E);
            put('/', 0x0F);
            put('0', 0x10);
            put('1', 0x11);
            put('2', 0x12);
            put('3', 0x13);
            put('4', 0x14);
            put('5', 0x15);
            put('6', 0x16);
            put('7', 0x17);
            put('8', 0x18);
            put('9', 0x19);
            put(':', 0x1A);
            put(';', 0x1B);
            put('<', 0x1C);
            put('=', 0x1D);
            put('>', 0x1E);
            put('?', 0x1F);
            put('@', 0x20);
            put('A', 0x21);
            put('B', 0x22);
            put('C', 0x23);
            put('D', 0x24);
            put('E', 0x25);
            put('F', 0x26);
            put('G', 0x27);
            put('H', 0x28);
            put('I', 0x29);
            put('J', 0x2A);
            put('K', 0x2B);
            put('L', 0x2C);
            put('M', 0x2D);
            put('N', 0x2E);
            put('O', 0x2F);
            put('P', 0x30);
            put('Q', 0x31);
            put('R', 0x32);
            put('S', 0x33);
            put('T', 0x34);
            put('U', 0x35);
            put('V', 0x36);
            put('W', 0x37);
            put('X', 0x38);
            put('Y', 0x39);
            put('Z', 0x3A);
            put('a', 0x41);
            put('b', 0x42);
            put('c', 0x43);
            put('d', 0x44);
            put('e', 0x45);
            put('f', 0x46);
            put('g', 0x47);
            put('h', 0x48);
            put('i', 0x49);
            put('j', 0x4A);
            put('k', 0x4B);
            put('l', 0x4C);
            put('m', 0x4D);
            put('n', 0x4E);
            put('o', 0x4F);
            put('p', 0x50);
            put('q', 0x51);
            put('r', 0x52);
            put('s', 0x53);
            put('t', 0x54);
            put('u', 0x55);
            put('v', 0x56);
            put('w', 0x57);
            put('x', 0x58);
            put('y', 0x59);
            put('z', 0x5A);
            put(' ', 0x00);
            // Special Stuff
            put('^', 0xFF);
            put('[', 0xFD);
            put(']', 0xFE);
            put('{', 0xFB);
        }
    };
    public static final HashMap<Integer, Character> REVERSE_TABLE = new HashMap<Integer, Character>() {
        {
            for (char c : TABLE.keySet()) {
                int i = TABLE.get(c);
                put(i, c);
            }
        }
    };

    public static int[] to(String s) {
        s = s.replace("\n\n", "]^");
        s = s.replace("\n", "[^");
        int[] to = new int[(s.length() * 2) - 1];
        int k = 0;
        for (int i = 0; i < s.length(); i += 2) {
            char c = s.toCharArray()[k++];
            if (!TABLE.keySet().contains(c)) {
                i -= 2;
                continue;
            }
            to[i] = TABLE.get(c);
            try { // Should fail for the last one
                to[i + 1] = 0; // 0x00
            }
            catch (Exception e) {}
        }
        return to;
    }

    public static String from(int[] t) {
        String s = "";
        int last = -1;
        for (int i : t) {
            if (i == 0 && last == 0) {
                s += " ";
                last = -1;
                continue;
            }
            else if (i == 0) {
                last = 0;
                continue;
            }
            else if (i == 0xFF) {
                switch (last) {
                case 0xFE:
                    s += "\n\n";
                    break;
                case 0xFD:
                    s += "\n";
                    break;
                default:
                    break;
                }
            }
            else if (i == 0xFE || i == 0xFD) {
                last = i;
                continue;// ignore
            }
            else {
                if (REVERSE_TABLE.containsKey(i)) {
                    s += REVERSE_TABLE.get(i);
                    last = -1;
                }
            }
            last = i;
        }
        return s;
    }

    public static String convert(int last, int i) {
        if (i == 0 && last == 0) {
            return " ";
        }
        else if (i == 0xFF) {
            switch (last) {
            case 0xFE:
                return "\n\n";
            case 0xFD:
                return "\n";
            default:
                break;
            }
        }
        else {
            if (REVERSE_TABLE.containsKey(i)) return REVERSE_TABLE.get(i) + "";
        }
        return "";
    }
}
