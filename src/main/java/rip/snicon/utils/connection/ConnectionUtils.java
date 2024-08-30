package rip.snicon.utils.connection;

import net.minestom.server.entity.Player;

public class ConnectionUtils {

    public static void connectToServer(Player player, String serverName){
        player.sendMessage("You are trying to connect to server: " + serverName);
    }
}
