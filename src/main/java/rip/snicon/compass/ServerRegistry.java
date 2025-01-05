package rip.snicon.compass;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import build.buf.gen.minekube.gate.v1.*;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.common.TransferPacket;

import java.util.List;

public class ServerRegistry {

    private static final String GATE_HOST = Main.getProxyAddress().split(":")[0];
    private static final int GATE_PORT = 8080;

    private static final ManagedChannel channel = ManagedChannelBuilder
            .forAddress(GATE_HOST, GATE_PORT)
            .usePlaintext()
            .build();
    private static final GateServiceGrpc.GateServiceBlockingStub stub = GateServiceGrpc.newBlockingStub(channel);

    /**
     * Shutdown the gRPC channel.
     */
    public static void shutdown() {
        channel.shutdown();
    }

    /**
     * Registers a server with Gate.
     *
     * @param name    The server's unique name.
     * @param address The server's address (e.g., "127.0.0.1:25566").
     */
    public static void registerServer(String name, String address) {
        RegisterServerRequest request = RegisterServerRequest.newBuilder()
                .setName(name)
                .setAddress(address)
                .build();
        try {
            stub.registerServer(request);
            System.out.println("Server registered: " + name);
        } catch (Exception e) {
            System.err.println("Failed to register server: " + e.getMessage());
        }
    }

    /**
     * Unregisters a server from Gate.
     *
     * @param name The server's unique name.
     */
    public static void unregisterServer(String name) {
        UnregisterServerRequest request = UnregisterServerRequest.newBuilder()
                .setName(name)
                .build();
        try {
            stub.unregisterServer(request);
            System.out.println("Server unregistered: " + name);
        } catch (Exception e) {
            System.err.println("Failed to unregister server: " + e.getMessage());
        }
    }

    /**
     * Lists all registered servers.
     *
     * @return A list of servers.
     */
    public static List<Server> listServers() {
        try {
            ListServersResponse response = stub.listServers(ListServersRequest.getDefaultInstance());
            return response.getServersList();
        } catch (Exception e) {
            System.err.println("Failed to list servers: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Connects a player to a specified server.
     *
     * @param playerName The player's username or UUID.
     * @param serverName The target server's name.
     */
    public static void connectPlayerToServer(String playerName, String serverName) {
        ConnectPlayerRequest request = ConnectPlayerRequest.newBuilder()
                .setPlayer(playerName)
                .setServer(serverName)
                .build();
        try {
            stub.connectPlayer(request);
            System.out.println("Player " + playerName + " connected to server: " + serverName);
        } catch (Exception e) {
            System.err.println("Failed to connect player to server: " + e.getMessage());
        }
    }

    /**
     * Disconnects a player from the proxy.
     *
     * @param playerName The player's username or UUID.
     * @param reason     The reason for disconnection.
     */
    public static void disconnectPlayer(String playerName, String reason) {
        DisconnectPlayerRequest request = DisconnectPlayerRequest.newBuilder()
                .setPlayer(playerName)
                .setReason(reason)
                .build();
        try {
            stub.disconnectPlayer(request);
            System.out.println("Player " + playerName + " disconnected: " + reason);
        } catch (Exception e) {
            System.err.println("Failed to disconnect player: " + e.getMessage());
        }
    }

    /**
     * Sends a player to a proxy using TransferPacket.
     *
     * @param player The player to transfer.
     * @param ip     The proxy IP address.
     * @param port   The proxy port.
     */
    public static void connectPlayerToProxy(Player player, String ip, int port) {
        if (player == null || ip == null) {
            return;
        }
        player.sendMessage("<gray>Connecting to proxy: " + ip + ":" + port + "</gray>");
        player.sendPacket(new TransferPacket(ip, port));
        System.out.println("Player " + player.getUsername() + " connected to proxy: " + ip + ":" + port);
    }

    /**
     * Sends an AddJoinableServer plugin message.
     *
     * @param player     The player to send the message.
     * @param serverName The server to add as joinable.
     */
    public static void sendAddJoinablePluginMessage(Player player, String serverName) {
        if (player == null || serverName == null) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("AddJoinableServer");
        out.writeUTF(serverName);
        player.sendPluginMessage("bungeecord:main", out.toByteArray());
        System.out.println("Sent AddJoinableServer for: " + serverName);
    }

    /**
     * Sends a RemoveJoinableServer plugin message.
     *
     * @param player     The player to send the message.
     * @param serverName The server to remove from joinable.
     */
    public static void sendRemoveJoinablePluginMessage(Player player, String serverName) {
        if (player == null || serverName == null) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("RemoveJoinableServer");
        out.writeUTF(serverName);
        player.sendPluginMessage("bungeecord:main", out.toByteArray());
        System.out.println("Sent RemoveJoinableServer for: " + serverName);
    }
}
