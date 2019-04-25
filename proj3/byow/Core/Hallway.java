package byow.Core;

import java.awt.Point;

public class Hallway {
    Point p;
    int length;
    boolean horizontal;
    public Hallway(Point p, int l, boolean h) {
        this.length = l;
        this.p = p;
        this.horizontal = h;
    }
}
