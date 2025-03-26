package dev.zwazel.internal.game.utils;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node n1, Node n2) {
        return Double.compare(n1.getFCost(), n2.getFCost());
    }
}
