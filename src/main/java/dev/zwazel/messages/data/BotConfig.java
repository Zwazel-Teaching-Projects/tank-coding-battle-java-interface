package dev.zwazel.messages.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.zwazel.messages.MessageData;
import lombok.Data;

@JsonTypeName("BOT_CONFIG")
@Data
public class BotConfig implements MessageData {

}
