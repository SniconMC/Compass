package rip.snicon.compass.player.handler;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.player.cosmetics.BaseCosmetic;
import rip.snicon.compass.player.cosmetics.PlayerHelmet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MysteryCosmeticHandler {

    private final UUID uuid;
    private final Map<BaseCosmetic, Boolean> cosmetics = new HashMap<>();

    public MysteryCosmeticHandler(@NotNull UUID uuid) {
        this.uuid = uuid;

        BaseCosmetic defaultHelmet = PlayerHelmet.IRON_HELMET;
        cosmetics.put(defaultHelmet,false);
    }

    public boolean hasCosmetic(BaseCosmetic cosmetic) {
        return cosmetics.containsKey(cosmetic);
    }

    public boolean isEnabled(BaseCosmetic cosmetic) {
        return cosmetics.getOrDefault(cosmetic, false);
    }

    public void addCosmetic(BaseCosmetic cosmetic) {
        cosmetics.putIfAbsent(cosmetic, false);
        saveCosmeticsToDatabase();
    }

    public void enableCosmetic(BaseCosmetic cosmetic) {
        if (!hasCosmetic(cosmetic)) {
            throw new IllegalStateException("You do not own this cosmetic.");
        }

        for (Object conflict : cosmetic.getConflicts()) {
            if (conflict instanceof Class<?> conflictCategory) {
                cosmetics.keySet().stream()
                        .filter(c -> c.getClass() == conflictCategory && c != cosmetic)
                        .forEach(c -> cosmetics.put(c, false));
            } else if (conflict instanceof BaseCosmetic conflictCosmetic) {
                cosmetics.put(conflictCosmetic, false);
            }
        }

        cosmetics.put(cosmetic, true);
        cosmetic.onEnable(uuid);
        saveCosmeticsToDatabase();
    }

    public void disableCosmetic(BaseCosmetic cosmetic) {
        if (!hasCosmetic(cosmetic)) {
            throw new IllegalStateException("You do not own this cosmetic.");
        }

        cosmetics.put(cosmetic, false);
        cosmetic.onDisable(uuid);
        saveCosmeticsToDatabase();
    }

    public void saveCosmeticsToDatabase() {
        Document data = new Document("uuid", uuid.toString());

        for (Map.Entry<BaseCosmetic, Boolean> entry : cosmetics.entrySet()) {
            String category = entry.getKey().getClass().getSimpleName();
            Document categoryData = data.get(category, Document.class);
            if (categoryData == null) {
                categoryData = new Document();
                data.append(category, categoryData);
            }
            categoryData.append(entry.getKey().getName(), entry.getValue());
        }

        MongoDatabaseManager.save("cosmetics", "uuid", data).exceptionally(throwable -> {
            System.err.println("Failed to save cosmetics: " + throwable.getMessage());
            return null;
        });
    }

    public void fetchCosmeticsFromDatabase() {
        MongoDatabaseManager.fetch("cosmetics", "uuid", uuid.toString()).thenAccept(document -> {
            if (document != null) {
                loadCosmetics(document);
            }
        });
    }

    private void loadCosmetics(Document document) {
        cosmetics.clear();

        for (String category : document.keySet()) {
            if ("uuid".equals(category)) continue;

            try {
                Class<? extends BaseCosmetic> cosmeticClass = (Class<? extends BaseCosmetic>) Class.forName("rip.snicon.compass.player.cosmetics." + category);
                Document categoryData = document.get(category, Document.class);

                if (categoryData != null) {
                    for (String cosmeticName : categoryData.keySet()) {
                        BaseCosmetic cosmetic = findCosmeticByName(cosmeticClass, cosmeticName);
                        if (cosmetic != null) {
                            cosmetics.put(cosmetic, categoryData.getBoolean(cosmeticName));
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Unknown cosmetic category: " + category);
            }
        }
    }

    private BaseCosmetic findCosmeticByName(Class<? extends BaseCosmetic> category, String name) {
        for (BaseCosmetic cosmetic : category.getEnumConstants()) {
            if (cosmetic.getName().equals(name)) {
                return cosmetic;
            }
        }
        return null;
    }
    public void applyEnabledCosmetics() {
        cosmetics.forEach((cosmetic, isEnabled) -> {
            if (isEnabled) {
                cosmetic.onEnable(uuid);
            }
        });
    }
}
