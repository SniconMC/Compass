package rip.snicon.compass.player.cosmetics;

import java.util.Set;
import java.util.UUID;

public interface BaseCosmetic {
    String getName();

    boolean isOwned();

    void setOwned(boolean owned);

    void onEnable(UUID uuid);

    void onDisable(UUID uuid);

    // Get conflicts, which can be specific cosmetics or entire categories
    Set<Object> getConflicts();
}
