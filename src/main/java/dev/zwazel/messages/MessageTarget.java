package dev.zwazel.messages;

public enum MessageTarget {
    TEAM,
    ALL;

    public static MessageTarget fromOrdinal(int ordinal) {
        return values()[ordinal];
    }
}
