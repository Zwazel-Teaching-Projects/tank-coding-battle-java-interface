package dev.zwazel.internal.game.tank;

import dev.zwazel.internal.PublicGameWorld;

public interface Tank {
    String getTankType();

    void setWorld(PublicGameWorld world);

    TankConfig getConfig();

    default void move(MoveDirection direction) {
        TankConfig config = getConfig();
        System.out.println("Moving " + direction + " with speed " + config.speed());
    }

    default void rotateBody(RotationDirection direction) {
        System.out.println("Rotating body " + direction);
    }

    default void rotateTurret(RotationDirection direction) {
        System.out.println("Rotating turret " + direction);
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
