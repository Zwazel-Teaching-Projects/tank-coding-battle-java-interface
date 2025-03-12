package dev.zwazel.internal.game.misc;

public record SimplifiedRGB(float r, float g, float b) {
    public java.awt.Color toColor() {
        return new java.awt.Color(r, g, b);
    }
}
