package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    TERenderer ter = new TERenderer();
    Locations listOfLocs = new Locations();
    ArrayList<Room> listOfRooms = new ArrayList<>();
    ArrayList<Hallway> listOfHallways = new ArrayList<>();

    TETile[][] world = new TETile[WIDTH][HEIGHT];


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    // @Source - lab12
    public TETile[][] interactWithInputString(String input) {

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        String seed = input.substring(1, input.length() - 2);
        Long seedVal = Long.parseLong(seed);
        Random r = new Random(seedVal);

        int numOfRooms = r.nextInt(15 * HEIGHT / 16) + 1;
        makeRooms(numOfRooms, r);
        makeHallways(numOfRooms);
        addHallwayWalls();

        return world;
    }

    private void makeRooms(int numRooms, Random rand) {
        for (int i = 0; i < numRooms; i++) {
            int height = rand.nextInt(HEIGHT / 8) + 1;
            int width = rand.nextInt(WIDTH / 8) + 1;
            int x = rand.nextInt(WIDTH - (int) width - 2) + 1;
            int y = rand.nextInt(HEIGHT - (int) height - 2) + 1;
            Point p = new Point(x, y);
            Room room = new Room(p, height, width);
            listOfRooms.add(room);
            listOfLocs.roomPoints.add(p);
            for (int x1 = x; x1 < x + width; x1++) {
                for (int y1 = y; y1 < y + height; y1++) {
                    world[x1][y1] = Tileset.FLOOR;
                }
            }

            for (int xval = Math.max(0, x - 1); xval <= x + width; xval++) {
                world[xval][y + height] = Tileset.WALL;
                if (y == 0) {
                    break;
                }
                world[xval][y - 1] = Tileset.WALL;

            }
            for (int yval = Math.max(0, y - 1); yval <= y + height; yval++) {
                world[x + width][yval] = Tileset.WALL;
                if (x == 0) {
                    break;
                }
                world[x - 1][yval] = Tileset.WALL;
            }
        }
    }

    private void makeHallways(int numRooms) {
        for (int i = 0; i < numRooms - 1; i++) {
            Room curr = listOfRooms.get(i);
            Room next = listOfRooms.get(i + 1);
            Room minx;
            Room maxx;
            if (curr.p.x + curr.width < next.p.x) {
                minx = curr;
                maxx = next;
            } else {
                minx = next;
                maxx = curr;
            }
            int count = 0;
            for (int x = minx.p.x + minx.width - 1; x <= maxx.p.x + 1; x++) {
                world[x][minx.p.y] = Tileset.FLOOR;
                count++;
            }
            Point p = new Point(minx.p.x + minx.width, minx.p.y);
            Hallway h = new Hallway(p, count, true);
            listOfLocs.hallwayPoints.add(p);
            listOfHallways.add(h);

            Room miny;
            Room maxy;
            if (curr.p.y + curr.height < next.p.y) {
                miny = curr;
                maxy = next;
            } else {
                miny = next;
                maxy = curr;
            }
            count = 0;
            for (int y = miny.p.y + 1; y < maxy.p.y; y++) {
                if (maxy.p.x != 0) {
                    world[maxy.p.x][y] = Tileset.FLOOR;
                    count++;
                }

            }
            Point po = new Point(maxy.p.x, miny.p.y + 1);
            Hallway ha = new Hallway(po, count, false);
            listOfLocs.hallwayPoints.add(po);
            listOfHallways.add(ha);
        }
    }

    private void addHallwayWalls() {
        for (Hallway h : listOfHallways) {
            if (h.horizontal) {
                for (int x = h.p.x; x <= h.p.x + h.length - 1; x++) {
                    if (world[x][h.p.y + 1] == Tileset.NOTHING) {
                        world[x][h.p.y + 1] = Tileset.WALL;
                    }
                    if (h.p.y != 0) {
                        if (world[x][h.p.y - 1] == Tileset.NOTHING) {
                            world[x][h.p.y - 1] = Tileset.WALL;
                        }
                    }

                }
                if (world[h.p.x + h.length - 1][h.p.y] == Tileset.NOTHING) {
                    world[h.p.x + h.length - 1][h.p.y] = Tileset.WALL;
                }
            } else {
                //System.out.println(h.length);
                for (int y = h.p.y - 2; y < h.p.y + h.length; y++) {

                    if (world[h.p.x + 1][y].equals(Tileset.NOTHING)) {
                        world[h.p.x + 1][y] = Tileset.WALL;

                    }
                    if (h.p.x != 0) {
                        if (world[h.p.x - 1][y].equals(Tileset.NOTHING)) {
                            world[h.p.x - 1][y] = Tileset.WALL;

                        }

                    }

                }
                if (world[h.p.x][h.p.y - 1] == Tileset.NOTHING) {
                    world[h.p.x][h.p.y - 1] = Tileset.WALL;

                }
            }
        }
    }


}
