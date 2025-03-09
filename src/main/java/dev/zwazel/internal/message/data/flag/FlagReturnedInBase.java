package dev.zwazel.internal.message.data.flag;

import dev.zwazel.internal.message.MessageData;

/**
 * Represents the message data for when a flag is returned in the base.
 * Received when a flag is returned in the base.
 * This could happen when a player of the same team as the flag's team collects the dropped flag, thus returning it to the base.
 * Or when the enemy team captures the flag and returns it to their base, scoring a point.
 *
 * @param flagId The ID of the flag that got returned in the base.
 */
public record FlagReturnedInBase(long flagId) implements MessageData {
}
