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

    // Actual cost from the start node to this node.
    private double gCost = Double.MAX_VALUE;

    // Heuristic estimated cost from this node to the target.
    private double hCost = 0;

    // fCost is the sum of gCost and hCost.
    public double getFCost() {
        return gCost + hCost;
    }

    @Override
    public String toString() {
        return "Node{" +
                "height=" + height +
                ", x=" + x +
                ", y=" + y +
                ", gCost=" + gCost +
                ", hCost=" + hCost +
                ", fCost=" + getFCost() +
                '}';
    }
}