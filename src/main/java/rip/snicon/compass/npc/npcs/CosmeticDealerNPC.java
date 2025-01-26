package rip.snicon.compass.npc.npcs;


import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.metadata.PlayerMeta;
import nub.wi1helm.template.TemplateInventory;
import nub.wi1helm.template.items.BackgroundItem;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.bundels.BundleViewerContainer;
import rip.snicon.compass.inventory.cosmetics.CosmeticsShop;
import rip.snicon.compass.npc.MysteryHologram;
import rip.snicon.compass.npc.MysteryNPC;
import rip.snicon.compass.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

public class CosmeticDealerNPC extends MysteryNPC {

    public CosmeticDealerNPC() {
        super(
                EntityType.PLAYER, // Entity type from JSON
                3,
                32,
                new Pos(29.5, 10, 0.5, 0, 0)
        );
    }

    @Override
    public void initialize() {

        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new LookAtPlayerGoal(this, 7, getDefaultPos())) // Look at players within 5 blocks
                        .build()
        );

        // Set skin attributes from JSON
        setPlayerSkin(new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTY4NzQ0NzIwNDA2NSwKICAicHJvZmlsZUlkIiA6ICIwZWQ2MDFlMDhjZTM0YjRkYWUxZmI4MDljZmEwNTM5NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJOZWVkTW9yZUFjY291bnRzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI4ZmYyZmVlN2ZmOWVjY2MxNGU3N2NhNWZjZWNmNGFhYmVmY2Y1M2NlZmYyZDU4ZTMxOWIzODgwZjY5NDUzYTIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "a7mXkCW/rSNrY2gOQjKBg63BloHiszji3a/FTqHAPwUMQO7lNZNETuRdWY/hGshA+mTNKaYQWywAOr6evVAyMB9GZyNANxICgb876u4t6razIqNZHvRV27ScA+L0hU+hd2ZKI1EWg/8wGr998coVLdLzdwyB7qDlWqCdisEfPcp7w+TSb/pfvVSbnsJRbhhVNiQdZdJX/1UVG7CyDPLKzTWoqkZQehA00W2zJR1SSfYRjjSoCsaVlC6j8Y3JV5mZRI5ntYNqO2rGm3768x8L05YSuDbBxFei9/sZ2itnwITsh9cIv5iEHg2tBHY68klZRYgaSpSEWJkobR/5urzqTLC3m1kEc2LzxcxfidOSiWrIE7rizLQTLAGhG5ZIjxQHk62MRddgT2ZO4N+VhRORv0OqWg8yUktp6P6u1xPaZ8URpvb9xr8DPr+c9mfH1MSBS0Ha13Dr7MQdt+a0GyHbIsK2AlzDmdOp5xadzsvCLtv5/ZBiTulD4ed+C3U1dOhSYAJD06S/McQukYShoMDFnlb2mEuZeTXVO344rfMJDgLzsu8Ad6tRdDjPGMYeGmfvtLC4ZqPakMDrBacC+GRdXthTNKmuxdNpz1CfJzyi6Zta3ckGG10TMkfgirndgQ+XQbbEhBnc51tMhjTajU7+UN288SrBSzZCpiHWo/qh1G8="));

        // Set player meta
        editEntityMeta(PlayerMeta.class, meta -> {
            meta.setCapeEnabled(false);
            meta.setJacketEnabled(true);
            meta.setLeftSleeveEnabled(true);
            meta.setRightSleeveEnabled(true);
            meta.setLeftLegEnabled(true);
            meta.setRightLegEnabled(true);
            meta.setHatEnabled(true);
        });
    }

    @Override
    public void onSpawnHologram(MysteryHologram hologram, MysteryPlayer player) {
        switch (hologram.getRow()) {
            case 0:
                hologram.setText(TextUtils.convertStringToComponent("<yellow>Right Click</yellow>"));
                break;
            case 1:
                hologram.setText(TextUtils.convertStringToComponent("<light_purple>Cosmetics Dealer</light_purple>"));
                break;
            case 2:
                hologram.setText(TextUtils.convertStringToComponent("<dark_purple>100 new Cosmetics</dark_purple>"));
                break;
        }
    }

    @Override
    public void onDespawnHologram(MysteryHologram hologram, MysteryPlayer player) {

    }

    @Override
    public void onSpawn(MysteryPlayer player) {
        setInstance(MysteryInstanceType.HUB.getInstance(), getDefaultPos());
    }

    @Override
    public void onDespawn(MysteryPlayer player) {
        // Handle despawning logic if necessary
    }

    @Override
    public void onInteract(MysteryPlayer player) {
        TemplateInventory inventory = new CosmeticsShop();
        inventory.fillInventory(new BackgroundItem());
        player.openInventory(inventory.constructInventory(player));
    }
}

