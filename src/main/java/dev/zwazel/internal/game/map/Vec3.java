package dev.zwazel.internal.game.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public Vec3(double[] position) {
        this.x = position[0];
        this.y = position[1];
        this.z = position[2];
    }
}