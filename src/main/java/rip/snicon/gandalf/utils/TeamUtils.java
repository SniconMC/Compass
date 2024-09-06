package rip.snicon.gandalf.utils;

import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.github.sniconmc.utils.text.ColorUtils;
import com.github.sniconmc.utils.text.TextUtils;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import rip.snicon.gandalf.GandalfMain;
import rip.snicon.gandalf.config.GandalfConfig;

import java.util.List;
import java.util.UUID;

public class TeamUtils {


    public static void createTeam(Player player, GandalfConfig config) {
        // Get the raw rank format (containing the placeholder $(username))
        String rawRankFormat = config.getRankFormatSimple().getFirst();

        // Split the raw rank format into prefix and suffix using the $(username) placeholder
        String prefix = getPrefixBeforeUsernamePlaceholder(rawRankFormat);
        String suffix = getSuffixAfterUsernamePlaceholder(rawRankFormat);
        GandalfMain.logger.warn("prefix:" + prefix + " suffix: " + suffix);
        // Replace placeholders in both prefix and suffix
        String replacedPrefix = PlaceholderReplacer.replacePlaceholders(player, String.valueOf(prefix));
        String replacedSuffix = PlaceholderReplacer.replacePlaceholders(player, String.valueOf(suffix));

        // Create the team with the replaced prefix and suffix
        Team team = MinecraftServer.getTeamManager().createTeam(
                config.getRankId(),
                TextUtils.convertStringToComponent(List.of(replacedPrefix)),  // Set the team's prefix
                NamedTextColor.WHITE,            // You can set the team's color as needed
                TextUtils.convertStringToComponent(List.of(replacedSuffix))   // Set the team's suffix
        );

        // Add the player to the team
        team.addMember(player.getUsername());

        // Set the collision rule
        team.setCollisionRule(TeamsPacket.CollisionRule.NEVER);
    }



    public static void createTeam(Player player, UUID uuid, List<String> prefix, List<String> suffix) {

        Team team = MinecraftServer.getTeamManager().createTeam(uuid.toString(), Component.empty(), TextUtils.convertStringToComponent(prefix) , NamedTextColor.WHITE  , TextUtils.convertStringToComponent(suffix));

        team.addMember(player.getUsername() + " ");
        team.setCollisionRule(TeamsPacket.CollisionRule.NEVER);

    }

    public static void changeApperence(Player player, UUID uuid, List<String> prefix, List<String> suffix) {
        player.sendMessage(uuid.toString());
        Team team = MinecraftServer.getTeamManager().getTeam(uuid.toString());

        team.updatePrefix(TextUtils.convertStringToComponent(prefix));
        team.updateSuffix(TextUtils.convertStringToComponent(suffix));
        team.setCollisionRule(TeamsPacket.CollisionRule.NEVER);

    }

    public static String getPrefixBeforeUsernamePlaceholder(String input) {
        String placeholder = "$(username)";

        // If placeholder does not exist, treat the entire string as the prefix
        if (!input.contains(placeholder)) {
            return input; // Return the entire input as the prefix
        }

        // Find the position of the $(username) placeholder
        int usernameIndex = input.indexOf(placeholder);

        // Return everything before the $(username) as the prefix, keeping the space intact
        return input.substring(0, usernameIndex);
    }

    public static String getSuffixAfterUsernamePlaceholder(String input) {
        String placeholder = "$(username)";

        // If placeholder does not exist, treat the entire string as the suffix
        if (!input.contains(placeholder)) {
            return input; // Return the entire input as the suffix
        }

        // Find the position of the $(username) placeholder
        int usernameIndex = input.indexOf(placeholder);

        // Return everything after the $(username) as the suffix, keeping the space intact
        return input.substring(usernameIndex + placeholder.length());
    }





}
