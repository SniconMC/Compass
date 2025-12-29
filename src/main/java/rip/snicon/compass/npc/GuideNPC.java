package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityPose;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.network.packet.server.SendablePacket;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.actions.AbstractAction;
import nub.wi1helm.template.npc.actions.ActionList;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import nub.wi1helm.template.npc.hologram.TemplateTextNPC;
import nub.wi1helm.template.npc.player.SkinLayer;
import nub.wi1helm.template.npc.player.TemplatePlayerNPC;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.PlayerRank;
import rip.snicon.compass.player.data.cosmetics.PlayerHelmet;
import rip.snicon.compass.utils.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GuideNPC extends TemplatePlayerNPC implements Interactable, Nameable {

    private ActionList actionList = ActionList.empty();

    public GuideNPC() {
        super(MysteryInstanceType.HUB.getInstance(), new Pos(38.0, 9.0, 37.0, 150.0f, 0.0f));


        // Set Skin Layers
        setSkinLayer(SkinLayer.NO_CAPE);
        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTczNTU5MzAxMjM4NywKICAicHJvZmlsZUlkIiA6ICI3NmIwM2FiYjk5YzQ0MzgwOTQ4MDM4ZjFiNGJiYzgxOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGFtc2l0ZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hYTU4MWNjNTgxYWNkYjA1ZjFlMWU2NWI4ZDMyNmM1MTBjYTgxNGZmYjEwNjYyYTQ0ZWZlYmVhMmMyOGJiNTBiIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81YzI5NDEwMDU3ZTMyYWJlYzAyZDg3MGVjYjUyZWMyNWZiNDVlYTgxZTc4NWE3ODU0YWU4NDI5ZDcyMzZjYTI2IgogICAgfQogIH0KfQ==",
                "R2eNRaudWbbu6QbrqWtR9SRdKXmnXfUDAZVsEl+ZWFdVsCr+wahQb849YaBKj8D5avA+TKdTx6EG3CSv/Q60P3fC6g1TaDMZdRvjNy2Q2rYJ8N2glfEUtSljAmVxRNH2gT7dTaSkzCMCvAYp4tGFwzpDG/liATR27oBXUqfvIdNZ20Ao/JeioxPsrKuu+Oj5WiQ354gWSXCJHpGZZQ51/2r807q28vUMtc8vDl0fNtnpkqNMAtdycHr/vi1Z3290++v8L4EkIxCS07CrgxrM7RMDmKmyWSCOp67zECjxlwmyUACBUsst4hGzHT6WatN2tsv6IkgsFMQBlVWt1NmaGWy/N0NNTIxoOgwF/Rd5zUpmFgYyxQ0Tt91w0d1v2SbqG6JjDh8hG7vbaJbL66kdvWnMUjXB2u2wZ2cqTMPm+/yygFsJQ1LLbXPtEyYoVu2/brHpvJx+uh77maJ4qn0xE1hDSSnI0oabaxSPucbFEbX9vkGzSX+MrYlIhIYyf+mIfR9UpR8bjCb01IJV7GyL3NX0SuU1YuN9CLqFJsGQsV73nEdqOuyUj7I9Pgv5MRfRgSgQ+KyI37PpsAHmZHeKWE6WEvOukbkpVOvVsvOYg3CIqrZ6c1gON9GxcMX34ZKSf3pGcEzUz0XY+y+W5w9KE+RB6WDluHebL4kce/4hZwI="
        ));
    }

    @Override
    public void personalize(Player player) {

        final MysteryPlayer p = (MysteryPlayer) player;


        if (p.getDataHandler().getRank() == PlayerRank.EVOKER) {

            setName(MysteryInstanceType.HUB.getInstance(),
                    TextUtils.convertStringToComponent("<yellow></yellow>"),
                    TextUtils.convertStringToComponent("<red>Guide</red>"),
                    TextUtils.convertStringToComponent("<white>Browse Ingame Wiki</white>")
            );
        } else {
            setName(MysteryInstanceType.HUB.getInstance(),
                    TextUtils.convertStringToComponent("<yellow>Right Click</yellow>"),
                    TextUtils.convertStringToComponent("<green>Guide</green>"),
                    TextUtils.convertStringToComponent("<white>Browse Ingame Wiki</white>")
            );
        }

        setActionList(new ActionList(
                new AbstractAction(0, 0, true) {
                    @Override
                    public void execute(Player player) {
                        final MysteryPlayer p = (MysteryPlayer) player;
                        if (p.getCosmeticHandler().isEnabled(PlayerHelmet.IRON_HELMET)) {
                            p.sendMessage("Disabled");
                            p.getCosmeticHandler().disableCosmetic(PlayerHelmet.IRON_HELMET);
                        } else {
                            p.sendMessage("Enabled");
                            p.getCosmeticHandler().enableCosmetic(PlayerHelmet.IRON_HELMET);
                        }
                    }

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


    @Override
    public Collection<SendablePacket> getNpcSpawnPackets(Player player) {

        List<SendablePacket> packets = new ArrayList<>(super.getNpcSpawnPackets(player));

        packets.addAll(getNameSpawnPackets(player));

        return packets;
    }
}
