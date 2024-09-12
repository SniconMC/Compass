package rip.snicon.compass.gandalf;

import net.minestom.server.entity.Player;
import rip.snicon.compass.gandalf.config.GandalfProfession;
import rip.snicon.compass.gandalf.config.GandalfProfile;
import rip.snicon.compass.gandalf.config.GandalfRank;
import rip.snicon.compass.gandalf.utils.LoadGandalf;

public class ProfilePlayer extends Player {

    private GandalfProfile profile;
    private GandalfProfession profession;
    private GandalfRank rank;

    public ProfilePlayer(Player player, GandalfProfession profession, GandalfRank rank) {
        super(player.getUuid(), player.getUsername(), player.getPlayerConnection());
        this.profile = profile;
        this.profession = profession;
        this.rank = rank;
    }
}
