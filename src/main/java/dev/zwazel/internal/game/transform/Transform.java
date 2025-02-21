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

    public static Transform combineTransforms(Transform parent, Transform child) {
        // Combine scale component-wise
        Vec3 globalScale = parent.getScale().multiply(child.getScale());

        // Apply parent's scale to child's translation, then rotate, then translate
        Vec3 scaledChildTranslation = child.getTranslation().multiply(parent.getScale());
        Vec3 rotatedTranslation = parent.getRotation().apply(scaledChildTranslation);
        Vec3 globalTranslation = parent.getTranslation().add(rotatedTranslation);

        // Combine rotations by multiplication
        Quaternion globalRotation = parent.getRotation().multiply(child.getRotation());

        return new Transform(globalTranslation, globalRotation, globalScale);
    }

    public Vec3 left() {
        return this.rotation.left();
    }

    public Vec3 right() {
        return this.rotation.right();
    }

    public Vec3 up() {
        return this.rotation.up();
    }

    public Vec3 down() {
        return this.rotation.down();
    }

    public Vec3 forward() {
        return this.rotation.forward();
    }

    public Vec3 backward() {
        return this.rotation.backward();
    }
}
