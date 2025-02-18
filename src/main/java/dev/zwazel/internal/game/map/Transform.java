package dev.zwazel.internal.game.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor(force = true)
public class Transform {
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JsonProperty("position")
    private final double[] positionRaw;
    @JsonIgnore
    private final Vec3 position;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JsonProperty("rotation")
    private final double[] rotationRaw;
    @JsonIgnore
    private final Rotation rotation;

    @JsonCreator
    public Transform(
            @JsonProperty("position") double[] position,
            @JsonProperty("rotation") double[] rotation
    ) {
        this.positionRaw = position;
        this.position = new Vec3();
        this.position.setX(position[0]);
        this.position.setY(position[1]);
        this.position.setZ(position[2]);

        this.rotationRaw = rotation;
        this.rotation = new Rotation();
        this.rotation.x = rotation[0];
        this.rotation.y = rotation[1];
        this.rotation.z = rotation[2];
        this.rotation.w = rotation[3];
    }

    public Transform(Vec3 position, Rotation rotation) {
        this.position = position;
        this.positionRaw = new double[]{position.getX(), position.getY(), position.getZ()};
        this.rotation = rotation;
        this.rotationRaw = new double[]{rotation.x, rotation.y, rotation.z, rotation.w};
    }

    @Data
    public static class Rotation {
        double x;
        double y;
        double z;
        double w;
    }
}
