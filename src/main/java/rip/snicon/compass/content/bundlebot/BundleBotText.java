package rip.snicon.compass.content.bundlebot;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityPositionPacket;
import net.kyori.adventure.text.Component;
import rip.snicon.compass.utils.TextUtils;

public class BundleBotText {
    /*
    private final TemplateText templateText;

    private final Pos spawnPos = new Pos(25,2,102.5);
    // Configuration
    private static final double BASE_HEIGHT = 2.0;
    private static final double OFFSET = 0.3;
    public BundleBotText(Player player) {
        templateText = new TemplateText(
                TextUtils.convertStringToComponent("<yellow>Right Click</yellow>"),
                TextUtils.convertStringToComponent("<aqua>The Bundle Bot</aqua>"),
                TextUtils.convertStringToComponent("<aqua>Welcome " + player.getUsername() + " To</aqua>"));
    }

    /**
     * Spawns the holograms for the specified player.

    public void spawn(Player player) {



        templateText.getText().forEach((row, entity) -> {
            Pos hologramPos = spawnPos.add(0, BASE_HEIGHT + (row * OFFSET), 0);

            entity.setInstance(player.getInstance(), hologramPos);
            player.sendPacket(entity.getMetadataPacket());
            player.sendPacket(new EntityPositionPacket());
            player.sendPacket(entity.getEntityType().registry().spawnType().getSpawnPacket(entity));
            entity.updateNewViewer(player);
        });
    }

    /**
     * Removes all holograms.

    public void remove(Player player) {
        templateText.getText().forEach((integer, entity) -> {
            player.sendPacket(new DestroyEntitiesPacket(entity.getEntityId()));
            entity.updateOldViewer(player);
        });
    }

    /**
     * Updates a specific row with new text.

    public void updateRow(int row, Component text) {
        templateText.setRow(row, text);
    }

    /**
     * Checks if the text display is empty.

    public boolean isEmpty() {
        return templateText.isEmpty();
    }
    */
}
