package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.awt.Point;
import java.util.ArrayList;


/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {


    //p has x and y, lower left of hexagon

    public void addHexagon(TETile[][] world, Point p, int s, TETile t) {
        for (int i = p.x; i > p.x - s; i--) {
            for (int j = p.y - (2* s) + 1; j <= p.y - s; j++){
                world[i][j] = t;
            }
        }

        for (int i = p.x - s + 1; i <= p.x; i++) {
            for (int j = p.y - s + 1; j <= p.y; j++) {
                world[i][j] = t;
            }

        }

    }

    private ArrayList<Point> findPoints() {
        return null;
    }

}
