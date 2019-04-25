package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
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

        int numOfRooms = r.nextInt(HEIGHT / 2) + 1;
        createRooms(numOfRooms, r);
        createConnectingHallways(numOfRooms);

        return world;
    }


    private void createRooms(int numRooms, Random rand) {
        for (int i = 0; i < numRooms; i++) {
            double height = rand.nextInt(HEIGHT / 4) + 1;
            double width = rand.nextInt(WIDTH / 4) + 1;
            double x = rand.nextInt(WIDTH - (int) width - 1);
            double y = rand.nextInt(HEIGHT - (int) height - 1);
            Point p = new Point((int) x, (int) y);
            Room room = new Room(p, (int) height, (int) width);
            listOfRooms.add(room);
            listOfLocs.roomPoints.add(p);
            for (int x1 = (int) x; x1 < x + width; x1++) {
                for (int y1 = (int) y; y1 < y + height; y1++) {
                    world[x1][y1] = Tileset.FLOOR;
                }
            }

        }
    }

    private void createConnectingHallways(int numRooms) {
        for (int i = 0; i < numRooms - 1; i++) {
            Room curr = listOfRooms.get(i);
            Room next = listOfRooms.get(i + 1);

            if ((curr.p.y + curr.height == next.p.y && curr.p.x == next.p.x)
                    ||
                    (next.p.y + next.height == curr.p.y && curr.p.x == next.p.x)) {
                break;
            }

            if ((curr.p.x + curr.width == next.p.x && curr.p.y == next.p.y)
                    ||
                    (next.p.x + next.width == curr.p.x && curr.p.y == next.p.y)) {
                break;
            }
            int count;

            Room minx;
            Room maxx;
            if (curr.p.x + curr.width < next.p.x) {
                minx = curr;
                maxx = next;
            } else {
                minx = next;
                maxx = curr;
            }
            count = 0;
            for (int x = minx.p.x + minx.width; x <= maxx.p.x; x++) {
                world[x][minx.p.y] = Tileset.FLOOR;
                count++;
            }
            Point nextP = new Point(minx.p.x + 1, minx.p.y);
            Hallway hall = new Hallway(nextP, count, true);
            listOfHallways.add(hall);
            listOfLocs.hallwayPoints.add(hall.p);
            Room miny;
            Room maxy;
            if (curr.p.y < next.p.y - next.height) {
                miny = curr;
                maxy = next;
            } else {
                miny = next;
                maxy = curr;
            }
            count = 0;
            for (int y = miny.p.y + 1; y <= maxy.p.y - maxy.height; y++) {
                world[miny.p.x][y] = Tileset.FLOOR;
                count++;
            }
            Point newP = new Point(maxy.p.y - 1, maxx.p.x - 1);
            Hallway hall2 = new Hallway(newP, count, false);
            listOfHallways.add(hall2);
            listOfLocs.hallwayPoints.add(hall2.p);

        }
    }


}
