package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.actions.MenuAction;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.cosmetics.CosmeticsShop;
import rip.snicon.compass.utils.TextUtils;

public class CosmeticDealerNPC extends TemplateNPC {

    public CosmeticDealerNPC() {
        super(EntityType.PLAYER);

        // Set Spawn Position
        setSpawnPosition(new Pos(29.5, 10.0, 1.5, 0.0f, 0.0f));

        // Set Skin Layers
        setSkinLayer(SkinLayer.NO_CAPE);

        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NzQ0NzIwNDA2NSwKICAicHJvZmlsZUlkIiA6ICIwZWQ2MDFlMDhjZTM0YjRkYWUxZmI4MDljZmEwNTM5NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJOZWVkTW9yZUFjY291bnRzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI4ZmYyZmVlN2ZmOWVjY2MxNGU3N2NhNWZjZWNmNGFhYmVmY2Y1M2NlZmYyZDU4ZTMxOWIzODgwZjY5NDUzYTIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "a7mXkCW/rSNrY2gOQjKBg63BloHiszji3a/FTqHAPwUMQO7lNZNETuRdWY/hGshA+mTNKaYQWywAOr6evVAyMB9GZyNANxICgb876u4t6razIqNZHvRV27ScA+L0hU+hd2ZKI1EWg/8wGr998coVLdLzdwyB7qDlWqCdisEfPcp7w+TSb/pfvVSbnsJRbhhVNiQdZdJX/1UVG7CyDPLKzTWoqkZQehA00W2zJR1SSfYRjjSoCsaVlC6j8Y3JV5mZRI5ntYNqO2rGm3768x8L05YSuDbBxFei9/sZ2itnwITsh9cIv5iEHg2tBHY68klZRYgaSpSEWJkobR/5urzqTLC3m1kEc2LzxcxfidOSiWrIE7rizLQTLAGhG5ZIjxQHk62MRddgT2ZO4N+VhRORv0OqWg8yUktp6P6u1xPaZ8URpvb9xr8DPr+c9mfH1MSBS0Ha13Dr7MQdt+a0GyHbIsK2AlzDmdOp5xadzsvCLtv5/ZBiTulD4ed+C3U1dOhSYAJD06S/McQukYShoMDFnlb2mEuZeTXVO344rfMJDgLzsu8Ad6tRdDjPGMYeGmfvtLC4ZqPakMDrBacC+GRdXthTNKmuxdNpz1CfJzyi6Zta3ckGG10TMkfgirndgQ+XQbbEhBnc51tMhjTajU7+UN288SrBSzZCpiHWo/qh1G8="
        ));

        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        // Set AI Goal (Look at players)
        setGoal(new LookAtPlayerGoal(this, 7, getSpawnPosition()));

        // Set Name (Hologram-like Text)
        setName(new TemplateText(
                TextUtils.convertStringToComponent("<yellow>Right Click</yellow>"),
                TextUtils.convertStringToComponent("<light_purple>Cosmetics Dealer</light_purple>"),
                TextUtils.convertStringToComponent("<dark_purple>100 new Cosmetics</dark_purple>")
        ));
    }

    @Override
    protected void personalize(Player player) {
        // Setup an interactive inventory menu
        setActionList(new ActionList(
                new MenuAction(0, 0, true, new CosmeticsShop().constructInventory(player)) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        return actionList.getAction(0);
                    }
                }
        ));
    }
}
