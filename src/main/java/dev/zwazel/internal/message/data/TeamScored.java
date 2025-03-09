package dev.zwazel.internal.message.data;

import dev.zwazel.internal.message.MessageData;

/**
 * Represents the message data for when a team scores a point.
 * Received when a team scores a point.
 *
 * @param scorer The ID of the client that scored the point.
 * @param team   The name of the team that scored the point.
 * @param score  The new score of the team that scored the point.
 */
public record TeamScored(long scorer, String team, long score) implements MessageData {
}
