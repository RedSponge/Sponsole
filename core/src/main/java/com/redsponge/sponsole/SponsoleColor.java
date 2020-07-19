package com.redsponge.sponsole;

import com.badlogic.gdx.graphics.Color;

public class SponsoleColor {

    public static final SponsoleColor ERROR = new SponsoleColor(1f, 0.2f, 0.2f, 1.0f);
    public static final SponsoleColor WHITE = new SponsoleColor(Color.WHITE);
    public static final SponsoleColor LIGHT_GRAY = new SponsoleColor(Color.LIGHT_GRAY);
    public static final SponsoleColor GRAY = new SponsoleColor(Color.GRAY);
    public static final SponsoleColor DARK_GRAY = new SponsoleColor(Color.DARK_GRAY);
    public static final SponsoleColor BLACK = new SponsoleColor(Color.BLACK);
    public static final SponsoleColor CLEAR = new SponsoleColor(Color.CLEAR);
    public static final SponsoleColor BLUE = new SponsoleColor(Color.BLUE);
    public static final SponsoleColor NAVY = new SponsoleColor(Color.NAVY);
    public static final SponsoleColor ROYAL = new SponsoleColor(Color.ROYAL);
    public static final SponsoleColor SLATE = new SponsoleColor(Color.SLATE);
    public static final SponsoleColor SKY = new SponsoleColor(Color.SKY);
    public static final SponsoleColor CYAN = new SponsoleColor(Color.CYAN);
    public static final SponsoleColor TEAL = new SponsoleColor(Color.TEAL);
    public static final SponsoleColor GREEN = new SponsoleColor(Color.GREEN);
    public static final SponsoleColor CHARTREUSE = new SponsoleColor(Color.CHARTREUSE);
    public static final SponsoleColor LIME = new SponsoleColor(Color.LIME);
    public static final SponsoleColor FOREST = new SponsoleColor(Color.FOREST);
    public static final SponsoleColor OLIVE = new SponsoleColor(Color.OLIVE);
    public static final SponsoleColor YELLOW = new SponsoleColor(Color.YELLOW);
    public static final SponsoleColor GOLD = new SponsoleColor(Color.GOLD);
    public static final SponsoleColor GOLDENROD = new SponsoleColor(Color.GOLDENROD);
    public static final SponsoleColor ORANGE = new SponsoleColor(Color.ORANGE);
    public static final SponsoleColor BROWN = new SponsoleColor(Color.BROWN);
    public static final SponsoleColor TAN = new SponsoleColor(Color.TAN);
    public static final SponsoleColor FIREBRICK = new SponsoleColor(Color.FIREBRICK);
    public static final SponsoleColor RED = new SponsoleColor(Color.RED);
    public static final SponsoleColor SCARLET = new SponsoleColor(Color.SCARLET);
    public static final SponsoleColor CORAL = new SponsoleColor(Color.CORAL);
    public static final SponsoleColor SALMON = new SponsoleColor(Color.SALMON);
    public static final SponsoleColor PINK = new SponsoleColor(Color.PINK);
    public static final SponsoleColor MAGENTA = new SponsoleColor(Color.MAGENTA);
    public static final SponsoleColor PURPLE = new SponsoleColor(Color.PURPLE);
    public static final SponsoleColor VIOLET = new SponsoleColor(Color.VIOLET);
    public static final SponsoleColor MAROON = new SponsoleColor(Color.MAROON);

    private int r, g, b, a;
    private String cachedRepresentation;

    public SponsoleColor(float r, float g, float b, float a) {
        set(r, g, b, a);
    }

    public SponsoleColor(int r, int g, int b, int a) {
        set(r, g, b, a);
    }

    public SponsoleColor(Color c) {
        set(c.r, c.g, c.b, c.a);
    }

    public void refreshRepresentation() {
        String hexR = String.format("%02x", r);
        String hexG = String.format("%02x", g);
        String hexB = String.format("%02x", b);
        String hexA = String.format("%02x", a);
        cachedRepresentation = hexR + hexG + hexB + hexA;
    }

    public SponsoleColor clamp() {
        if (r < 0)
            r = 0;
        else if (r > 255) r = 255;

        if (g < 0)
            g = 0;
        else if (g > 255) g = 255;

        if (b < 0)
            b = 0;
        else if (b > 255) b = 255;

        if (a < 0)
            a = 0;
        else if (a > 255) a = 255;
        return this;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }

    public SponsoleColor setR(byte r) {
        this.r = Math.max(0, Math.min(1, r));
        refreshRepresentation();
        return this;
    }

    public SponsoleColor setG(byte g) {
        this.g = Math.max(0, Math.min(1, g));
        refreshRepresentation();
        return this;
    }

    public SponsoleColor setB(byte b) {
        this.b = Math.max(0, Math.min(1, b));
        refreshRepresentation();
        return this;
    }

    public SponsoleColor setA(byte a) {
        this.a = Math.max(0, Math.min(1, a));
        refreshRepresentation();
        return this;
    }

    public SponsoleColor set(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        clamp();
        refreshRepresentation();
        return this;
    }

    public SponsoleColor set(float r, float g, float b, float a) {
        return set((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));
    }

    @Override
    public String toString() {
        return "[#" + cachedRepresentation + "]";
    }
}
