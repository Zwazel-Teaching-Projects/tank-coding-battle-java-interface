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
    private final Vec3 translation;
    private final Quaternion rotation;
    private final Vec3 scale;

    @JsonCreator
    public Transform(@JsonProperty("translation") double[] translation,
                     @JsonProperty("rotation") double[] rotation,
                     @JsonProperty("scale") double[] scale
    ) {
        this.translation = new Vec3();
        this.translation.setX(translation[0]);
        this.translation.setY(translation[1]);
        this.translation.setZ(translation[2]);

        this.rotation = new Quaternion();
        this.rotation.setX(rotation[0]);
        this.rotation.setY(rotation[1]);
        this.rotation.setZ(rotation[2]);
        this.rotation.setW(rotation[3]);

        this.scale = new Vec3();
        this.scale.setX(scale[0]);
        this.scale.setY(scale[1]);
        this.scale.setZ(scale[2]);
    }

    public Transform(Vec3 translation, Quaternion rotation) {
        this(translation, rotation, new Vec3(1, 1, 1));
    }

    public Transform multiply(Transform child) {
        // First, scale the child's translation by the parent's scale
        Vec3 scaledChildTranslation = child.getTranslation().multiply(this.scale);
        // Then rotate and translate
        Vec3 newTranslation = this.rotation.rotate(scaledChildTranslation).add(this.translation);
        // Compose rotations
        Quaternion newRotation = this.rotation.multiply(child.getRotation());
        // Compose scales element-wise
        Vec3 newScale = this.scale.multiply(child.getScale());
        return new Transform(newTranslation, newRotation, newScale);
    }

    // Get my forward, z+ is forward when not rotated
    public Vec3 getForward() {
        return new Vec3(0, 0, 1).ro(rotation);
    }
}
