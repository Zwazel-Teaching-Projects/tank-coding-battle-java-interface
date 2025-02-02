package dev.zwazel.internal.messages.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.zwazel.GameWorld;
import dev.zwazel.internal.messages.MessageData;
import lombok.Builder;
import lombok.Data;

@JsonTypeName("FIRST_CONTACT")
@Data
@Builder
public class FirstContact implements MessageData {
    private final String name;
    private final String lobby_id;

    @Override
    public void applyOnReceive(GameWorld world) {

    }
}
