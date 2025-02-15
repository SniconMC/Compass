package rip.snicon.compass.player.data;

public enum PlayerRank {
    VILLAGER("gray"),
    TRADER("yellow"),
    GOLEM("white"),
    ALLAY("aqua"),
    VEX("dark_aqua"),
    VINDICATOR("dark_blue"),
    EVOKER("red");

    private final String color;

    PlayerRank(String color) {
        this.color = color;
    }

    public String getColor(){
        return color;
    }
}
