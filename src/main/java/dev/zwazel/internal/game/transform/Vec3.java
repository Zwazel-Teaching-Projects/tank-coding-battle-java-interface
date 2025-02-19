package dev.zwazel.internal.game.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Vec3 represents a 3D vector in game–space.
 * Game–space convention:
 * forward = +z,
 * up      = +y,
 * right   = -x.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vec3 {
    private double x;
    private double y;
    private double z;

    public static final Vec3 ZERO = new Vec3(0, 0, 0);

    @JsonCreator
    public Vec3(double[] values) {
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
    }

    public Vec3 subtract(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3 normalize() {
        double mag = magnitude();
        return new Vec3(x / mag, y / mag, z / mag);
    }

    public double getAngle() {
        return Math.atan2(z, x);
    }

    public double distance(Vec3 other) {
        return subtract(other).magnitude();
    }
}
