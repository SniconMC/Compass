package rip.snicon.modules.momentum.json;

import net.minestom.server.coordinate.Pos;

public class Coordinates {

    private int x1;
    private int y1;
    private int z1;

    private int x2;
    private int y2;
    private int z2;

    public Pos getCorner1() {
        return new Pos(x1, y1, z1);
    }

    public Pos getCorner2() {
        return new Pos(x2, y2, z2);
    }
}
