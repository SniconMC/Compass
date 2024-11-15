package rip.snicon.compass.proxy;

import com.github.sniconmc.utils.text.TextUtils;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minestom.server.entity.Player;

public class Connect {

    public static void ConnectPlayerToServer(Player player, String server) {

        if (player == null || server == null) {
            return;
        }
        player.sendMessage(TextUtils.convertStringToComponent("<gray>Connecting to server: " + server + "</gray>"));

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage("bungeecord:main", out.toByteArray());
    }


}
