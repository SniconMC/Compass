package rip.snicon.compass.player;

public enum PlayerProfession {
    NITWIT(0, "?"),
    BUTCHER(100, "\uD83C\uDF56"),
    SHEPHERD(240, "✂"),
    LEATHERWORKER(400, "⅗"),
    FLETCHER(700, "\uD83C\uDFF9"),
    FISHERMAN(800, "\uD83C\uDFA3"),
    FARMER(1050, "⅖"),
    MASON(1500, "⅕"),
    WEAPONSMITH(2200, "\uD83D\uDDE1"),
    TOOLSMITH(3100, "⅑"),
    ARMORER(4300, "\uD83D\uDEE1"),
    CLERIC(600, "✝"),
    CARTOGRAPHER(8500, "⅐"),
    LIBRARIAN(12000, "✎");

    PlayerProfession(int reqXP, String icon) {
    }
}
