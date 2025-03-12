package dev.zwazel.internal.debug;

import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.game.map.MapDefinition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
public class MapVisualiser extends JPanel {
    private final PublicGameWorld world;
    private final int CELL_SIZE = 50;
    private DrawingMode drawingMode = DrawingMode.HEIGHT;
    /**
     * A list of points to draw on the map. Used for drawing paths.
     */
    private ArrayList<Point> path = new ArrayList<>();

    public void showMap() {
        int width = ((int) world.getGameConfig().mapDefinition().width() + 1) * CELL_SIZE;
        int height = ((int) world.getGameConfig().mapDefinition().depth() + 1) * CELL_SIZE;

        JFrame frame = new JFrame("Map Visualiser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.getContentPane().add(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Drawing the map
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        MapDefinition mapDefinition = world.getGameConfig().mapDefinition();

        // Switch drawing mode
        switch (drawingMode) {
            case HEIGHT -> drawHeightMap(g2d, mapDefinition);
            case PATH -> drawPath(g2d, mapDefinition);
        }
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

        // Draw each cell with a color gradient based on its height.
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

        drawCellBorders(g2d, mapDefinition);
    }

    private void drawPath(Graphics2D g2d, MapDefinition mapDefinition) {
        // draw path, path is a list of points representing cells in the grid. Each point is a cell in the grid.
        // Draw line between each point in the path
        g2d.setColor(Color.RED);
        for (int i = 0; i < path.size() - 1; i++) {
            Point p1 = path.get(i);
            Point p2 = path.get(i + 1);
            g2d.drawLine(
                    p1.x * CELL_SIZE + CELL_SIZE / 2,
                    p1.y * CELL_SIZE + CELL_SIZE / 2,
                    p2.x * CELL_SIZE + CELL_SIZE / 2,
                    p2.y * CELL_SIZE + CELL_SIZE / 2
            );
        }

        drawCellBorders(g2d, mapDefinition);
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
                // Draw the height value in the cell
                g2d.setColor(Color.BLACK);
                g2d.drawString(
                        String.format("%.2f", mapDefinition.tiles()[y][x]),
                        x * CELL_SIZE + 5,
                        y * CELL_SIZE + 15
                );
            }
        }
    }

    // Enum for switching drawing modes
    public enum DrawingMode {
        HEIGHT,
        PATH
    }
}
