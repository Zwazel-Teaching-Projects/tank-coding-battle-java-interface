package dev.zwazel.internal.game.state.flag;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "state")
@JsonSubTypes({
        @JsonSubTypes.Type(value = InBase.class),
        @JsonSubTypes.Type(value = Carried.class),
        @JsonSubTypes.Type(value = Dropped.class),
})
public interface FlagState {
}
