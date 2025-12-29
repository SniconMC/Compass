
package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.actions.AbstractAction;
import nub.wi1helm.template.npc.actions.ActionList;

import nub.wi1helm.template.npc.actions.DynamicMenuAction;
import nub.wi1helm.template.npc.player.SkinLayer;
import nub.wi1helm.template.npc.player.TemplatePlayerNPC;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.debug.DebugContainer;
import rip.snicon.compass.utils.TextUtils;

public class DebugJohnson extends TemplatePlayerNPC implements Interactable, Nameable{

    private ActionList actionList = ActionList.empty();

    public DebugJohnson() {
        super(MysteryInstanceType.HUB.getInstance(), new Pos(33.2, 8.5, 25.0, 90.0f, 0.0f));

        setSkinLayer(SkinLayer.NO_CAPE);
        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTczNzE0NDA0NjQ2NCwKICAicHJvZmlsZUlkIiA6ICIwMzBlMDA1OWQwY2M0YTZhODY3N2RkZWU3MjEzMjg1MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTbXVnRm9vZGllIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk5NGNmMDZiNTE4MmZhNTdlNDE2ZDFmZTIyOWY5MTE5YjI5N2Y5MDA4OGJhNDVlYzk0NWIzZmI4NWFkOWYzM2QiCiAgICB9CiAgfQp9",
                "mMjcrgCypBmaaAkix9vuAZcgKq7EU+eUaAlPxsSklu/vStL+D+ek1cEcJ4vbu/zaVxTEXPHAikBS5eyb+f3CHrvu2uCwyRh097IAzBjVMPSBAhEuijVzicL+rQK9SXU9kAPCuEzNZSEg8i73xXgzzQQMkw+/bGzoQXaO4yVWMStV7qTxCSTPv6WE0ob2rwy1EKsoJl1fUe/FME9Lijqyegotn89hHaZLn9diR5yzfdjo/fyLO0ipLLAYNIHx7cvCXN6cI87GQ1h/d9lhh586YJoW2fgididPoIbispioY+p6QzvgxnIqPKyIEcy5IbdcheRSCsFsTlAmtCcp4XBKxFbFhbBATP2BPk1tGPEWycYfmO9yc0qtArKDJWrq9SjRUhbIwI4/EC2Lza+QdDckxvV/dbiyF/7heXKwA2VmYMn4XgfCvgR4HtoQSZGXbby/I1LGQl9HYdtiY1uZZzmFu/v56RVknAIkjMc0x8zxIGAdQGL+hKwItsGGlw7qVoAP5Ky5jrjrU8/JrvLRvVdRPQN5H7YrZm8C2m2WKi2W6hxoYhzZuxyZGKVPoC69k4MK1WMzD5Y8RxRrgeCpUVPVeIwHQ3pD3+/d1TiJud64noLYbGgiH9Fnq9p5hMpFfMLFYay97kZlK5cLRSWe3X6iHsqQu8dwWZo8mZkMrNHhRMQ="
        ));

        // Set Name (Hologram-like Text)
        setName(MysteryInstanceType.HUB.getInstance(),
                TextUtils.convertStringToComponent("<gold>Debug Johnson</gold>")
        );
    }

    @Override
    public void personalize(Player player) {
        // Open Debug Inventory on interact
        setActionList(new ActionList(
                new DynamicMenuAction(0, 0, true, new DebugContainer()) {
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

