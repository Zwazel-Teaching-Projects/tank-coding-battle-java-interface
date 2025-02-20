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

    public Vec3 rotate(Vec3 v) {
        // Convert the vector into a quaternion with zero scalar part
        Quaternion vQuat = new Quaternion(v.getX(), v.getY(), v.getZ(), 0);

        // Conjugate of the quaternion (inverse if unit quaternion)
        Quaternion conjugate = this.conjugate();

        // The rotated vector is: this * vQuat * this^-1
        Quaternion resultQuat = this.multiply(vQuat).multiply(conjugate);

        // The vector part of the result holds the rotated vector
        return new Vec3(resultQuat.getX(), resultQuat.getY(), resultQuat.getZ());
    }

    public Quaternion multiply(Quaternion other) {
        double x = this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y;
        double y = this.w * other.y - this.x * other.z + this.y * other.w + this.z * other.x;
        double z = this.w * other.z + this.x * other.y - this.y * other.x + this.z * other.w;
        double w = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
        return new Quaternion(x, y, z, w);
    }

    public Quaternion inverse() {
        double norm = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
        return new Quaternion(-this.x / norm, -this.y / norm, -this.z / norm, this.w / norm);
    }

    public Vec3 getForward() {
        return rotate(new Vec3(0, 0, 1));
    }

    public Vec3 getUp() {
        return rotate(new Vec3(0, 1, 0));
    }

    public Vec3 getRight() {
        // In game space, “right” is –x.
        return rotate(new Vec3(-1, 0, 0));
    }

    public double getPitch() {
        return Math.asin(-2 * (x * z - w * y));
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }
}