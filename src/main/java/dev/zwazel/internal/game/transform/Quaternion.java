package dev.zwazel.internal.game.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Game (right-handed coordinate system): forward = +z, up = +y, right = –x.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quaternion {
    private double x;
    private double y;
    private double z;
    private double w;

    @JsonCreator
    public Quaternion(double[] values) {
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
        this.w = values[3];
    }

    public static Quaternion fromAxisAngle(double x, double y, double z, double angle) {
        double halfAngle = angle / 2;
        double sinHalfAngle = Math.sin(halfAngle);
        return new Quaternion(x * sinHalfAngle, y * sinHalfAngle, z * sinHalfAngle, Math.cos(halfAngle));
    }

    public Quaternion multiply(Quaternion other) {
        double x = this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y;
        double y = this.w * other.y - this.x * other.z + this.y * other.w + this.z * other.x;
        double z = this.w * other.z + this.x * other.y - this.y * other.x + this.z * other.w;
        double w = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
        return new Quaternion(x, y, z, w);
    }

    public Vec3 multiply(Vec3 v) {
        Quaternion vQuat = new Quaternion(v.getX(), v.getY(), v.getZ(), 0);
        Quaternion resultQuat = this.multiply(vQuat).multiply(this.conjugate());
        return new Vec3(resultQuat.getX(), resultQuat.getY(), resultQuat.getZ());
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    public Vec3 apply(Vec3 v) {
        // Convert the vector into a pure quaternion (0, v.x, v.y, v.z)
        Quaternion vectorQuat = new Quaternion(v.getX(), v.getY(), v.getZ(), 0);

        // For a unit quaternion, the inverse is simply its conjugate.
        Quaternion qConjugate = this.conjugate();

        // Perform the rotation: result = this * vectorQuat * this^-1 (which is the conjugate here)
        Quaternion result = this.multiply(vectorQuat).multiply(qConjugate);

        // Extract and return the rotated vector
        return new Vec3(result.getX(), result.getY(), result.getZ());
    }

    public Vec3 forward() {
        return this.apply(Vec3.Z);
    }

    public Vec3 up() {
        return this.apply(Vec3.Y);
    }

    public Vec3 right() {
        return this.apply(Vec3.X);
    }

    public Vec3 backward() {
        return this.apply(Vec3.Z.multiply(-1));
    }

    public Vec3 down() {
        return this.apply(Vec3.Y.multiply(-1));
    }

    public Vec3 left() {
        return this.apply(Vec3.X.multiply(-1));
    }

    public double getPitch() {
        return Math.atan2(2 * (w * x + y * z), 1 - 2 * (x * x + y * y));
    }

    public double getYaw() {
        return Math.atan2(2 * (w * z + x * y), 1 - 2 * (y * y + z * z));
    }
}