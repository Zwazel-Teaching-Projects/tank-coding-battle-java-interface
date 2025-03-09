package dev.zwazel.internal.message.data;

import dev.zwazel.internal.game.tank.TankType;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

/**
 * Message data for when a bot makes first contact with the server.
 * This is the first message sent by a bot to the server.
 * Used to register the bot with the server, and do initial setup.
 *
 * @param botName               The name of the bot.
 * @param lobbyName             The name of the lobby the bot is joining.
 * @param mapName               The name of the map the bot is joining.
 * @param teamName              The name of the team the bot is joining.
 * @param clientType            The type of client the bot is (player or spectator).
 * @param botAssignedSpawnPoint The spawn point the bot is assigned.
 * @param tankType              The type of tank the bot is using. can be null if the bot is a spectator.
 */
@Builder
public record FirstContact(String botName, String lobbyName, String mapName, String teamName,
                           ClientType clientType, Long botAssignedSpawnPoint,
                           TankType tankType) implements MessageData {
    public enum ClientType {
        PLAYER,
        SPECTATOR
    }
}