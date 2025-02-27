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
    public static final Vec3 ZERO = new Vec3(0, 0, 0);
    public static final Vec3 X = new Vec3(1, 0, 0);
    public static final Vec3 Y = new Vec3(0, 1, 0);
    public static final Vec3 Z = new Vec3(0, 0, 1);
    private double x;
    private double y;
    private double z;

    @JsonCreator
    public Vec3(double[] values) {
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
    }

    public Vec3 subtract(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(x + other.x, y + other.y, z + other.z);
    }

    public Vec3 multiply(Vec3 other) {
        return new Vec3(x * other.x, y * other.y, z * other.z);
    }

    public Vec3 multiply(double scalar) {
        return new Vec3(x * scalar, y * scalar, z * scalar);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3 normalize() {
        double mag = magnitude();
        return new Vec3(x / mag, y / mag, z / mag);
    }

    public Vec3 normalize_or_zero() {
        double mag = magnitude();
        if (mag == 0) {
            return Vec3.ZERO;
        }
        return new Vec3(x / mag, y / mag, z / mag);
    }

    public double getAngle() {
        return Math.atan2(x, z);
    }

    public double distance(Vec3 other) {
        return subtract(other).magnitude();
    }

    public Vec3 cross(Vec3 other) {
        return new Vec3(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    public double dot(Vec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vec3 scale(double scalar) {
        return new Vec3(x * scalar, y * scalar, z * scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }
}
