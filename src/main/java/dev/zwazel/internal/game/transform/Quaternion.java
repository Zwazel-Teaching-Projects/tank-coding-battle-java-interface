package dev.zwazel.internal.game.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Quaternion class uses standard math internally but converts to/from game–space:
 * Game: forward = +z, up = +y, right = –x.
 * (In standard math: forward = +z, up = +y, right = +x.)
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

    public Vec3 rotateVector(Vec3 v) {
        Quaternion q = new Quaternion(v.getX(), v.getY(), v.getZ(), 0);
        Quaternion result = this.multiply(q).multiply(this.inverse());
        return new Vec3(result.x, result.y, result.z);
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
        return rotateVector(new Vec3(0, 0, 1));
    }

    public Vec3 getUp() {
        return rotateVector(new Vec3(0, 1, 0));
    }

    public Vec3 getRight() {
        // In game space, “right” is –x.
        return rotateVector(new Vec3(-1, 0, 0));
    }
}