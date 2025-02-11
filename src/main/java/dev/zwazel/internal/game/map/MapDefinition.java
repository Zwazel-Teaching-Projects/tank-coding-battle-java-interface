package dev.zwazel.internal.game.map;

import java.util.Arrays;

public record MapDefinition(long width, long height,
                            float[][] tiles,
                            LayerDefinition[] layers,
                            MarkerDefinition[] markers) {
    @Override
    public String toString() {
        return "MapDefinition{" +
                "width=" + width +
                ", height=" + height +
                ", tiles=" + Arrays.deepToString(tiles) +
                ", layers=" + Arrays.toString(layers) +
                ", markers=" + Arrays.toString(markers) +
                '}';
    }
}
