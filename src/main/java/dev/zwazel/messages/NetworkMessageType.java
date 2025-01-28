package dev.zwazel.messages;

public enum NetworkMessageType {
    GAME_UPDATE;

    public static NetworkMessageType fromOrdinal(int ordinal) {
        return values()[ordinal];
    }
}
