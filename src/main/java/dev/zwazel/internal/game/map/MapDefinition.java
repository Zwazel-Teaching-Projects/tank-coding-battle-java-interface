package dev.zwazel.internal.game.map;

import dev.zwazel.internal.game.misc.SimplifiedRGB;
import dev.zwazel.internal.game.transform.Vec3;

import java.util.ArrayList;
import java.util.Arrays;

public record MapDefinition(long width, long depth, SimplifiedRGB floorColor,
                            float[][] tiles,
                            LayerDefinition[] layers,
                            MarkerDefinition[] markers) {
    public final static float TILE_SIZE = 1f;

    /**
     * Returns the Coordinates of the closest tile to the given world position in the map grid.
     * The closest tile is the tile whose center is closest to the given world position.
     * The y coordinate of the world position must be positive.
     * If the y coordinate of the world position is negative, an IllegalArgumentException is thrown.
     * The y coordinate of the returned Vec3 is the height of the tile.
     * The returned Vec3 is in grid coordinates, not world coordinates.
     * The given Vec3 must be in world coordinates, not grid coordinates.
     *
     * @param worldPos the world position
     * @return the closest tile to the world position, as a Vec3.
     */
    public Vec3 getClosestTileFromWorld(Vec3 worldPos) {
        if (worldPos.getY() < 0) {
            throw new IllegalArgumentException("Y coordinate of the world position must be positive");
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Vec3 closest = null;

        for (Vec3 tile : gridToWorld()) {
            double distance = tile.distance(worldPos);

            if (distance < TILE_SIZE / 2) {
                return getTile((int) tile.getX(), (int) tile.getZ());
            }

            if (distance < minDistance) {
                minDistance = distance;
                closest = tile;
            }

            double closestDistance = Math.abs(closest.getX() - worldPos.getX()) + Math.abs(closest.getZ() - worldPos.getZ());
            if (distance < closestDistance) {
                closest = tile;
                closestX = (int) tile.getX();
                closestY = (int) tile.getZ();
            }
        }

        assert closest != null;
        return getTile((int) closest.getX(), (int) closest.getZ());
    }

    /**
     * Returns the world coordinates of the tile centers in the map grid as a list.
     *
     * @return the world coordinates of the tile centers in the map grid
     */
    public ArrayList<Vec3> gridToWorld() {
        ArrayList<Vec3> grid = new ArrayList<>();
        for (int y = 0; y < depth; y++) {
            for (int x = 0; x < width; x++) {
                grid.add(getWorldTileCenter(x, y));
            }
        }
        return grid;
    }

    /**
     * Returns the center of the tile in world coordinates.
     *
     * @param x the x coordinate of the tile in the map grid (0-indexed)
     * @param y the y coordinate of the tile in the map grid (0-indexed)
     * @return the world coordinate of the center of the tile
     */
    public Vec3 getWorldTileCenter(int x, int y) {
        return new Vec3(x + 0.5f, getTileHeight(x, y), y + 0.5f);
    }

    /**
     * Returns the height of the tile at the given grid coordinates.
     *
     * @param x the x coordinate of the tile in the map grid (0-indexed)
     * @param y the y coordinate of the tile in the map grid (0-indexed)
     * @return the height of the tile
     */
    public float getTileHeight(int x, int y) {
        return tiles[y][x];
    }

    /**
     * Returns the height of the tile at the given grid coordinates.
     *
     * @param pos the grid coordinates of the tile, not world coordinates!
     *            The y coordinate is the height of the tile.
     *            The y coordinate is ignored in this method.
     *            The z coordinate of the given Vec3 is the y coordinate of the tile in the 2D map grid.
     *            The x coordinate of the given Vec3 is the x coordinate of the tile in the 2D map grid.
     * @return the height of the tile
     */
    public float getTileHeight(Vec3 pos) {
        return getTileHeight((int) pos.getX(), (int) pos.getZ());
    }

    /**
     * Returns the tile at the given grid coordinates as a Vec3.
     *
     * @param x the x coordinate of the tile in the map grid (0-indexed)
     * @param y the y coordinate of the tile in the map grid (0-indexed)
     * @return the tile as a Vec3 in grid coordinates. the y is the height of the tile.
     */
    public Vec3 getTile(int x, int y) {
        // Check if the coordinates are valid
        if (x < 0 || x >= width || y < 0 || y >= depth) {
            throw new IllegalArgumentException("Invalid grid coordinates: (" + x + ", " + y + ")");
        }

        return new Vec3(x, getTileHeight(x, y), y);
    }

    /**
     * Returns the tile at the given grid coordinates as a Vec3.
     *
     * @param pos the grid coordinates of the tile, not world coordinates!
     *            The y coordinate is the height of the tile.
     *            The y coordinate is ignored in this method.
     *            The z coordinate of the given Vec3 is the y coordinate of the tile in the 2D map grid.
     *            The x coordinate of the given Vec3 is the x coordinate of the tile in the 2D map grid.
     * @return the tile as a Vec3 in grid coordinates. the y is always 0, as this is a 2D map.
     */
    public Vec3 getTile(Vec3 pos) {
        return getTile((int) pos.getX(), (int) pos.getZ());
    }

    @Override
    public String toString() {
        return "MapDefinition{" +
                "width=" + width +
                ", depth=" + depth +
                ", tiles=" + Arrays.deepToString(tiles) +
                ", layers=" + Arrays.toString(layers) +
                ", markers=" + Arrays.toString(markers) +
                '}';
    }
}
