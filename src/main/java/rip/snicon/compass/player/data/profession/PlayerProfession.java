package rip.snicon.compass.player.data.profession;

public enum PlayerProfession {
    NITWIT(0, ProfessionIcon.NITWIT),
    BUTCHER(100, ProfessionIcon.BUTCHER),
    SHEPHERD(240, ProfessionIcon.SHEPHERD),
    LEATHERWORKER(400, ProfessionIcon.LEATHERWORKER),
    FLETCHER(700, ProfessionIcon.FLETCHER),
    FISHERMAN(800, ProfessionIcon.FISHERMAN),
    FARMER(1050, ProfessionIcon.FARMER),
    MASON(1500, ProfessionIcon.MASON),
    WEAPONSMITH(2200, ProfessionIcon.WEAPONSMITH),
    TOOLSMITH(3100, ProfessionIcon.TOOLSMITH),
    ARMORER(4300, ProfessionIcon.ARMORER),
    CLERIC(600, ProfessionIcon.CLERIC),
    CARTOGRAPHER(8500, ProfessionIcon.CARTOGRAPHER),
    LIBRARIAN(12000, ProfessionIcon.LIBRARIAN);

    private final int reqXP;
    private final ProfessionIcon icon;

    PlayerProfession(int reqXP, ProfessionIcon icon) {
        this.reqXP = reqXP;
        this.icon = icon;
    }

    public int getReqXP() {
        return reqXP;
    }

    public ProfessionIcon getIconData() {
        return icon;
    }
}