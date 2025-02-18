package dev.zwazel.internal.game.tank;

public interface TankSupplier<T extends Tank> {
    T create();
}
