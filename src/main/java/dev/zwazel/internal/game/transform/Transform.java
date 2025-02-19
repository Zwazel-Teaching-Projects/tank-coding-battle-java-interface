package dev.zwazel.internal.game.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public Transform(@JsonProperty("position") double[] position,
                     @JsonProperty("rotation") double[] rotation) {
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

    public Transform multiply(Transform child) {
    // Combine rotations: parent's rotation then child's rotation
    Quaternion newRotation = this.rotation.multiply(child.rotation);

    // Rotate the child's position by the parent's rotation and then add the parent's position.
    Vec3 rotatedChildPosition = this.rotation.rotate(child.position);
    Vec3 newPosition = this.position.add(rotatedChildPosition);

    return new Transform(newPosition, newRotation);
}

}
