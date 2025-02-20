package dev.zwazel.internal.game.state;

import dev.zwazel.internal.game.transform.Transform;

public record ClientState(long id, Transform transformBody, Transform transformTurret,
                          Transform globalTransformTurret) {
}
