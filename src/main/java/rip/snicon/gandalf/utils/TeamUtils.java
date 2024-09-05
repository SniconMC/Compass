package rip.snicon.gandalf.utils;

import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.github.sniconmc.utils.text.ColorUtils;
import com.github.sniconmc.utils.text.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import rip.snicon.gandalf.config.GandalfConfig;

import java.util.List;

public class TeamUtils {


    public static void createTeam(Player player, GandalfConfig config) {

        Team team = MinecraftServer.getTeamManager().createTeam(config.getRankId(), TextUtils.convertStringToComponent(List.of(PlaceholderReplacer.replacePlaceholders(player, config.getRankFormat().getFirst()))), NamedTextColor.WHITE  , Component.empty());

        team.addMember(player.getUsername());
        team.setCollisionRule(TeamsPacket.CollisionRule.NEVER);

    }
}
