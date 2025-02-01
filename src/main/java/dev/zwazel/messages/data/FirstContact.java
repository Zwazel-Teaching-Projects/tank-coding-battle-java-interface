package dev.zwazel.messages.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.zwazel.messages.MessageData;
import lombok.Builder;
import lombok.Data;

@JsonTypeName("FIRST_CONTACT")
@Data
@Builder
public class FirstContact implements MessageData {
    private final String name;
    private final String lobby_id;
}
