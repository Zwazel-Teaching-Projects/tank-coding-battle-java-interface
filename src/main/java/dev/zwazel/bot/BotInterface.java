package dev.zwazel.bot;

import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.message.data.GameConfig;

public interface BotInterface {
    void setup(PublicGameWorld world, GameConfig config);

    void processTick(PublicGameWorld world);
}
