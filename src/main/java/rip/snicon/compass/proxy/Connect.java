package rip.snicon.compass.proxy;

import com.github.sniconmc.utils.text.TextUtils;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.common.TransferPacket;
import rip.snicon.compass.Main;

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

    public static void ConnectPlayerToProxy(Player player, String proxy, String port) {
        String targetHost = proxy + ".localhost";
        Main.logger.info("Connecting to Proxy:" + targetHost);
        player.sendPacket(new TransferPacket(targetHost, Integer.parseInt(port)));
    }

}
