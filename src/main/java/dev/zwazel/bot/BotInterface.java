package dev.zwazel.bot;

import dev.zwazel.internal.PublicGameWorld;

/*
 TODO: This is the interface that the students will implement. they should not implement/extend the specific tanks
  instead they should implement this interface and the game will provide them with the implemented bot interface
  that will be used to interact with the game world.
 */
public interface BotInterface {
    void setup(PublicGameWorld world);

    void processTick(PublicGameWorld world);
}
