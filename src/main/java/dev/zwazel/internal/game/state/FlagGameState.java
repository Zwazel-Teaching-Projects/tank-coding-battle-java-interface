package dev.zwazel.internal.game.state;

import dev.zwazel.internal.game.state.flag.FlagState;
import dev.zwazel.internal.game.transform.Transform;
import dev.zwazel.internal.game.transform.Vec3;

public record FlagGameState(long flagId, long flagBaseId, Vec3 colliderSize, String team, Transform transform,
                            FlagState state) {
}
