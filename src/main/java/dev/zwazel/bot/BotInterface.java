package dev.zwazel.bot;

import dev.zwazel.internal.PublicGameWorld;

public interface BotInterface {
    void processTick(PublicGameWorld world);
}
