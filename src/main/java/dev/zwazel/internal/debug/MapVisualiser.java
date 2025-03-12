package dev.zwazel.internal.debug;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.game.map.MapDefinition;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
public class MapVisualiser extends JPanel {
    private final PublicGameWorld world;

    private final int CELL_SIZE = 50;

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
                        String.format("%.2f", tileHeight),
                        x * CELL_SIZE + 5,
                        y * CELL_SIZE + 15
                );
            }
        }
    }
}
