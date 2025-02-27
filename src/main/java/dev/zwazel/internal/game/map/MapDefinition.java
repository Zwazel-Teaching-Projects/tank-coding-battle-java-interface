package dev.zwazel.internal.game.map;

import dev.zwazel.internal.game.misc.SimplifiedRGB;

import java.util.Arrays;

public record MapDefinition(long width, long depth, SimplifiedRGB floorColor,
                            float[][] tiles,
                            LayerDefinition[] layers,
                            MarkerDefinition[] markers) {
    @Override
    public String toString() {
        return "MapDefinition{" +
                "width=" + width +
                ", depth=" + depth +
                ", tiles=" + Arrays.deepToString(tiles) +
                ", layers=" + Arrays.toString(layers) +
                ", markers=" + Arrays.toString(markers) +
                '}';
    }
}
