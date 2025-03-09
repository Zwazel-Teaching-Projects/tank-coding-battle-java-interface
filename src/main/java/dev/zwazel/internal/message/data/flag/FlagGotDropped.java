package dev.zwazel.internal.message.data.flag;

import dev.zwazel.internal.message.MessageData;

/**
 * Represents the message data for when a flag is dropped.
 * Received when a flag is dropped.
 *
 * @param flagId    The ID of the flag that got dropped.
 * @param carrierId The ID of the client that was carrying the flag before it got dropped.
 */
public record FlagGotDropped(long flagId, long carrierId) implements MessageData {
}
