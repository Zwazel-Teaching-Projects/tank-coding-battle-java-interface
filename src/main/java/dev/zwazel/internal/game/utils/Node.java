package dev.zwazel.internal.game.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;

@Getter
@Setter
@RequiredArgsConstructor
public class Node {
    private final float height;
    private final int x;
    private final int y;
    private final LinkedList<Node> neighbours = new LinkedList<>();
    private Node parent;
    private double cost = Double.MAX_VALUE;

    @Override
    public String toString() {
        return "Node{" +
                "height=" + height +
                ", x=" + x +
                ", y=" + y +
                ", cost=" + cost +
                '}';
    }
}