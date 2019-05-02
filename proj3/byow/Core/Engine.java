package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Point;
import java.awt.Font;
import java.awt.Color;

import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    TERenderer ter = new TERenderer();
    ArrayList<Room> listOfRooms = new ArrayList<>();
    ArrayList<Hallway> listOfHallways = new ArrayList<>();
    TETile[][] world = new TETile[WIDTH][HEIGHT];
    Room minx;
    Room maxx;
    Room miny;
    Room maxy;
    Point avatarLocation;
    Boolean win = true;
    //Point flowerLocation;
    String toInsert = "";
    String inputSeed;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        createMenu();
        StdDraw.enableDoubleBuffering();

        while (true) {

            if (StdDraw.hasNextKeyTyped()) {

                char input = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (input == 'l' || input == 'L') {
                    toInsert = loadWorld();
                    interactWithInputString(toInsert);
                    ter.renderFrame(world);
                    startCommands();
                }


                if (input == 'n' || input == 'N') {
                    toInsert += 'n';


                    StdDraw.enableDoubleBuffering();
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT * 2 / 3, "Enter a seed: " + toInsert);
                    StdDraw.show();
                    while (true) {
                        if (!StdDraw.hasNextKeyTyped()) {
                            continue;
                        }
                        char c = StdDraw.nextKeyTyped();
                        if (c == 's' || c == 'S') {
                            toInsert += 's';
                            StdDraw.enableDoubleBuffering();
                            StdDraw.clear(Color.BLACK);
                            StdDraw.text(WIDTH / 2, HEIGHT * 2 / 3, "Enter a seed: " + toInsert);
                            StdDraw.show();
                            interactWithInputString(toInsert);
                            ter.renderFrame(world);
                            startCommands();
                            break;

                        }
                        if (c != 'n' || c != 'N') {

                            toInsert += c;
                            StdDraw.enableDoubleBuffering();
                            StdDraw.clear(Color.BLACK);
                            StdDraw.text(WIDTH / 2, HEIGHT * 2 / 3, "Enter a seed: " + toInsert);
                            StdDraw.show();
                        }
                    }
                }
                if (input == 'l' || input == 'L') {
                    toInsert = loadWorld();
                    interactWithInputString(toInsert);
                    ter.renderFrame(world);
                    startCommands();
                }
                /**if (input == 'q' || input == 'Q') {

                 saveWorld(toInsert);
                 //System.out.println("hi");
                 makeWorld();
                 ter.renderFrame(world);
                 // System.out.println("hi1");
                 System.exit(0);
                 //System.out.println("hi2");
                 break;
                 }*/

            }
        }


    }

    private void createMenu() {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.disableDoubleBuffering();
        StdDraw.show();
        Font start = new Font("Monaco", Font.BOLD, 25);
        Font mainM = new Font("Monaco", Font.PLAIN, 16);
        StdDraw.setFont(start);
        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.text(WIDTH / 2, HEIGHT * 2 / 3, "Welcome to MEKSHINI");
        StdDraw.setFont(mainM);
        StdDraw.text(WIDTH / 2, HEIGHT * 5.5 / 10, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT * 4.5 / 10, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT * 3.5 / 10, "Quit (Q)");
    }

    private void startCommands() {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }

            char i = StdDraw.nextKeyTyped();
            Point x;
            if (i == 'W' || i == 'w') {
                toInsert += 'w';
                x = moveUp(avatarLocation, Tileset.AVATAR);
                if (x.x != -1) {
                    avatarLocation = x;
                    ter.renderFrame(world);
                }

            } else if (i == 'a' || i == 'A') {
                toInsert += 'a';
                x = moveLeft(avatarLocation, Tileset.AVATAR);
                if (x.x != -1) {
                    avatarLocation = x;
                    ter.renderFrame(world);
                }

            } else if (i == 's' || i == 'S') {
                toInsert += 's';
                x = moveDown(avatarLocation, Tileset.AVATAR);
                if (x.x != -1) {
                    avatarLocation = x;
                    ter.renderFrame(world);
                }

            } else if (i == 'd' || i == 'D') {
                toInsert += 'd';
                x = moveRight(avatarLocation, Tileset.AVATAR);
                if (x.x != -1) {
                    avatarLocation = x;
                    ter.renderFrame(world);
                }
            } else if (i == 'q') {
                saveWorld(toInsert);
                //System.out.println(loadWorld());
                //System.out.println("hi");
                makeWorld();
                ter.renderFrame(world);
                // System.out.println("hi1");
                System.exit(0);
                //System.out.println("hi2");
                break;
            }
        }
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
        makeWorld();
        if (checkForLoad(input)) {
            interactWithInputString(loadWorld() + input.substring(1));
        } else {
            //inputSeed = input;

            String seed = input.substring(1, input.indexOf('s'));
            Long seedVal = Long.parseLong(seed);
            String commands = input.substring(input.indexOf('s') + 1);
            Random r = new Random(seedVal);
            int numOfRooms = r.nextInt(7 * HEIGHT / 8) + 1;
            create(numOfRooms, r);
            createWalls();
            world[listOfRooms.get(0).p.x + ((listOfRooms.get(0).width) / 2)][listOfRooms.get(0).p.y
                    + ((listOfRooms.get(0).height) / 2)] = Tileset.AVATAR;
            //world[listOfRooms.get(listOfRooms.size() - 1).p.x]
            //      [listOfRooms.get(listOfRooms.size() - 1).p.y] = Tileset.FLOWER;
            avatarLocation = new Point((listOfRooms.get(0).p.x + ((listOfRooms.get(0).width) / 2)),
                    listOfRooms.get(0).p.y + ((listOfRooms.get(0).height) / 2));
            //flowerLocation = new Point(listOfRooms.get(listOfRooms.size() - 1).p.x,
            //      listOfRooms.get(listOfRooms.size() - 1).p.y);
            moveCharacters(commands);
            saveWorld(input);
        }
        return world;
    }

    private boolean checkForLoad(String i) {
        String i2 = i.substring(0, 1);
        if (i2.equals("l") || i2.equals("L")) {
            return true;
        }
        return false;
    }


    private void makeWorld() {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void create(int n, Random rand) {
        for (int i = 0; i < n; i++) {
            makeOneRoom(rand);
            if (i != 0) {
                connectingHallway(i);
            }
        }
    }

    private void makeOneRoom(Random rand) {
        int height = rand.nextInt(HEIGHT / 8) + 2;
        int width = rand.nextInt(WIDTH / 8) + 2;
        int x = rand.nextInt(WIDTH - width - 2) + 1;
        int y = rand.nextInt(HEIGHT - height - 2) + 1;
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
        Point p = new Point(x, y);
        Room r = new Room(p, height, width);
        listOfRooms.add(r);
    }

    private void connectingHallway(int val) {
        Room r1 = listOfRooms.get(val);
        Room r2 = listOfRooms.get(val - 1);
        hallwayX(r1, r2);
        hallwayY(r1, r2);
    }

    private void hallwayX(Room r1, Room r2) {
        if (r2.p.x + r2.width < r1.p.x) {
            minx = r2;
            maxx = r1;
        } else {
            minx = r1;
            maxx = r2;
        }
        Point p = new Point(minx.p.x + minx.width, minx.p.y);
        int count = 0;
        for (int i = minx.p.x + minx.width; i <= maxx.p.x; i++) {
            world[i][minx.p.y] = Tileset.FLOOR;
            count++;
        }
        Hallway h = new Hallway(p, count, true);
        listOfHallways.add(h);
    }

    private void hallwayY(Room r1, Room r2) {
        if (r2.p.y < r1.p.y) {
            miny = r2;
            maxy = r1;
        } else {
            miny = r1;
            maxy = r2;
        }
        Point p = new Point(maxx.p.x, miny.p.y);
        int count = 0;
        for (int i = miny.p.y; i < maxy.p.y; i++) {
            world[maxx.p.x][i] = Tileset.FLOOR;
            count++;
        }
        Hallway h = new Hallway(p, count, false);
        listOfHallways.add(h);
    }

    private void createWalls() {
        for (int i = 0; i < listOfRooms.size(); i++) {
            wallOneRoom(listOfRooms.get(i));
        }
        for (int j = 0; j < listOfHallways.size(); j++) {
            wallOneHallway(listOfHallways.get(j));
        }
    }

    private void wallOneRoom(Room r) {
        for (int i = r.p.x - 1; i < r.p.x + r.width + 1; i++) {
            if ((world[i][r.p.y - 1]).equals(Tileset.NOTHING)) {
                world[i][r.p.y - 1] = Tileset.WALL;
            }
            if ((world[i][r.p.y + r.height]).equals(Tileset.NOTHING)) {
                world[i][r.p.y + r.height] = Tileset.WALL;
            }
        }
        for (int j = r.p.y - 1; j < r.p.y + r.height + 1; j++) {
            if ((world[r.p.x - 1][j]).equals(Tileset.NOTHING)) {
                world[r.p.x - 1][j] = Tileset.WALL;
            }
            if ((world[r.p.x + r.width][j]).equals(Tileset.NOTHING)) {
                world[r.p.x + r.width][j] = Tileset.WALL;
            }
        }
    }

    private void wallOneHallway(Hallway h) {
        if (h.horizontal) {
            for (int x = h.p.x - 1; x <= h.p.x + h.length; x++) {
                if ((world[x][h.p.y - 1]).equals(Tileset.NOTHING)) {
                    world[x][h.p.y - 1] = Tileset.WALL;
                }
                if ((world[x][h.p.y + 1]).equals(Tileset.NOTHING)) {
                    world[x][h.p.y + 1] = Tileset.WALL;
                }
            }
            if ((world[h.p.x - 1][h.p.y]).equals(Tileset.NOTHING)) {
                world[h.p.x - 1][h.p.y] = Tileset.WALL;
            }
            if ((world[h.p.x + h.length][h.p.y]).equals(Tileset.NOTHING)) {
                world[h.p.x + h.length][h.p.y] = Tileset.WALL;
            }
        } else {
            for (int y = h.p.y - 1; y <= h.p.y + h.length; y++) {
                if ((world[h.p.x - 1][y]).equals(Tileset.NOTHING)) {
                    world[h.p.x - 1][y] = Tileset.WALL;
                }
                if ((world[h.p.x + 1][y]).equals(Tileset.NOTHING)) {
                    world[h.p.x + 1][y] = Tileset.WALL;
                }
            }
            if ((world[h.p.x][h.p.y - 1]).equals(Tileset.NOTHING)) {
                world[h.p.x][h.p.y - 1] = Tileset.WALL;
            }
            if ((world[h.p.x][h.p.y + h.length]).equals(Tileset.NOTHING)) {
                world[h.p.x][h.p.y + h.length] = Tileset.WALL;
            }
        }
    }


    private Point moveCharacters(String c) {

        Point x;
        for (int i = 0; i < c.length(); i++) {
            if (win) {
                if (c.charAt(i) == 'w') {
                    x = moveUp(avatarLocation, Tileset.AVATAR);
                    if (x.x != -1) {
                        avatarLocation = x;
                    }
                    //chaseDirection();
                }
                if (c.charAt(i) == 'a') {
                    x = moveLeft(avatarLocation, Tileset.AVATAR);
                    if (x.x != -1) {
                        avatarLocation = x;
                    }
                    //chaseDirection();
                }
                if (c.charAt(i) == 's') {
                    x = moveDown(avatarLocation, Tileset.AVATAR);
                    if (x.x != -1) {
                        avatarLocation = x;
                    }
                    //chaseDirection();
                }
                if (c.charAt(i) == 'd') {
                    x = moveRight(avatarLocation, Tileset.AVATAR);
                    if (x.x != -1) {
                        avatarLocation = x;
                    }
                    //chaseDirection();
                }
            }

        }
        return avatarLocation;

    }

    private Point moveUp(Point loc, TETile t) {
        Point p = new Point(-1, 0);
        if ((world[loc.x][loc.y + 1]).equals(Tileset.FLOOR)
                || (world[loc.x][loc.y + 1]).equals(Tileset.AVATAR)) {
            world[loc.x][loc.y] = Tileset.FLOOR;
            world[loc.x][loc.y + 1] = t;
            p = new Point(loc.x, loc.y + 1);
        }
        //ter.renderFrame(world);
        return p;

    }

    private Point moveLeft(Point loc, TETile t) {
        Point p = new Point(-1, 0);
        if ((world[loc.x - 1][loc.y]).equals(Tileset.FLOOR)
                || (world[loc.x - 1][loc.y]).equals(Tileset.AVATAR)) {
            world[loc.x][loc.y] = Tileset.FLOOR;
            world[loc.x - 1][loc.y] = t;
            p = new Point(loc.x - 1, loc.y);
        }
        //ter.renderFrame(world);
        return p;

    }

    private Point moveDown(Point loc, TETile t) {
        Point p = new Point(-1, 0);
        if ((world[loc.x][loc.y - 1]).equals(Tileset.FLOOR)
                || (world[loc.x][loc.y - 1]).equals(Tileset.AVATAR)) {
            world[loc.x][loc.y] = Tileset.FLOOR;
            world[loc.x][loc.y - 1] = t;
            p = new Point(loc.x, loc.y - 1);
        }
        //ter.renderFrame(world);
        return p;
    }

    private Point moveRight(Point loc, TETile t) {
        Point p = new Point(-1, 0);
        if ((world[loc.x + 1][loc.y]).equals(Tileset.FLOOR)
                || (world[loc.x + 1][loc.y]).equals(Tileset.AVATAR)) {
            world[loc.x][loc.y] = Tileset.FLOOR;
            world[loc.x + 1][loc.y] = t;
            p = new Point(loc.x + 1, loc.y);
        }
        //ter.renderFrame(world);
        return p;
    }

    // @Source SaveDemo
    private String loadWorld() {
        File f = new File("./previousCodes.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (String) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        /* In the case no World has been saved yet, we return a new one. */
        StdDraw.clear(StdDraw.BLACK);
        System.exit(0);
        return "";

    }

    // @Source SaveDemo
    private static void saveWorld(String seedv) {
        File f = new File("./previousCodes.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(seedv);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /**private void chaseDirection() {
     if (win) {
     Point i;
     if ((avatarLocation.x == flowerLocation.x) && (avatarLocation.y == flowerLocation.x)) {
     win = false;
     } else if ((avatarLocation.x > flowerLocation.x)
     && (avatarLocation.y > flowerLocation.y)) {
     i = moveRight(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     i = moveUp(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     } else if ((avatarLocation.x > flowerLocation.x)
     && (avatarLocation.y < flowerLocation.y)) {
     i = moveRight(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     i = moveDown(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     } else if ((avatarLocation.x < flowerLocation.x)
     && (avatarLocation.y > flowerLocation.y)) {
     i = moveLeft(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     i = moveUp(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     } else if ((avatarLocation.x < flowerLocation.x)
     && (avatarLocation.y < flowerLocation.y)) {
     i = moveLeft(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     i = moveDown(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     } else if ((avatarLocation.x < flowerLocation.x)) {
     i = moveLeft(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     } else if ((avatarLocation.x > flowerLocation.x)) {
     i = moveRight(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     } else if ((avatarLocation.y < flowerLocation.y)) {
     i = moveDown(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     } else if ((avatarLocation.y > flowerLocation.y)) {
     i = moveUp(flowerLocation, Tileset.FLOWER);
     if (i.x != -1) {
     flowerLocation = i;
     }
     }
     }
     }*/

    /**private void winOrLose(boolean winLose) {
     PointerInfo cursor = MouseInfo.getPointerInfo();
     Point floor_or_wall = cursor.getLocation();
     System.out.print(floor_or_wall);
     while (winLose) {
     if (world[floor_or_wall.x][floor_or_wall.y].equals(Tileset.WALL)) {
     //display wall
     } else if (world[floor_or_wall.x][floor_or_wall.y].equals(Tileset.FLOOR)) {
     //display floor
     }
     }
     if (!(winLose)) {
     //we have to quit game and display game over
     }
     }*/


}
