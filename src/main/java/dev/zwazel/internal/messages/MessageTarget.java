package dev.zwazel.internal.messages;

public enum MessageTarget {
    SERVER_ONLY,
    /// If I get a message from the server, it will have a target of CLIENT (me)
    CLIENT,
    TEAM,
    ALL
}
