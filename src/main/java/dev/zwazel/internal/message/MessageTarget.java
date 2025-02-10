package dev.zwazel.internal.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.zwazel.internal.message.target.ClientMessageTarget;
import dev.zwazel.internal.message.target.LobbyMessageTarget;
import dev.zwazel.internal.message.target.ServerOnlyMessageTarget;
import dev.zwazel.internal.message.target.TeamMessageTarget;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientMessageTarget.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = LobbyMessageTarget.class, name = "ALL_IN_LOBBY"),
        @JsonSubTypes.Type(value = ServerOnlyMessageTarget.class, name = "SERVER_ONLY"),
        @JsonSubTypes.Type(value = TeamMessageTarget.class, name = "TEAM"),
})
public interface MessageTarget {
    enum Type {
        CLIENT(id -> {
            if (id == null) {
                throw new IllegalArgumentException("Client target requires a client ID");
            }
            return new ClientMessageTarget(id);
        }),
        ALL_IN_LOBBY(_ -> new LobbyMessageTarget()),
        SERVER_ONLY(_ -> new ServerOnlyMessageTarget()),
        TEAM(_ -> new TeamMessageTarget());

        private final MessageTargetSupplier supplier;

        Type(MessageTargetSupplier supplier) {
            this.supplier = supplier;
        }

        public MessageTarget get() {
            return this.supplier.get(null);
        }

        public MessageTarget get(Long clientId) {
            return this.supplier.get(clientId);
        }
    }

    @FunctionalInterface
    interface MessageTargetSupplier {
        MessageTarget get(Long clientId);
    }
}
