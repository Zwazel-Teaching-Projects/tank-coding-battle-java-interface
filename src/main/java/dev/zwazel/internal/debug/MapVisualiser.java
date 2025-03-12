package dev.zwazel.internal.debug;

import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.game.map.MapDefinition;
import dev.zwazel.internal.game.transform.Vec3;
import dev.zwazel.internal.game.utils.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

@EqualsAndHashCode(callSuper = true)
@Data
public class MapVisualiser extends JPanel {
    private final PublicGameWorld world;
    private final int CELL_SIZE = 50;
    private DrawingMode drawingMode = DrawingMode.HEIGHT;
    private LinkedList<Node> path = new LinkedList<>();

    public void showMap() {
        int width = ((int) world.getGameConfig().mapDefinition().width() + 1) * CELL_SIZE;
        int height = ((int) world.getGameConfig().mapDefinition().depth() + 1) * CELL_SIZE;

        JFrame frame = new JFrame("Map Visualiser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.getContentPane().add(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add key listener to switch drawing modes
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    toggleDrawingMode();
                }
            }
        });
    }

    // Drawing the map
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        MapDefinition mapDefinition = world.getGameConfig().mapDefinition();

        switch (drawingMode) {
            case HEIGHT -> drawHeightMap(g2d, mapDefinition);
            case PATH -> drawPath(g2d, mapDefinition);
        }

        // Draw cell borders
        drawCellBorders(g2d, mapDefinition);
    }

    private void drawHeightMap(Graphics2D g2d, MapDefinition mapDefinition) {
        // Determine min and max heights to normalize cell colors
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        for (int x = 0; x < mapDefinition.width(); x++) {
            for (int y = 0; y < mapDefinition.depth(); y++) {
                float tileHeight = mapDefinition.tiles()[y][x];
                if (tileHeight < min) {
                    min = tileHeight;
                }
                if (tileHeight > max) {
                    max = tileHeight;
                }
            }
        }

        // First Pass: Draw each cell with a color gradient based on its height.
        for (int x = 0; x < mapDefinition.width(); x++) {
            for (int y = 0; y < mapDefinition.depth(); y++) {
                float tileHeight = mapDefinition.tiles()[y][x];
                float normalizedHeight = (tileHeight - min) / (max - min);
                // Draw cell
                Color cellColor = new Color(normalizedHeight, 1 - normalizedHeight, 0);
                g2d.setColor(cellColor);
                g2d.fillRect(
                        x * CELL_SIZE,
                        y * CELL_SIZE,
                        CELL_SIZE,
                        CELL_SIZE
                );
            }
        }

        // Second Pass: Draw tank
        drawTank(g2d, world);

        // Third Pass: Draw heights
        for (int x = 0; x < mapDefinition.width(); x++) {
            for (int y = 0; y < mapDefinition.depth(); y++) {
                float tileHeight = mapDefinition.tiles()[y][x];
                g2d.setColor(Color.BLACK);
                g2d.drawString(
                        String.format("%.2f", tileHeight),
                        x * CELL_SIZE + 5,
                        y * CELL_SIZE + 15
                );
            }
        }
    }

    private void drawPath(Graphics2D g2d, MapDefinition mapDefinition) {
        // First pass: Draw Tank
        drawTank(g2d, world);

        // Second pass: Draw start and end cells
        if (!path.isEmpty()) {
            Node startNode = path.getFirst();
            Node endNode = path.getLast();

            // Draw start cell in green
            g2d.setColor(Color.GREEN);
            g2d.fillRect(
                    startNode.getX() * CELL_SIZE,
                    startNode.getY() * CELL_SIZE,
                    CELL_SIZE,
                    CELL_SIZE
            );

            // Draw end cell in red
            g2d.setColor(Color.RED);
            g2d.fillRect(
                    endNode.getX() * CELL_SIZE,
                    endNode.getY() * CELL_SIZE,
                    CELL_SIZE,
                    CELL_SIZE
            );
        }

        // Third pass: Draw Path
        g2d.setColor(Color.RED);
        for (int i = 0; i < path.size() - 1; i++) {
            Node node1 = path.get(i);
            Node node2 = path.get(i + 1);
            g2d.drawLine(
                    node1.getX() * CELL_SIZE + CELL_SIZE / 2,
                    node1.getY() * CELL_SIZE + CELL_SIZE / 2,
                    node2.getX() * CELL_SIZE + CELL_SIZE / 2,
                    node2.getY() * CELL_SIZE + CELL_SIZE / 2
            );
        }

        // Fourth pass: Draw costs
        for (Node node : path) {
            g2d.setColor(Color.BLACK);
            g2d.drawString(
                    String.format("%.2f", node.getCost()),
                    node.getX() * CELL_SIZE + 5,
                    node.getY() * CELL_SIZE + 15
            );
        }
    }

    private void drawCellBorders(Graphics2D g2d, MapDefinition mapDefinition) {
        for (int x = 0; x < mapDefinition.width(); x++) {
            for (int y = 0; y < mapDefinition.depth(); y++) {
                // Draw cell border
                g2d.setColor(Color.BLACK);
                g2d.drawRect(
                        x * CELL_SIZE,
                        y * CELL_SIZE,
                        CELL_SIZE,
                        CELL_SIZE
                );
            }
        }
    }

    private void drawTank(Graphics2D g2d, PublicGameWorld world) {
        // Draw location of my tank (drawing the cell blue, and the actual position as a point)
        Vec3 myPosition = world.getMyState().transformBody().getTranslation();
        Vec3 closestTile = world.getGameConfig().mapDefinition().getClosestTileFromWorld(myPosition);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(
                (int) closestTile.getX() * CELL_SIZE,
                (int) closestTile.getZ() * CELL_SIZE,
                CELL_SIZE,
                CELL_SIZE
        );

        // Turn the position from float to int, so it can be drawn. from units to pixels
        int ovaLSize = 15;
        int x = (int) (myPosition.getX() * CELL_SIZE) - ovaLSize / 2;
        int y = (int) (myPosition.getZ() * CELL_SIZE) - ovaLSize / 2;

        g2d.setColor(Color.ORANGE);
        g2d.fillOval(
                x,
                y,
                ovaLSize,
                ovaLSize
        );
    }

    private void toggleDrawingMode() {
        DrawingMode[] modes = DrawingMode.values();
        int currentIndex = drawingMode.ordinal();
        int nextIndex = (currentIndex + 1) % modes.length;
        drawingMode = modes[nextIndex];
        repaint();
    }

    // Enum for switching drawing modes
    public enum DrawingMode {
        HEIGHT,
        PATH
    }
}
