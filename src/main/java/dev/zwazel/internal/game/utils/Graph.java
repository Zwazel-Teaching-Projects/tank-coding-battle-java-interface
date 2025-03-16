package dev.zwazel.internal.game.utils;

import dev.zwazel.internal.game.map.MapDefinition;
import lombok.Data;

@Data
public class Graph {
    private Node[][] nodes;

    public Graph(MapDefinition mapDefinition, boolean allowDiagonal) {
        nodes = new Node[(int) mapDefinition.depth()][(int) mapDefinition.width()];

        // Create nodes
        for (int y = 0; y < mapDefinition.depth(); y++) {
            for (int x = 0; x < mapDefinition.width(); x++) {
                Node node = new Node(mapDefinition.tiles()[y][x], x, y);
                nodes[y][x] = node;
            }
        }

        // Create edges
        for (Node[] row : nodes) {
            for (Node node : row) {
                int x = node.getX();
                int y = node.getY();

                // Add horizontal and vertical edges
                if (x > 0) {
                    node.getNeighbours().add(getNode(x - 1, y));
                }
                if (x < mapDefinition.width() - 1) {
                    node.getNeighbours().add(getNode(x + 1, y));
                }
                if (y > 0) {
                    node.getNeighbours().add(getNode(x, y - 1));
                }
                if (y < mapDefinition.depth() - 1) {
                    node.getNeighbours().add(getNode(x, y + 1));
                }

                // Add diagonal edges
                if (allowDiagonal) {
                    if (x > 0 && y > 0) {
                        node.getNeighbours().add(getNode(x - 1, y - 1));
                    }
                    if (x < mapDefinition.width() - 1 && y > 0) {
                        node.getNeighbours().add(getNode(x + 1, y - 1));
                    }
                    if (x > 0 && y < mapDefinition.depth() - 1) {
                        node.getNeighbours().add(getNode(x - 1, y + 1));
                    }
                    if (x < mapDefinition.width() - 1 && y < mapDefinition.depth() - 1) {
                        node.getNeighbours().add(getNode(x + 1, y + 1));
                    }
                }
            }
        }
    }

    @Deprecated(forRemoval = true)
    public Graph(float[][] heights, boolean allowDiagonal) {
        nodes = new Node[heights.length][heights[0].length];

        // Create nodes
        for (int y = 0; y < heights.length; y++) {
            for (int x = 0; x < heights[y].length; x++) {
                Node node = new Node(heights[y][x], x, y);
                nodes[y][x] = node;
            }
        }

        // Create edges
        for (Node[] row : nodes) {
            for (Node node : row) {
                int x = node.getX();
                int y = node.getY();

                // Add horizontal and vertical edges
                if (x > 0) {
                    node.getNeighbours().add(getNode(x - 1, y));
                }
                if (x < heights[y].length - 1) {
                    node.getNeighbours().add(getNode(x + 1, y));
                }
                if (y > 0) {
                    node.getNeighbours().add(getNode(x, y - 1));
                }
                if (y < heights.length - 1) {
                    node.getNeighbours().add(getNode(x, y + 1));
                }

                // Add diagonal edges
                if (allowDiagonal) {
                    if (x > 0 && y > 0) {
                        node.getNeighbours().add(getNode(x - 1, y - 1));
                    }
                    if (x < heights[y].length - 1 && y > 0) {
                        node.getNeighbours().add(getNode(x + 1, y - 1));
                    }
                    if (x > 0 && y < heights.length - 1) {
                        node.getNeighbours().add(getNode(x - 1, y + 1));
                    }
                    if (x < heights[y].length - 1 && y < heights.length - 1) {
                        node.getNeighbours().add(getNode(x + 1, y + 1));
                    }
                }
            }

        }
    }

    public Node getNode(int x, int y) {
        return nodes[y][x];
    }

    public Node getNode(long x, long y) {
        return nodes[(int) y][(int) x];
    }

    public Node getNode(double x, double y) {
        return nodes[(int) y][(int) x];
    }
}