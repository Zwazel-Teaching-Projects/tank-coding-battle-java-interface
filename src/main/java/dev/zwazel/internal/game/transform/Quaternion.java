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

    /**
     * Creates a quaternion from an axis and an angle.
     *
     * @param axisX the x component of the axis
     * @param axisY the y component of the axis
     * @param axisZ the z component of the axis
     * @param angle the angle in radians
     * @return a new Quaternion representing the rotation
     */
    public static Quaternion fromAxisAngle(double axisX, double axisY, double axisZ, double angle) {
        double halfAngle = angle / 2;
        double sinHalfAngle = Math.sin(halfAngle);
        double cosHalfAngle = Math.cos(halfAngle);
        return new Quaternion(
                axisX * sinHalfAngle,
                axisY * sinHalfAngle,
                axisZ * sinHalfAngle,
                cosHalfAngle
        );
    }

    /**
     * Performs spherical linear interpolation (slerp) between two quaternions.
     *
     * @param q1 the start quaternion
     * @param q2 the end quaternion
     * @param t  the interpolation factor (0.0 to 1.0)
     * @return a new Quaternion representing the interpolated rotation
     */
    public static Quaternion slerp(Quaternion q1, Quaternion q2, double t) {
        // Normalize quaternions
        q1 = q1.normalize();
        q2 = q2.normalize();

        double dot = q1.x * q2.x + q1.y * q2.y + q1.z * q2.z + q1.w * q2.w;
        // If the dot product is negative, slerp won't take the shorter path.
        if (dot < 0.0f) {
            q2 = new Quaternion(-q2.x, -q2.y, -q2.z, -q2.w);
            dot = -dot;
        }

        final double DOT_THRESHOLD = 0.995;
        if (dot > DOT_THRESHOLD) {
            // If the quaternions are very close, use linear interpolation
            return new Quaternion(
                    q1.x + t * (q2.x - q1.x),
                    q1.y + t * (q2.y - q1.y),
                    q1.z + t * (q2.z - q1.z),
                    q1.w + t * (q2.w - q1.w)
            ).normalize();
        }

        double theta0 = Math.acos(dot);
        double theta = theta0 * t;
        double sinTheta = Math.sin(theta);
        double sinTheta0 = Math.sin(theta0);

        double s0 = Math.cos(theta) - dot * sinTheta / sinTheta0;
        double s1 = sinTheta / sinTheta0;

        return new Quaternion(
                (q1.x * s0) + (q2.x * s1),
                (q1.y * s0) + (q2.y * s1),
                (q1.z * s0) + (q2.z * s1),
                (q1.w * s0) + (q2.w * s1)
        );
    }

    /**
     * Multiplies this quaternion by another quaternion.
     *
     * @param q the quaternion to multiply by
     * @return a new Quaternion representing the product
     */
    public Quaternion multiply(Quaternion q) {
        double nw = this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z;
        double nx = this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y;
        double ny = this.w * q.y - this.x * q.z + this.y * q.w + this.z * q.x;
        double nz = this.w * q.z + this.x * q.y - this.y * q.x + this.z * q.w;
        return new Quaternion(nx, ny, nz, nw);
    }

    /**
     * Returns the conjugate of this quaternion.
     *
     * @return a new Quaternion representing the conjugate
     */
    public Quaternion conjugate() {
        return new Quaternion(-this.x, -this.y, -this.z, this.w);
    }

    /**
     * Calculates the squared norm of this quaternion.
     *
     * @return the squared norm
     */
    public double normSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }

    /**
     * Returns the inverse of this quaternion.
     *
     * @return a new Quaternion representing the inverse
     * @throws ArithmeticException if the quaternion is zero
     */
    public Quaternion inverse() {
        double normSq = normSquared();
        if (normSq == 0) {
            throw new ArithmeticException("Cannot invert a zero quaternion.");
        }
        Quaternion conj = conjugate();
        return new Quaternion(conj.x / normSq, conj.y / normSq, conj.z / normSq, conj.w / normSq);
    }

    /**
     * Normalizes this quaternion.
     *
     * @return a new Quaternion representing the normalized quaternion
     * @throws ArithmeticException if the quaternion is zero
     */
    public Quaternion normalize() {
        double norm = Math.sqrt(normSquared());
        if (norm == 0) {
            throw new ArithmeticException("Cannot normalize a zero quaternion.");
        }
        return new Quaternion(x / norm, y / norm, z / norm, w / norm);
    }

    /**
     * Converts this quaternion to an axis-angle representation.
     *
     * @return an AxisAngle object representing the axis-angle
     */
    public AxisAngle toAxisAngle() {
        Quaternion normalized = this.normalize();
        double angle = 2 * Math.acos(normalized.w);
        double s = Math.sqrt(1 - normalized.w * normalized.w);
        if (s < 0.0001) { // avoid division by zero
            return new AxisAngle(1, 0, 0, angle);
        }
        return new AxisAngle(normalized.x / s, normalized.y / s, normalized.z / s, angle);
    }

    /**
     * Converts this quaternion to Euler angles.
     *
     * @return an array of doubles representing the Euler angles [roll, pitch, yaw]
     */
    public double[] toEulerAngles() {
        Quaternion q = this.normalize();
        double ysqr = q.y * q.y;

        // roll (x-axis rotation)
        double t0 = +2.0 * (q.w * q.x + q.y * q.z);
        double t1 = +1.0 - 2.0 * (q.x * q.x + ysqr);
        double roll = Math.atan2(t0, t1);

        // pitch (y-axis rotation)
        double t2 = +2.0 * (q.w * q.y - q.z * q.x);
        t2 = Math.min(t2, 1.0);
        t2 = Math.max(t2, -1.0);
        double pitch = Math.asin(t2);

        // yaw (z-axis rotation)
        double t3 = +2.0 * (q.w * q.z + q.x * q.y);
        double t4 = +1.0 - 2.0 * (ysqr + q.z * q.z);
        double yaw = Math.atan2(t3, t4);

        return new double[]{roll, pitch, yaw};
    }

    /**
     * Rotates a vector by this quaternion.
     *
     * @param v the vector to rotate
     * @return a new Vec3 representing the rotated vector
     */
    public Vec3 rotateVector(Vec3 v) {
        Quaternion vecQuat = new Quaternion(v.getX(), v.getY(), v.getZ(), 0);
        Quaternion resQuat = this.multiply(vecQuat).multiply(this.inverse());
        return new Vec3(resQuat.x, resQuat.y, resQuat.z);
    }

    /**
     * Represents an axis-angle pair.
     */
    public record AxisAngle(double x, double y, double z, double angle) {
    }

}
