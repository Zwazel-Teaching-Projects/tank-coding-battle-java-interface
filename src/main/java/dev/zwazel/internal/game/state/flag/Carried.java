package dev.zwazel.internal.game.state.flag;

/**
 * Represents the state of a flag being carried by a player.
 *
 * @param entityId The entity ID of the player carrying the flag.
 */
public record Carried(long entityId) implements FlagState {
}
