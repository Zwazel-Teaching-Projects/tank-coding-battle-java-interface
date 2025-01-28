package dev.zwazel.messages.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.zwazel.messages.MessageData;
import lombok.Data;

@JsonTypeName("GAME_STATE_UPDATE")
@Data
public class GameStateUpdateData implements MessageData {
    private int tick;
}
