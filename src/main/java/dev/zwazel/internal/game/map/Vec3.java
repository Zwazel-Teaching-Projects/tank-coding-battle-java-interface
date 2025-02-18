package dev.zwazel.internal.game.map;

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
}
