package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.actions.MenuAction;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.debug.DebugContainer;
import rip.snicon.compass.utils.TextUtils;

public class DebugJohnson extends TemplateNPC {

    public DebugJohnson() {
        super(EntityType.PLAYER);

        // Set Spawn Position
        setSpawnPosition(new Pos(26.5, 9.0, 20.5, 0.0f, 0.0f));

        // Set Skin Layers
        setSkinLayer(SkinLayer.NO_CAPE);

        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTczNzE0NDA0NjQ2NCwKICAicHJvZmlsZUlkIiA6ICIwMzBlMDA1OWQwY2M0YTZhODY3N2RkZWU3MjEzMjg1MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTbXVnRm9vZGllIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk5NGNmMDZiNTE4MmZhNTdlNDE2ZDFmZTIyOWY5MTE5YjI5N2Y5MDA4OGJhNDVlYzk0NWIzZmI4NWFkOWYzM2QiCiAgICB9CiAgfQp9",
                "mMjcrgCypBmaaAkix9vuAZcgKq7EU+eUaAlPxsSklu/vStL+D+ek1cEcJ4vbu/zaVxTEXPHAikBS5eyb+f3CHrvu2uCwyRh097IAzBjVMPSBAhEuijVzicL+rQK9SXU9kAPCuEzNZSEg8i73xXgzzQQMkw+/bGzoQXaO4yVWMStV7qTxCSTPv6WE0ob2rwy1EKsoJl1fUe/FME9Lijqyegotn89hHaZLn9diR5yzfdjo/fyLO0ipLLAYNIHx7cvCXN6cI87GQ1h/d9lhh586YJoW2fgididPoIbispioY+p6QzvgxnIqPKyIEcy5IbdcheRSCsFsTlAmtCcp4XBKxFbFhbBATP2BPk1tGPEWycYfmO9yc0qtArKDJWrq9SjRUhbIwI4/EC2Lza+QdDckxvV/dbiyF/7heXKwA2VmYMn4XgfCvgR4HtoQSZGXbby/I1LGQl9HYdtiY1uZZzmFu/v56RVknAIkjMc0x8zxIGAdQGL+hKwItsGGlw7qVoAP5Ky5jrjrU8/JrvLRvVdRPQN5H7YrZm8C2m2WKi2W6hxoYhzZuxyZGKVPoC69k4MK1WMzD5Y8RxRrgeCpUVPVeIwHQ3pD3+/d1TiJud64noLYbGgiH9Fnq9p5hMpFfMLFYay97kZlK5cLRSWe3X6iHsqQu8dwWZo8mZkMrNHhRMQ="
        ));

        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        // Set AI Goal (Look at players)
        setGoal(new LookAtPlayerGoal(this, 7, getSpawnPosition()));

        // Set Name (Hologram-like Text)
        setName(new TemplateText(
                TextUtils.convertStringToComponent("<gold>Debug Johnson</gold>")
        ));
    }

    @Override
    protected void personalize(Player player) {
        // Open Debug Inventory on interact
        setActionList(new ActionList(
                new MenuAction(0, 0, true, new DebugContainer().constructInventory(player)) {
                    @Override
                    public AbstractAction determineNextAction(Player player, ActionList actionList) {
                        return actionList.getAction(0);
                    }
                }
        ));
    }
}
