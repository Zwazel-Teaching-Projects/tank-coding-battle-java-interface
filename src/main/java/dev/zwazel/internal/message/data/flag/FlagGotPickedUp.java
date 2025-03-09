package dev.zwazel.internal.message.data.flag;

import dev.zwazel.internal.message.MessageData;

/**
 * Represents the message data for when a flag is picked up.
 * Received when a flag is picked up.
 *
 * @param flagId    The ID of the flag that got picked up.
 * @param carrierId The ID of the client that picked up the flag.
 */
public record FlagGotPickedUp(long flagId, long carrierId) implements MessageData {
}
