package rip.snicon.compass.player.handler;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.player.data.MysteryToggles;

import java.util.*;

public class MysteryToggleHandler {

    private final UUID uuid;
    private final Set<MysteryToggles> toggles = new HashSet<>();

    public MysteryToggleHandler(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public void fetchTogglesFromDatabase() {
        MongoDatabaseManager.fetch("toggles", "uuid", uuid.toString()).thenAccept(document -> {
            if (document != null) {
                loadToggles(document);
            } else {
                saveTogglesToDatabase();
            }
        });
    }

    public void saveTogglesToDatabase() {
        Document data = new Document("uuid", uuid.toString())
                .append("toggles", toggles.stream().map(Enum::name).toList());

        MongoDatabaseManager.save("toggles", "uuid", data).exceptionally(throwable -> {
            System.err.println("Failed to save toggles: " + throwable.getMessage());
            return null;
        });
    }

    private void loadToggles(Document document) {
        List<String> toggleNames = document.getList("toggles", String.class);
        if (toggleNames != null) {
            for (String toggleName : toggleNames) {
                try {
                    toggles.add(MysteryToggles.valueOf(toggleName));
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown toggle found in database: " + toggleName);
                }
            }
        }
    }

    public void addToggle(@NotNull MysteryToggles toggle) {
        toggles.add(toggle);
        saveTogglesToDatabase();
    }

    public void removeToggle(@NotNull MysteryToggles toggle) {
        toggles.remove(toggle);
        saveTogglesToDatabase();
    }

    public boolean hasToggle(@NotNull MysteryToggles toggle) {
        return toggles.contains(toggle);
    }
}
