package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.actions.MenuAction;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.store.StoreMenu;
import rip.snicon.compass.utils.TextUtils;

public class BankerJohnson extends TemplateNPC {
    public BankerJohnson() {
        super(EntityType.PLAYER);
        setSpawnPosition(new Pos(17.5,9,25.5, -90, 0));
        setSpawnStrategy(SpawnStrategy.STANDING);
        setSkin(new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTczOTcxNTgzNDY1MCwKICAicHJvZmlsZUlkIiA6ICI0YWY1YmQ3NTdmZDE0MWEwOTczYmUxNTFkZWRjNmM5ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJjcmFzaGludG95b3VybW9tIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk5OGZhYmMyYzcxZmEwMDQ5OGRhOGVjZTVlZDNlMjdhYjJiYzZkNmZmODc4MjdmMDgwNjdkOTJiNzM5MzQ0YTIiCiAgICB9CiAgfQp9","ozP7RjQgv0DoS81l8GkJbD+c2YeoFST7wNfJJkveHdc8T++MZvX7GnFUE9fl0SDtUoG+W80DmzCNIIkt55JxxfyXVd+DaaaFHKttBr8NiCqkg5tgeFs/B+fWvgBstvR3NB9/a4luSZFP9J646WMosJR/9WjrVS6CucBY/tQa5mlpKhvRytBmwioFD/KdiOa+G3v5Q3Y6AWcOqac5XmBNeDbgQ9szg3ttepBq9pHyD1d+wJYgUOVaujgYkubYbvOtxDm8O16ESpHJyRUJB2RDzkjfx/UFJwP+FKaUUp4ZO1lJ8IAz6mZacbI8pJIxw9IYWhyioGeORIwwy9RHt3gDfV83jeV5lER/ul7bi8lh++pIFdcfj4wiQuY6kdFrRo4/8L4sbA6gxXf5TacyssRalz5UJ7t2uBxDrOG/aBMNQmZFJgim949HmKhU7lSCSAju4EeuNebikpscRJGNCcydz89Uj57UxJUPK0n7phUU3g4YrpL/ZvzLR0NoTDSC1RPoqx+etSQXn07D/HPfK5W1kqV1SIw166K7lyqfch/gqLKdigC4hyWjx7moQvRzBxCG5/KddwmV7+MdEFAgP4gaWp4AjPNxD5Lz1OItGC1fTtt0foeCoe0OKpokqAUmSEr8tYldQb9h7bYvy4+SvB0TaQcoKw0DBtOYVigRa4tel3w="));
        setName(new TemplateText(TextUtils.convertStringToComponent("<green>Shop</green>")));

        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        setSkinLayer(SkinLayer.NO_CAPE);

        setGoal(new LookAtPlayerGoal(this,7, getSpawnPosition()));



    }

    @Override
    protected void personalize(Player player) {
        setActionList(new ActionList(new MenuAction(0,0,true,new StoreMenu().constructInventory(player)) {
            @Override
            public AbstractAction determineNextAction(Player player, ActionList actionList) {
                return actionList.getAction(0);
            }
        }));
    }
}
