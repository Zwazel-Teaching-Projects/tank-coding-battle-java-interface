package dev.zwazel.internal.game.tank;

public class TankFactory {
    // Creation method
    public static <T extends Tank> T createTank(Class<T> tankType) {
        try {
            return tankType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tank", e);
        }
    }
}
