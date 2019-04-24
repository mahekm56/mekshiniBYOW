package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

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
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    // @Source - lab12
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        String seed = input.substring(1, input.length()-2);
        Long seedVal = Long.parseLong(seed);
        Random r = new Random(seedVal);
        Locations listOfLocs = new Locations();
        ArrayList<Room> listOfRooms = new ArrayList<>();
        ArrayList<Hallway> listOfHallways = new ArrayList<>();


        double numOfRooms = RandomUtils.uniform(r);
                //r.nextInt();
        for (int i = 0; i < numOfRooms; i++) {

            double x = RandomUtils.uniform(r);
            double y = RandomUtils.uniform(r);
            double height = RandomUtils.uniform(r);
            double width = RandomUtils.uniform(r);
            Point p = new Point( (int) x, (int) y);
            Room room = new Room(p, (int)height, (int)width);
            listOfRooms.add(room);
            listOfLocs.roomPoints.add(p);
            for (int x1 = (int)x; x1< x1+width -1; x1++) {
                for (int y1 = (int)y; y1< y1+height; y1++) {
                    world[x1][y1] = Tileset.FLOOR;
                }
            }

        }

        for (int i = 0; i < numOfRooms - 1; i++) {
            //creating hallways between rooms
            Room curr = listOfRooms.get(i);
            Room next = listOfRooms.get(i+1);
            Room minx; Room maxx;
            if (Math.max(curr.p.x, next.p.x) == curr.p.x) {
                maxx= curr;
                minx = next;
            }
            else {
                maxx = next;
                minx = curr;
            }
            int count = 0;
            for (int x = minx.p.x; x < maxx.p.x; x++) {
                world[x][curr.p.y] = Tileset.FLOOR;
                count++;
            }
            Point nextP = new Point(minx.p.x+1, minx.p.y);

            Hallway hall = new Hallway(nextP , count);
            listOfHallways.add(hall);
            listOfLocs.hallwayPoints.add(hall.p);

            // first x hallway
            Room miny; Room maxy;
            if (Math.max(curr.p.y, next.p.y) == curr.p.y) {
                maxy = curr;
                miny = next;
            }
            else {
                maxy = next;
                miny = curr;
            }
            count = 0;
            for (int y = miny.p.y; y< maxy.p.y; y++) {
                world[maxx.p.x - 1][y] = Tileset.FLOOR;
                count++;
            }
            Point newP = new Point(maxy.p.y - 1, maxx.p.x - 1);
            Hallway hall2 = new Hallway(newP, count);
            listOfHallways.add(hall2);
            listOfLocs.hallwayPoints.add(hall2.p);
        }





        /**
        ArrayList<Integer> sizeOfRooms = new ArrayList<>();
        for (int i = 0; i < numOfRooms * 2; i++) {
            int x = r.nextInt();
            if (x<15) {
                sizeOfRooms.add(x);
            }
        }
        numOfRooms = sizeOfRooms.size();
        WeightedQuickUnionUF areRoomsConnected = new WeightedQuickUnionUF(numOfRooms);*/


        // now we have an arbitrary number of rooms and size of each room
        // stays constant when seed is the same
        //room class - size, position
        //hallways- position
        //position class- to hold all points  - arrays to hold all the room points and the hallway points
        //for loops to connect points of rooms



       // fill random rooms in random spaces based on seed, connect with hallways , rooms without walls, then connect them with hallways,
        //arraylist stores edges of rooms, any square that needs a wall


        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        //TETile[][] finalWorldFrame = null;
        return world;
    }


}
