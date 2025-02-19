package dev.zwazel.internal.game.map.marker;

import dev.zwazel.internal.game.map.MarkerType;

public record Spawn(long spawnNumber, LookDirection lookDirection) implements MarkerType {
    enum LookDirection {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
