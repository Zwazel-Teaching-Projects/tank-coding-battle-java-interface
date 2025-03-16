package dev.zwazel.internal.config;

import dev.zwazel.GameWorld;
import dev.zwazel.internal.game.tank.Tank;
import lombok.Builder;

@Builder
public record LocalBotConfig(GameWorld.DebugMode debugMode, String botName, Class<? extends Tank> tankType, String serverIp, int serverPort,
                             LobbyConfig lobbyConfig) {
}
