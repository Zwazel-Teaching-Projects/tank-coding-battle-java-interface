package dev.zwazel.internal.config;

import lombok.Builder;
import lombok.NonNull;

import java.util.Optional;

@Builder
public record LobbyConfig(@NonNull String lobbyName, @NonNull String mapName, @NonNull String teamName, Optional<Integer> spawnPoint,
                          boolean fillEmptySlots) {
}
