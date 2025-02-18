package dev.zwazel.internal.game.tank.implemented;

import dev.zwazel.internal.game.tank.ILightTank;
import dev.zwazel.internal.game.tank.TankFactory;

public class LightTank implements ILightTank {
    static {
        TankFactory.register(LightTank.class, LightTank::new);
    }
}
