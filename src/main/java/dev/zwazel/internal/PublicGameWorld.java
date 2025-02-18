package dev.zwazel.internal;

import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.game.tank.Tank;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.GameState;

import java.util.List;

public interface PublicGameWorld {
    /**
     * Returns if the game is currently running.
     *
     * @return true if the game is running, false otherwise
     */
    boolean isRunning();

    /**
     * Returns if the game is currently in debug mode.
     *
     * @return true if the game is in debug mode, false otherwise
     */
    boolean isDebug();

    /**
     * Adds a message to the outgoing message queue.
     *
     * @param message the message to send
     */
    void send(MessageContainer message);

    /**
     * Get the current game state.
     *
     * @return the current game state
     */
    GameState getGameState();

    /**
     * Get my current state.
     *
     * @return my current state
     */
    ClientState getMyState();

    /**
     * Get the state of a specific client.
     *
     * @param clientId the client id
     * @return the client state
     */
    ClientState getClientState(Long clientId);

    /**
     * Get the client states of all clients in a team (could include the client itself, if it is part of the team).
     *
     * @param teamName the team name
     * @return the client states of all clients in the team
     */
    default List<ClientState> getTeamClientStates(String teamName) {
        return getTeamClientStates(teamName, null);
    }

    /**
     * Get the client states of all clients in a team, excluding a specific client.
     *
     * @param teamName        the team name
     * @param excludeClientId the client id to exclude
     * @return the client states of all clients in the team
     */
    List<ClientState> getTeamClientStates(String teamName, Long excludeClientId);

    /**
     * Get the tank instance of the client.
     * Cast to the correct tank type to access tank-specific methods.
     *
     * @return the tank interface instance
     */
    Tank getTank();

    /**
     * Get the game configuration.
     *
     * @return the game configuration
     */
    GameConfig getGameConfig();

    /**
     * Returns a copy of the current tick's incoming messages.
     *
     * @return a list of incoming messages
     */
    List<MessageContainer> getIncomingMessages();
}
