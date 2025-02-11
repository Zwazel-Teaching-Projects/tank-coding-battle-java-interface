package dev.zwazel.internal.game.map;

public record LayerDefinition(LayerType kind, float costModifier, TileDefinition[] tiles) {

    public enum LayerType {
        FOREST,
    }
}
