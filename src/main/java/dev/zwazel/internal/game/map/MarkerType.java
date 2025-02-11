package dev.zwazel.internal.game.map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.zwazel.internal.game.map.marker.Flag;
import dev.zwazel.internal.game.map.marker.Spawn;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Spawn.class),
        @JsonSubTypes.Type(value = Flag.class),
})
public interface MarkerType {
}
