package dev.zwazel.internal.message.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.connection.client.ConnectedClientConfig;
import dev.zwazel.internal.game.lobby.TeamConfig;
import dev.zwazel.internal.game.map.MapDefinition;
import dev.zwazel.internal.game.tank.TankConfig;
import dev.zwazel.internal.game.tank.TankType;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Builder
public record GameConfig(long tickRate, long clientId, MapDefinition mapDefinition,
                         ConnectedClientConfig[] connectedClients,
                         HashMap<String, TeamConfig> teamConfigs,
                         HashMap<TankType, TankConfig> tankConfigs) implements MessageData {
    @Override
    public boolean applyOnReceive(InternalGameWorld internalWorld) {
        internalWorld.setGameConfig(this);

        if (internalWorld.isInternalDebug()) {
            System.out.println("Received Game Config from Server:\n\t" + this);
        }

        return false;
    }

    public ConnectedClientConfig getMyConfig() {
        return getClientConfig(clientId).orElseThrow();
    }

    public Optional<ConnectedClientConfig> getClientConfig(String clientName) {
        return Arrays.stream(connectedClients)
                .filter(clientConfig -> clientConfig.clientName().equals(clientName))
                .findFirst();
    }

    public Optional<ConnectedClientConfig> getClientConfig(long clientId) {
        return Arrays.stream(connectedClients)
                .filter(clientConfig -> clientConfig.clientId() == clientId)
                .findFirst();
    }

    public Optional<TeamConfig> getTeamConfig(String teamName) {
        return Optional.ofNullable(teamConfigs.get(teamName));
    }

    public TeamConfig getMyTeamConfig() {
        return getTeamConfig(getMyConfig().clientTeam()).orElseThrow();
    }

    /**
     * Gets team members. Can include the client itself, if it is part of the team.
     *
     * @param teamName the team name
     * @return the team members
     */
    public List<ConnectedClientConfig> getTeamMembers(String teamName) {
        return getTeamMembers(teamName, null);
    }

    /**
     * Gets team members. Can exclude a specific client from the list.
     *
     * @param teamName        the team name
     * @param excludeClientId the exclude client id
     * @return the team members
     */
    public List<ConnectedClientConfig> getTeamMembers(String teamName, Long excludeClientId) {
        if (!teamConfigs.containsKey(teamName)) {
            throw new IllegalArgumentException("Team " + teamName + " does not exist in the game configuration.");
        }

        return Arrays.stream(connectedClients)
                .filter(clientConfig -> clientConfig.clientTeam().equals(teamName))
                .filter(clientConfig -> excludeClientId == null || clientConfig.clientId() != excludeClientId)
                .toList();
    }

    public Optional<TankConfig> getTankConfig(TankType tankType) {
        return Optional.ofNullable(tankConfigs.get(tankType));
    }
}
