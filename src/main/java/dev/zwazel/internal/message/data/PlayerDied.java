package dev.zwazel.internal.message.data;

import dev.zwazel.internal.message.MessageData;

/**
 * Message data for when a player dies.
 * Received when a player dies.
 *
 * @param entityId The ID of the entity that died.
 */
public record PlayerDied(long entityId) implements MessageData {
}
