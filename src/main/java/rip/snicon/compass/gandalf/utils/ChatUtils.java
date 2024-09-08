package rip.snicon.compass.gandalf.utils;

import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.github.sniconmc.utils.text.ColorUtils;
import com.github.sniconmc.utils.text.TextUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.AudienceRegistry;
import net.minestom.server.adventure.audience.PacketGroupingAudience;
import net.minestom.server.crypto.FilterMask;
import net.minestom.server.crypto.LastSeenMessages;
import net.minestom.server.crypto.SignedMessageBody;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.play.PlayerChatMessagePacket;
import net.minestom.server.network.packet.server.play.SystemChatPacket;
import rip.snicon.compass.gandalf.GandalfManager;
import rip.snicon.compass.gandalf.config.GandalfProfile;
import rip.snicon.compass.gandalf.config.GandalfProfileSettings;
import rip.snicon.compass.gandalf.config.GandalfRank;

import java.nio.LongBuffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;


public class ChatUtils {

    // TODO use Audiences

    public static void sendChatMessage(Player viewer, Player sender, PlayerChatEvent event) {

        GandalfProfile profile = GandalfManager.getProfiles(viewer);
        if (profile == null) {
            return;
        }
        GandalfRank rank = GandalfManager.getRank(profile.getRank_id());
        if (rank == null) {
            return;
        }

        GandalfProfileSettings settings = profile.getSettings();
        String format = "";

        if (settings.getProfession_format().equals("icon")) {
            format = rank.getRankFormatSimple();
        }
        if (settings.getProfession_format().equals("text")) {
            format = rank.getRankFormat();
        }
        Component message = TextUtils.convertStringToComponent(PlaceholderReplacer.replacePlaceholders(sender, format)).append(Component.text(": " + event.getMessage()).color(ColorUtils.StringToTextColor(rank.getRankChatColor())));
        viewer.sendMessage(message);

    }
}
