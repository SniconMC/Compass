package rip.snicon.compass.player.handler;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.player.data.MysteryRarities;
import rip.snicon.compass.player.data.bundle.MysteryBundle;
import rip.snicon.compass.player.data.bundle.MysteryBundleTypes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MysteryBundleHandler {

    private final UUID uuid;
    private final Map<String, MysteryBundle> bundles = new ConcurrentHashMap<>();

    public MysteryBundleHandler(@NotNull UUID uuid) {
        this.uuid = uuid;
        initializeBundles();
    }

    private void initializeBundles() {
        for (MysteryBundleTypes bundleType : MysteryBundleTypes.values()) {
            boolean isInfinite = bundleType == MysteryBundleTypes.STARTER_BUNDLE;
            bundles.put(bundleType.name(), MysteryBundle.createBundleFromType(bundleType, 0, isInfinite));
        }
    }

    public void fetchBundlesFromDatabase() {
        MongoDatabaseManager.fetch("bundles", "uuid", uuid.toString()).thenAccept(document -> {
            if (document != null) {
                loadBundles(document);
            } else {
                saveBundlesToDatabase();
            }
        });
    }

    public void saveBundlesToDatabase() {
        List<Document> bundleDocuments = new ArrayList<>();
        for (Map.Entry<String, MysteryBundle> entry : bundles.entrySet()) {
            String internalName = entry.getKey();
            MysteryBundle mysteryBundle = entry.getValue();

            bundleDocuments.add(new Document("internal_name", internalName)
                    .append("display_name", mysteryBundle.getName())
                    .append("rarity", mysteryBundle.getRarity().name())
                    .append("materials", mysteryBundle.getMaterialsNames())
                    .append("entities", mysteryBundle.getEntitiesNames())
                    .append("display_item", mysteryBundle.getDisplayItem())
                    .append("amount", mysteryBundle.getAmount())
                    .append("is_infinite", mysteryBundle.isInfinite()));
        }

        Document data = new Document("uuid", uuid.toString())
                .append("bundles", bundleDocuments);

        MongoDatabaseManager.save("bundles", "uuid", data).exceptionally(throwable -> {
            System.err.println("Failed to save bundles: " + throwable.getMessage());
            return null;
        });
    }

    private void loadBundles(Document document) {
        List<Document> bundleDocuments = document.getList("bundles", Document.class);
        if (bundleDocuments != null) {
            for (Document bundleDoc : bundleDocuments) {
                String internalName = bundleDoc.getString("internal_name");
                String displayName = bundleDoc.getString("display_name");
                MysteryRarities rarity = MysteryRarities.valueOf(bundleDoc.getString("rarity"));
                List<String> materials = bundleDoc.getList("materials", String.class);
                List<String> entities = bundleDoc.getList("entities", String.class);
                String displayItem = bundleDoc.getString("display_item");
                int amount = bundleDoc.getInteger("amount", 0);
                boolean isInfinite = bundleDoc.getBoolean("is_infinite", false);

                bundles.put(internalName, new MysteryBundle(displayName, rarity,
                        MysteryBundle.convertBlockListToMaterials(materials),
                        MysteryBundle.convertEntityListToEntityTypes(entities),
                        displayItem, amount, isInfinite));
            }
        }
    }

    public void addBundle(@NotNull MysteryBundleTypes mysteryBundleType, int amount, boolean isInfinite) {
        bundles.put(mysteryBundleType.name(), MysteryBundle.createBundleFromType(mysteryBundleType, amount, isInfinite));
        saveBundlesToDatabase();
    }

    public void removeBundle(@NotNull String bundleName) {
        bundles.remove(bundleName);
        saveBundlesToDatabase();
    }

    public boolean useBundle(@NotNull String bundleName) {
        MysteryBundle bundle = bundles.get(bundleName);
        if (bundle != null) {
            saveBundlesToDatabase();
            return true;
        }
        return false;
    }

    public void addBundleAmount(@NotNull String bundleName, int amount) {
        MysteryBundle bundle = bundles.get(bundleName);
        if (bundle != null) {
            bundle.addAmount(amount);
            saveBundlesToDatabase();
        }
    }

    public MysteryBundle getBundle(@NotNull String bundleName) {
        return bundles.get(bundleName);
    }

    public Collection<MysteryBundle> getAllBundles() {
        return bundles.values();
    }

    public boolean hasBundle(@NotNull String bundleName) {
        return bundles.containsKey(bundleName);
    }
}
