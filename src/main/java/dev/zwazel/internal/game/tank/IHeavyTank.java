package dev.zwazel.internal.game.tank;

public interface IHeavyTank extends Tank{
    @Override
    default TankType getTankType() {
        return TankType.HEAVY_TANK;
    }
}
