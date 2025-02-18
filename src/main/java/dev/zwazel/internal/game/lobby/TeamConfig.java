package dev.zwazel.internal.game.lobby;

import dev.zwazel.internal.game.misc.SimplifiedRGB;

public record TeamConfig(String teamName, SimplifiedRGB color, long maxPlayers) {

}
