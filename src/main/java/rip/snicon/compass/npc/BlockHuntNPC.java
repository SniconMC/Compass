package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.actions.MenuAction;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.bundels.BundleViewerContainer;
import rip.snicon.compass.utils.TextUtils;

public class BlockHuntNPC extends TemplateNPC {

    public BlockHuntNPC() {
        super(EntityType.PLAYER);

        // Set Spawn Position
        setSpawnPosition(new Pos(26.5, 10.0, 36.5, -165.0f, 0.0f));

        // Set Skin Layers
        setSkinLayer(SkinLayer.NO_CAPE);

        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTcyNDk1NTg3ODY5NywKICAicHJvZmlsZUlkIiA6ICJhZDg4NGExOTQ5NTc0ZDEyYjUwMTViNjc3NDgxY2NjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaW1teVN0b25lIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU0ZjFjMmQwZDY1ZGQ1OWZiNWRjNzJmNTU1NDAxOTMzNjdmYjUxNjNkM2RmNjAzZTZhOWQ1YTE5NWEyYjNmNzMiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yMzQwYzBlMDNkZDI0YTExYjE1YThiMzNjMmE3ZTllMzJhYmIyMDUxYjI0ODFkMGJhN2RlZmQ2MzVjYTdhOTMzIgogICAgfQogIH0KfQ==",
                "mqRCJB3ZiMblbVofvCvZ2HyfkqvO0R4R8PP+nWH2kLN8W/7pYQiF7vdGl3GSQd9jET6bz5rjD75dpRskadeVzLI/D246yUcu0zlOttwZioGaVDFL0cC09t/CZMHOmbcuJv3XD2Ho+fK/dxrwhzGT8qNkJ0Ott+AnJYYCCQXRiI7tQ5Mz6qghFKUvE5WxRrzvfzyk1s3xTUsp6yhX4hOdsVI/IQcQff5QeJY9wfPg7cwqp4d+HkQJ7ybkhHYxe2jJk8L127bFL2SV/Yds6E4Ry0X3S3+ULI5mo1Tb1wbfAvOkmv4pTe5fFxBDfJZLEDnvlgezRIH/HZjbvPYZRRUA4SqImmRsFvY47Qufddff7ufYg/Mles5kp1El5Dw/dGgINyqXdT2rEg80usapG/VRPDs33XYjffwgYbTxejL0BZNXGPq7nA8RFZWFMIg19NuUviKSpM9OZaJFbrogxGv0sdmJBiHgQE/3qB8MjZcTijGUgOchgU2u3DOalFsj2Tlyr+dS6s4JTTcczhsE7lj7JHia6dyOO3RoWPmUjA8nn8qv1IxNh9flUPI+2M3c4ehhmaBLgICIASDNMgGNwXDWNmMF8fhLsJvv7FZoGggZoUmFy3fm7mEzQ95S7vq+XPyvopOc4j7ZyD0md0T44NRrCMWc/cuzZdhHCaI4GCanKnY="
        ));

        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        // Set Name (Hologram-like Text)
        setName(new TemplateText(
                TextUtils.convertStringToComponent("<yellow>-1 Playing</yellow>"),
                TextUtils.convertStringToComponent("<gold>Blockhunt <gray>[0.1]</gray></gold>"),
                TextUtils.convertStringToComponent("<light_purple>Old Update is Available</light_purple>")
        ));
    }

    @Override
    protected void personalize(Player player) {
        setActionList(new ActionList(
                new MenuAction(0, 0, true, new BundleViewerContainer().constructInventory(player)) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        return actionList.getAction(0);
                    }
                }
        ));
    }
}
