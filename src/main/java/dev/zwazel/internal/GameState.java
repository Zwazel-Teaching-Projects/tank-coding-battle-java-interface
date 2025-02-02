package dev.zwazel.internal;

import lombok.Data;

@Data
public class GameState {
    private static GameState instance;

    private Long tick = 0L;

    private GameState() {

    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }
}
