package rip.snicon.compass.player.profession;

public enum ProfessionIcon {

    NITWIT( "?"),
    BUTCHER( "\uD83C\uDF56"),
    SHEPHERD( "✂"),
    LEATHERWORKER( "⅗"),
    FLETCHER( "\uD83C\uDFF9"),
    FISHERMAN( "\uD83C\uDFA3"),
    FARMER( "⅖"),
    MASON( "⅕"),
    WEAPONSMITH( "\uD83D\uDDE1"),
    TOOLSMITH( "⅑"),
    ARMORER( "\uD83D\uDEE1"),
    CLERIC( "✝"),
    CARTOGRAPHER( "⅐"),
    LIBRARIAN( "✎");

    private final String icon;

    ProfessionIcon(String icon) {
        this.icon = icon;
    }

    public String icon() {
        return icon;
    }
}
