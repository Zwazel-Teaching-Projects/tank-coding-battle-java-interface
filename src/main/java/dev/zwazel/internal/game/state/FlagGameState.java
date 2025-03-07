package dev.zwazel.internal.game.state;

import dev.zwazel.internal.game.state.flag.FlagState;
import dev.zwazel.internal.game.transform.Transform;

public record FlagGameState(long flagId, String team, Transform transform, FlagState state) {
}
