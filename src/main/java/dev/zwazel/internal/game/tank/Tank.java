package dev.zwazel.internal.game.tank;

public interface Tank {
    default void move(MoveDirection direction) {
        System.out.println("Moving " + direction);
    }

    default void rotate(RotationDirection direction) {
        System.out.println("Rotating " + direction);
    }

    enum MoveDirection {
        FORWARD,
        BACKWARD,
    }

    enum RotationDirection {
        CLOCKWISE,
        COUNTER_CLOCKWISE,
    }
}
