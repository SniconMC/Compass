package rip.snicon.compass.player.data.cosmetics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public enum PlayerPerk implements BaseCosmetic {
    FLY("fly"),
    JUMPBOOST("jumpboost");

    private final String name;
    private final Set<Object> conflicts = new HashSet<>();

    PlayerPerk(String name) {
        this.name = name;
    }

    static {
        FLY.conflicts.add(JUMPBOOST);
        JUMPBOOST.conflicts.add(PlayerPerk.class); // Conflicts with the entire PlayerPerk category
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isOwned() {
        throw new UnsupportedOperationException("Use the handler to check ownership.");
    }

    @Override
    public void setOwned(boolean owned) {
        throw new UnsupportedOperationException("Use the handler to manage ownership.");
    }

    @Override
    public void onEnable(UUID uuid) {
    }

    @Override
    public void onDisable(UUID uuid) {

    }

    @Override
    public Set<Object> getConflicts() {
        return Collections.unmodifiableSet(conflicts);
    }
}
