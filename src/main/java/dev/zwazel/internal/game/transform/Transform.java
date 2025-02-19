package dev.zwazel.internal.game.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Transform {
    private final Vec3 position;
    private final Quaternion rotation;

    @JsonCreator
    public Transform(double[] position, double[] rotation) {
        this.position = new Vec3();
        this.position.setX(position[0]);
        this.position.setY(position[1]);
        this.position.setZ(position[2]);

        this.rotation = new Quaternion();
        this.rotation.setX(rotation[0]);
        this.rotation.setY(rotation[1]);
        this.rotation.setZ(rotation[2]);
        this.rotation.setW(rotation[3]);
    }
}
