package rip.snicon.modules.momentum.json;

import net.minestom.server.coordinate.Pos;

public class Coordinates {

    private double x1;
    private double y1;
    private double z1;

    private double x2;
    private double y2;
    private double z2;

    public Pos getCorner1() {
        return new Pos(x1, y1, z1);
    }

    public Pos getCorner2() {
        return new Pos(x2, y2, z2);
    }
}
