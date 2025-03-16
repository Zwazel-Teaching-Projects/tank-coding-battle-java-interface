package dev.zwazel.internal.config;

import lombok.Builder;

import java.util.Optional;

@Builder
public record LobbyConfig(String lobbyName, String mapName, String teamName, Optional<Integer> spawnPoint,
                          boolean fillEmptySlots) {
}
