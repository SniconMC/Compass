package rip.snicon.gandalf.data;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;
import rip.snicon.gandalf.config.GandalfPermission;
import rip.snicon.gandalf.config.GandalfProfession;
import rip.snicon.gandalf.config.GandalfProfile;
import rip.snicon.gandalf.config.GandalfRank;

import java.util.UUID;

public class PlayerProfile extends Player {

    GandalfRank rankConfig;
    GandalfProfile profileConfig;
    GandalfProfession professionConfig;



    public PlayerProfile(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }

    public PlayerProfile(@NotNull Player player, @NotNull GandalfRank rankConfig, @NotNull GandalfProfile profileConfig, @NotNull GandalfProfession professionConfig){
        super(player.getUuid(), player.getUsername(), player.getPlayerConnection());
        this.rankConfig = rankConfig;
        this.profileConfig = profileConfig;
        this.professionConfig = professionConfig;
    }


    public GandalfRank getRank(){
        return rankConfig;
    }

    public GandalfProfile getProfile() {
        return profileConfig;
    }

    public GandalfProfession getProfession(){
        return professionConfig;
    }

    public void setRank(GandalfRank rankConfig) {
        this.rankConfig = rankConfig;
    }

    public void setProfile(GandalfProfile profileConfig) {
        this.profileConfig = profileConfig;
    }

    public void setProfession(GandalfProfession professionConfig) {
        this.professionConfig = professionConfig;
    }
}
