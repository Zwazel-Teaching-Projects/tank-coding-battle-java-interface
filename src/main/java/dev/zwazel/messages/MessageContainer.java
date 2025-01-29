package dev.zwazel.messages;

import lombok.Data;

@Data
public class MessageContainer {
    private MessageTarget target;

    private MessageData message;
}
