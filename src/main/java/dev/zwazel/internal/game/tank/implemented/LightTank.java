package dev.zwazel.internal.game.tank.implemented;

import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.game.tank.ILightTank;
import dev.zwazel.internal.game.tank.TankConfig;
import dev.zwazel.internal.message.data.GameConfig;

import java.util.HashMap;

public class LightTank implements ILightTank {
    private PublicGameWorld world;

    @Override
    public void setWorld(PublicGameWorld world) {
        this.world = world;
    }

    @Override
    public TankConfig getConfig() {
        GameConfig config = world.getGameConfig();
        HashMap<String, TankConfig> configs = config.tankConfigs();
        return configs.get(getTankType());
    }

    @Override
    public String getTankType() {
        return "LIGHT_TANK";
    }
}
