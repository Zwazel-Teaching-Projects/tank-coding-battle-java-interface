package dev.zwazel.internal.config;

import dev.zwazel.GameWorld;
import dev.zwazel.internal.game.tank.Tank;
import lombok.Builder;
import lombok.NonNull;

import java.util.Optional;

@Builder
public record LocalBotConfig(Optional<GameWorld.DebugMode> debugMode, @NonNull String botName,
                             @NonNull Class<? extends Tank> tankType, @NonNull String serverIp, @NonNull int serverPort,
                             @NonNull LobbyConfig lobbyConfig) {
}
