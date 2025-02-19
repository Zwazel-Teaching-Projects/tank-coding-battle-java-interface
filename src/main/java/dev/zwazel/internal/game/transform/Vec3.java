package dev.zwazel.internal.game.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vec3 {
    private double x;
    private double y;
    private double z;

    @JsonCreator
    public Vec3(double[] values) {
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
    }

    /**
     * Adds the given Vec3 to this Vec3.
     *
     * @param other the Vec3 to add
     * @return a new Vec3 representing the sum of this Vec3 and the given Vec3
     */
    public Vec3 add(Vec3 other) {
        return new Vec3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Subtracts the given Vec3 from this Vec3.
     *
     * @param other the Vec3 to subtract
     * @return a new Vec3 representing the difference between this Vec3 and the given Vec3
     */
    public Vec3 subtract(Vec3 other) {
        return new Vec3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Multiplies this Vec3 by the given scalar.
     *
     * @param scalar the scalar to multiply by
     * @return a new Vec3 representing the product of this Vec3 and the given scalar
     */
    public Vec3 multiply(double scalar) {
        return new Vec3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    /**
     * Divides this Vec3 by the given scalar.
     *
     * @param scalar the scalar to divide by
     * @return a new Vec3 representing the quotient of this Vec3 and the given scalar
     */
    public Vec3 divide(double scalar) {
        return new Vec3(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    /**
     * Calculates the magnitude (length) of this Vec3.
     *
     * @return the magnitude of this Vec3
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalizes this Vec3 (scales it to have a magnitude of 1).
     *
     * @return a new Vec3 representing the normalized version of this Vec3
     */
    public Vec3 normalize() {
        double mag = magnitude();
        return new Vec3(x / mag, y / mag, z / mag);
    }

    /**
     * Calculates the distance between this Vec3 and the given Vec3.
     *
     * @param other the Vec3 to calculate the distance to
     * @return the distance between this Vec3 and the given Vec3
     */
    public double distance(Vec3 other) {
        return this.subtract(other).magnitude();
    }

    /**
     * Calculates the dot product of this Vec3 and the given Vec3.
     *
     * @param other the Vec3 to calculate the dot product with
     * @return the dot product of this Vec3 and the given Vec3
     */
    public double dot(Vec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Calculates the cross product of this Vec3 and the given Vec3.
     *
     * @param other the Vec3 to calculate the cross product with
     * @return a new Vec3 representing the cross product of this Vec3 and the given Vec3
     */
    public Vec3 cross(Vec3 other) {
        return new Vec3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    /**
     * Calculates the angle between this Vec3 and the given Vec3.
     *
     * @param other the Vec3 to calculate the angle with
     * @return the angle between this Vec3 and the given Vec3 in radians
     */
    public double angleBetween(Vec3 other) {
        double dot = this.dot(other);
        double mags = this.magnitude() * other.magnitude();
        return Math.acos(dot / mags);
    }

    /**
     * Gets the yaw angle of this Vec3.
     */
    public double getYaw() {
        return Math.atan2(z, x);
    }

    /**
     * Gets the pitch angle of this Vec3.
     *
     * @return the pitch angle in radians
     */
    public double getPitch() {
        return Math.atan2(y, Math.sqrt(x * x + z * z));
    }
}