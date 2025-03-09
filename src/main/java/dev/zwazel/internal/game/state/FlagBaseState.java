package dev.zwazel.internal.game.state;

import dev.zwazel.internal.game.transform.Transform;
import dev.zwazel.internal.game.transform.Vec3;

public record FlagBaseState(long flagId, long flagBaseId, String team, Transform transform, Vec3 colliderSize,
                            boolean flagInBase) {
}
