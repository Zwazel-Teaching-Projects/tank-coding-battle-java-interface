package dev.zwazel.internal.game.tank;

public interface ISelfPropelledArtillery extends Tank {
    @Override
    default TankType getTankType() {
        return TankType.SELF_PROPELLED_ARTILLERY;
    }
}
