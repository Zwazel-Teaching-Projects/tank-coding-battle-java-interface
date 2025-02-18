package dev.zwazel.internal.game.tank;

import java.util.HashMap;
import java.util.Map;

public class TankFactory {
    private static final Map<Class<? extends Tank>, TankSupplier<? extends Tank>> registry = new HashMap<>();

    public static <T extends Tank> void register(Class<T> tankType, TankSupplier<T> supplier) {
        registry.put(tankType, supplier);
    }

    public static <T extends Tank> T createTank(Class<T> tankType) {
        TankSupplier<T> supplier = (TankSupplier<T>) registry.get(tankType);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown tank type: " + tankType);
        }
        return supplier.create();
    }
}
