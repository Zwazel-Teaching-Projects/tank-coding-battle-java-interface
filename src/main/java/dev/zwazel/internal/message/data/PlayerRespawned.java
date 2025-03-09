package dev.zwazel.internal.message.data;

import dev.zwazel.internal.message.MessageData;

/**
 * Message data for when a player respawns.
 * Received when a player respawns.
 *
 * @param entityId The ID of the entity that respawned.
 */
public record PlayerRespawned(long entityId) implements MessageData {
}
