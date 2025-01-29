package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.cosmetics.PlayerHelmet;
import rip.snicon.compass.utils.TextUtils;

public class GuideNPC extends TemplateNPC {

    public GuideNPC() {
        super(EntityType.PLAYER);

        // Set Spawn Position
        setSpawnPosition(new Pos(38.0, 9.0, 37.0, 150.0f, 0.0f));

        // Set Skin Layers
        setSkinLayer(SkinLayer.NO_CAPE);
        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTczNTU5MzAxMjM4NywKICAicHJvZmlsZUlkIiA6ICI3NmIwM2FiYjk5YzQ0MzgwOTQ4MDM4ZjFiNGJiYzgxOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGFtc2l0ZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hYTU4MWNjNTgxYWNkYjA1ZjFlMWU2NWI4ZDMyNmM1MTBjYTgxNGZmYjEwNjYyYTQ0ZWZlYmVhMmMyOGJiNTBiIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81YzI5NDEwMDU3ZTMyYWJlYzAyZDg3MGVjYjUyZWMyNWZiNDVlYTgxZTc4NWE3ODU0YWU4NDI5ZDcyMzZjYTI2IgogICAgfQogIH0KfQ==",
                "R2eNRaudWbbu6QbrqWtR9SRdKXmnXfUDAZVsEl+ZWFdVsCr+wahQb849YaBKj8D5avA+TKdTx6EG3CSv/Q60P3fC6g1TaDMZdRvjNy2Q2rYJ8N2glfEUtSljAmVxRNH2gT7dTaSkzCMCvAYp4tGFwzpDG/liATR27oBXUqfvIdNZ20Ao/JeioxPsrKuu+Oj5WiQ354gWSXCJHpGZZQ51/2r807q28vUMtc8vDl0fNtnpkqNMAtdycHr/vi1Z3290++v8L4EkIxCS07CrgxrM7RMDmKmyWSCOp67zECjxlwmyUACBUsst4hGzHT6WatN2tsv6IkgsFMQBlVWt1NmaGWy/N0NNTIxoOgwF/Rd5zUpmFgYyxQ0Tt91w0d1v2SbqG6JjDh8hG7vbaJbL66kdvWnMUjXB2u2wZ2cqTMPm+/yygFsJQ1LLbXPtEyYoVu2/brHpvJx+uh77maJ4qn0xE1hDSSnI0oabaxSPucbFEbX9vkGzSX+MrYlIhIYyf+mIfR9UpR8bjCb01IJV7GyL3NX0SuU1YuN9CLqFJsGQsV73nEdqOuyUj7I9Pgv5MRfRgSgQ+KyI37PpsAHmZHeKWE6WEvOukbkpVOvVsvOYg3CIqrZ6c1gON9GxcMX34ZKSf3pGcEzUz0XY+y+W5w9KE+RB6WDluHebL4kce/4hZwI="
        ));

        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        // Set AI Goal (Look at players)
        setGoal(new LookAtPlayerGoal(this, 7, getSpawnPosition()));

        // Set Name (Hologram-like Text)
        setName(new TemplateText(
                TextUtils.convertStringToComponent("<yellow>Right Click</yellow>"),
                TextUtils.convertStringToComponent("<green>Guide</green>"),
                TextUtils.convertStringToComponent("<white>Browse Ingame Wiki</white>")
        ));
    }

    @Override
    protected void personalize(Player player) {
        // Helmet toggle interaction
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
}
