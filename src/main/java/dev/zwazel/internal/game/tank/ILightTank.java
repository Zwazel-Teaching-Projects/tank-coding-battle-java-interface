package dev.zwazel.internal.game.tank;

public interface ILightTank extends Tank {
    @Override
    default TankType getTankType() {
        return TankType.LIGHT_TANK;
    }

    // TODO: Define light tank specific methods (if any)
}
