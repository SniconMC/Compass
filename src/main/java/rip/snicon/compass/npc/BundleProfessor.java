package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.SkinLayer;
import nub.wi1helm.template.npc.SpawnStrategy;
import nub.wi1helm.template.npc.TemplateNPC;
import nub.wi1helm.template.npc.TemplateText;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.utils.TextUtils;

public class BundleProfessor extends TemplateNPC {
    public BundleProfessor() {
        super(EntityType.PLAYER);
        setSpawnPosition(new Pos(11.5,6,107.5,160, 0));

        // Set Skin Layers
        setSkinLayer(SkinLayer.NO_CAPE);
        setSpawnStrategy(SpawnStrategy.STANDING);
        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTY1OTIyMDk4OTg5NiwKICAicHJvZmlsZUlkIiA6ICIwNTkyNTIxZGNjZWE0NzRkYjE0M2NmMDg2MDA1Y2FkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJwdXIyNCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NTgwODczZWNkNDM1NGJlZjljOGVjZWQ2OWUwMzJiMTFkNzUyYzZiNWMzYTZmMzA2M2ZhNzdlNjczNGExNDUyIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                "poGP286S2bG43JkrZ4izY6UkWo7fyONqy4uAgPCNVVvxcnDejTRIwxAljujfTx/D8tQEz3x0/O3NPdqvvty4cRWYUTYjvB/noFnzdmEhQSgocJPRC+ei6FJlQ73xqCMA76fYuKR5cmX7TjACD5tJ7Yxi7Ib8qsvsBvAcXT5ZJOd4fAxJGKTX3mq/Tl0ES4uVaLgL57nhau78eFS6s8xGPB5l2AF16Qcfe7onROCJHr8dQQTUSpPmC7fN/Yn2Ic/P1W82g5MvqPYXel93WP4fTBbkfA9UCXpgs13dGnBy4fmkIahkaOPj1AKMmvvKW7HHPegNbR6J7mVtTRi4eHQUw1u5aJfA1+EOOotnj3KYKraxxuN8goadMVpKCsM+OLRtQzZcdQNO0MmSP8onrBdRD60/afa5MiMWjO6sYBnFgIvyiv7VP8I+Q9Ccza05J0G6nkhCbz931cbRX2ZeJngAqKMEFyPs1Vb2AULBtFVHDLiISOdsVaLKbS5q9fR3i3JP6LE5tyGpIh8t/xGV3f8z7QZBm3/halsqlS2r8tBHKcUVymeNECwQtCvyILZyAlj01xcavmka2jfnEvIC7xV5qffSktHPqnwNrlHfT9CihAQBKHgLR/IrOh7yQU/1B+QnHSoCnTSa++YAwvCZhIpBbwe97fjEJ1UC53kefRDX9sk="));

        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        // Set AI Goal (Look at players)
        setGoal(new LookAtPlayerGoal(this, 7, getSpawnPosition()));

        // Set Name (Hologram-like Text)
        setName(new TemplateText(
                TextUtils.convertStringToComponent("<yellow>Right Click<yellow>"),
                TextUtils.convertStringToComponent("<gold>Bundle Professor</gold>")
        ));
    }

    @Override
    protected void personalize(Player player) {

    }
}
