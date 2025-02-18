package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.actions.DialogAction;
import nub.wi1helm.template.npc.actions.MenuAction;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.debug.DebugContainer;
import rip.snicon.compass.inventory.fisherman.FishMerchantInventory;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.MysteryToggles;
import rip.snicon.compass.utils.TextUtils;

public class FishMerchant extends TemplateNPC {
    public FishMerchant() {
        super(EntityType.PLAYER);

        setSpawnPosition(new Pos(-42.5,5,-1.5,0, 0));
        // Set Skin Layers
        setSkinLayer(SkinLayer.NO_CAPE);
        setSpawnStrategy(SpawnStrategy.STANDING);
        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTY2MjU2OTA1NDI0NSwKICAicHJvZmlsZUlkIiA6ICIzZGRhMDc1ZTQ0Yzc0MmZhODE2YTNjNTNkY2RlMWQ3ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJzbmFLQVkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJmM2QxMGRhOTlhZjZlNjkyODFhNTU2NzNkYmJlN2M3MjM1N2QyMjc1YTJhNjVjYTkyYjc2YmZmMWZlZDI5YyIKICAgIH0KICB9Cn0=",
                "qMpNVownHS3GGBoNKeH5x6G3Ua3CkTzpZ2lTw3Mh39kbaIhSKfH02DZMc84MFnM/Lh88oY6yPwFbbdXB2vw0IwZrH9E2f40NfFmWCuZpRZvXbZBBY2MulUq03MJZkMAaK1KkK6cvJLUhfVH1vVpRmAoG2QdIuUnTG5HXkEkeectgpj3cIrZimGgnMtzLMdpaVsLdii/rUEN8I78bpjqaSzW7TtIdcTXYFDariX2KXPXpwKPZ6c5OyFSDnXpAeSwjODilLZn/CV3jiqlXOS6oRtxTK0EN1UxlIg9eJaPO2aLtrpKQbNU4KHdq0qq/npUKhBrUbCVhbg3trF2VCXrSIZZg9uusUZaE7r9djdGiURDdryJR/oMtoz/MsN92Deekvdn56Q2WF4ih7bIjQNg2vhLqjzF/9ux7+F+LUhP/PxgO/LJSWKl3TS7+LlYg+eiOG8E7eBEy42J8o9Z74RYaxZ5jP/ZWCK0ILKMikCWpJumgoB3BHJ1BnHOA2fx/1rvfvsL85Th3O1i9KAGQRETth5h/GpmwBCVkvKmcIyXlzWTVMpr3ZshF5wkYZ0b/j1wK7mKLU02aq+hMNIctBbjdmFHom2LWlWaPK9EphSvsQdz6pjh+HX6nDAb6XmaAoO7keuTZ1jyi6epfifKQa4msxzls0nzZdre+9P9ykgIU7M0="
        ));
        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        // Set AI Goal (Look at players)
        setGoal(new LookAtPlayerGoal(this, 7, getSpawnPosition()));

        // Set Name (Hologram-like Text)
        setName(new TemplateText(
                TextUtils.convertStringToComponent("<yellow>Right Click<yellow>"),
                TextUtils.convertStringToComponent("<grey>[<yellow>NPC</yellow>]</grey> <white>Fish Merchant</white>")
        ));
    }

    @Override
    protected void personalize(Player player) {
        setActionList(new ActionList(
                // Action 0: Entry point for ANY click on this NPC
                new AbstractAction(0, 0, true) {
                    @Override
                    public void execute(Player player) {
                        // No immediate output needed here
                    }

                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        MysteryPlayer p = (MysteryPlayer) player;
                        // If they haven't toggled, go to the dialogue chain (action 1)
                        if (!p.getToggleHandler().hasToggle(MysteryToggles.FIRST_TALK_FISH_MERCHANT)) {
                            p.getToggleHandler().addToggle(MysteryToggles.FIRST_TALK_FISH_MERCHANT);
                            return actionList.getAction(1);
                        }
                        // Otherwise, go straight to the shop (action 4)
                        return actionList.getAction(4);
                    }
                },

                // Action 1,2,3: The conversation lines
                new DialogAction(1, 0, false, TextUtils.convertStringToComponent("<grey>[<yellow>NPC</yellow>]</grey> <white>Fish Merchant</white>: Hello")) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        return actionList.getAction(2);
                    }
                },
                new DialogAction(2, 2000, false, TextUtils.convertStringToComponent("<grey>[<yellow>NPC</yellow>]</grey> <white>Fish Merchant</white>: I sell fish themed items")) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        return actionList.getAction(3);
                    }
                },
                new DialogAction(3, 2000, false, TextUtils.convertStringToComponent("<grey>[<yellow>NPC</yellow>]</grey> <white>Fish Merchant</white>: Click me again to open my shop")) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        // after the last line, jump back to action 0
                        // so that the next click restarts the logic
                        return actionList.getAction(0);
                    }
                },

                // Action 4: Open the menu
                new MenuAction(4, 0, false, new FishMerchantInventory().constructInventory(player)) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList list) {
                        // After closing the menu, you could jump back to action 0
                        // or do something else entirely.
                        return list.getAction(0);
                    }
                }
        ));
    }
}
