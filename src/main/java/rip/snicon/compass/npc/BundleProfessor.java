/*
package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.actions.AbstractAction;
import nub.wi1helm.template.npc.actions.ActionList;
import nub.wi1helm.template.npc.actions.DialogAction;
import nub.wi1helm.template.npc.actions.MenuAction;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.blockhunt.bundlebot.BundleProfessorContainer;
import rip.snicon.compass.inventory.fisherman.FishMerchantInventory;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.MysteryToggles;
import rip.snicon.compass.utils.TextUtils;

public class BundleProfessor extends TemplatePlayerNPC {
    public BundleProfessor() {

        setPosition(new Pos(11.5,6,107.5,160, 0));

        // Set Skin Layers
        setSkinLayer(SkinLayer.NO_CAPE);
        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTY1OTIyMDk4OTg5NiwKICAicHJvZmlsZUlkIiA6ICIwNTkyNTIxZGNjZWE0NzRkYjE0M2NmMDg2MDA1Y2FkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJwdXIyNCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NTgwODczZWNkNDM1NGJlZjljOGVjZWQ2OWUwMzJiMTFkNzUyYzZiNWMzYTZmMzA2M2ZhNzdlNjczNGExNDUyIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                "poGP286S2bG43JkrZ4izY6UkWo7fyONqy4uAgPCNVVvxcnDejTRIwxAljujfTx/D8tQEz3x0/O3NPdqvvty4cRWYUTYjvB/noFnzdmEhQSgocJPRC+ei6FJlQ73xqCMA76fYuKR5cmX7TjACD5tJ7Yxi7Ib8qsvsBvAcXT5ZJOd4fAxJGKTX3mq/Tl0ES4uVaLgL57nhau78eFS6s8xGPB5l2AF16Qcfe7onROCJHr8dQQTUSpPmC7fN/Yn2Ic/P1W82g5MvqPYXel93WP4fTBbkfA9UCXpgs13dGnBy4fmkIahkaOPj1AKMmvvKW7HHPegNbR6J7mVtTRi4eHQUw1u5aJfA1+EOOotnj3KYKraxxuN8goadMVpKCsM+OLRtQzZcdQNO0MmSP8onrBdRD60/afa5MiMWjO6sYBnFgIvyiv7VP8I+Q9Ccza05J0G6nkhCbz931cbRX2ZeJngAqKMEFyPs1Vb2AULBtFVHDLiISOdsVaLKbS5q9fR3i3JP6LE5tyGpIh8t/xGV3f8z7QZBm3/halsqlS2r8tBHKcUVymeNECwQtCvyILZyAlj01xcavmka2jfnEvIC7xV5qffSktHPqnwNrlHfT9CihAQBKHgLR/IrOh7yQU/1B+QnHSoCnTSa++YAwvCZhIpBbwe97fjEJ1UC53kefRDX9sk="));

        */
/*
        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        // Set AI Goal (Look at players)
        setGoal(new LookAtPlayerGoal(this, 7, getPosition()));

        // Set Name (Hologram-like Text)
        setName(new TemplateText(
                TextUtils.convertStringToComponent("<yellow>Right Click<yellow>"),
                TextUtils.convertStringToComponent("<gold>Bundle Professor</gold>")
        ));

         *//*

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
                        if (!p.getToggleHandler().hasToggle(MysteryToggles.FIRST_TALK_BUNDLE_PROFESSOR)) {
                            p.getToggleHandler().addToggle(MysteryToggles.FIRST_TALK_BUNDLE_PROFESSOR);
                            return actionList.getAction(1);
                        }
                        // Otherwise, go straight to the shop (action 4)
                        return actionList.getAction(5);
                    }
                },

                // Action 1,2,3: The conversation lines
                new DialogAction(1, 0, false, TextUtils.convertStringToComponent("<grey>[<yellow>NPC</yellow>]</grey> <white>Bundle Professor</white>: Hello")) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        return actionList.getAction(2);
                    }
                },
                new DialogAction(2, 2000, false, TextUtils.convertStringToComponent("<grey>[<yellow>NPC</yellow>]</grey> <white>Bundle Professor</white>: Im the creator of the Bundle Bot and relized i built it in a bad spot")) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        return actionList.getAction(3);
                    }
                },
                new DialogAction(3, 2000, false, TextUtils.convertStringToComponent("<grey>[<yellow>NPC</yellow>]</grey> <white>Bundle Professor</white>: Therefor i also build a fast travel to this location. But i don't want anyone to use it.")) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        // after the last line, jump back to action 0
                        // so that the next click restarts the logic
                        return actionList.getAction(4);
                    }
                },
                new DialogAction(4, 2000, false, TextUtils.convertStringToComponent("<grey>[<yellow>NPC</yellow>]</grey> <white>Bundle Professor</white>: Complete my 5 challenges to get access to it or just walk here like a looser.")) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        // after the last line, jump back to action 0
                        // so that the next click restarts the logic
                        return actionList.getAction(0);
                    }
                },

                // Action 4: Open the menu
                new MenuAction(5, 0, false, new BundleProfessorContainer().constructInventory(player)) {
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
*/
