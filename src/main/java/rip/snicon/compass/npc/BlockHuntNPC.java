package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityPose;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.PlayerMeta;
import net.minestom.server.entity.metadata.monster.zombie.ZombieMeta;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.actions.AbstractAction;
import nub.wi1helm.template.npc.actions.ActionList;
import nub.wi1helm.template.npc.actions.DynamicMenuAction;
import nub.wi1helm.template.npc.player.SkinLayer;
import nub.wi1helm.template.npc.player.TemplatePlayerNPC;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.blockhunt.BlockhuntMenu;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.PlayerRank;

public class BlockHuntNPC extends TemplatePlayerNPC implements Interactable {


private ActionList actionList = ActionList.empty();

public BlockHuntNPC() {
    super(MysteryInstanceType.HUB.getInstance(), new Pos(26.5, 10.0, 36.5, -165.0f, 0.0f));

    setSkinLayer(SkinLayer.NO_CAPE);

    // Set Skin
    setSkin(new PlayerSkin(
            "ewogICJ0aW1lc3RhbXAiIDogMTcyNDk1NTg3ODY5NywKICAicHJvZmlsZUlkIiA6ICJhZDg4NGExOTQ5NTc0ZDEyYjUwMTViNjc3NDgxY2NjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaW1teVN0b25lIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU0ZjFjMmQwZDY1ZGQ1OWZiNWRjNzJmNTU1NDAxOTMzNjdmYjUxNjNkM2RmNjAzZTZhOWQ1YTE5NWEyYjNmNzMiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yMzQwYzBlMDNkZDI0YTExYjE1YThiMzNjMmE3ZTllMzJhYmIyMDUxYjI0ODFkMGJhN2RlZmQ2MzVjYTdhOTMzIgogICAgfQogIH0KfQ==",
            "mqRCJB3ZiMblbVofvCvZ2HyfkqvO0R4R8PP+nWH2kLN8W/7pYQiF7vdGl3GSQd9jET6bz5rjD75dpRskadeVzLI/D246yUcu0zlOttwZioGaVDFL0cC09t/CZMHOmbcuJv3XD2Ho+fK/dxrwhzGT8qNkJ0Ott+AnJYYCCQXRiI7tQ5Mz6qghFKUvE5WxRrzvfzyk1s3xTUsp6yhX4hOdsVI/IQcQff5QeJY9wfPg7cwqp4d+HkQJ7ybkhHYxe2jJk8L127bFL2SV/Yds6E4Ry0X3S3+ULI5mo1Tb1wbfAvOkmv4pTe5fFxBDfJZLEDnvlgezRIH/HZjbvPYZRRUA4SqImmRsFvY47Qufddff7ufYg/Mles5kp1El5Dw/dGgINyqXdT2rEg80usapG/VRPDs33XYjffwgYbTxejL0BZNXGPq7nA8RFZWFMIg19NuUviKSpM9OZaJFbrogxGv0sdmJBiHgQE/3qB8MjZcTijGUgOchgU2u3DOalFsj2Tlyr+dS6s4JTTcczhsE7lj7JHia6dyOO3RoWPmUjA8nn8qv1IxNh9flUPI+2M3c4ehhmaBLgICIASDNMgGNwXDWNmMF8fhLsJvv7FZoGggZoUmFy3fm7mEzQ95S7vq+XPyvopOc4j7ZyD0md0T44NRrCMWc/cuzZdhHCaI4GCanKnY="
    ));
/*
    // Set Name (Hologram-like Text)
    setName(new TemplateText(
            TextUtils.convertStringToComponent("<yellow>-1 Playing</yellow>"),
            TextUtils.convertStringToComponent("<gold>Blockhunt <gray>[0.1]</gray></gold>"),
            TextUtils.convertStringToComponent("<light_purple>Old Update is Available</light_purple>")
    ));*/


    }

    @Override
    public void personalize(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;

        editEntityMeta(PlayerMeta.class, meta -> {
            if (p.getDataHandler().getRank() == PlayerRank.EVOKER) {
                setPose(EntityPose.SITTING);
            } else {
                setPose(EntityPose.STANDING);
            }
        });

        setActionList(new ActionList(
                new DynamicMenuAction(0, 0, true, new BlockhuntMenu()) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        return actionList.getAction(0);
                    }
                }
        ));
    }

    @Override
    public @NotNull ActionList getActionList() {
        return actionList;
    }

    @Override
    public void setActionList(@NotNull ActionList actionList) {
        this.actionList = actionList;
    }
}
