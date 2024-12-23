package rip.snicon.compass.database.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

import java.util.concurrent.CompletableFuture;

public class MongoDatabaseManager {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static boolean isConnected = false;

    public static void connect(String uri, String databaseName) {
        try {
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(databaseName);
            isConnected = true;
            System.out.println("Connected to MongoDB!");
        } catch (Exception e) {
            isConnected = false;
            System.err.println("Failed to connect to MongoDB. Please check your connection settings.");
        }
    }

    public static void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            isConnected = false;
            System.out.println("Disconnected from MongoDB.");
        }
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static CompletableFuture<Document> fetch(String collectionName, String keyField, String key) {
        CompletableFuture<Document> future = new CompletableFuture<>();
        if (!isConnected) {
            future.completeExceptionally(new IllegalStateException("MongoDB is offline!"));
            return future;
        }

        MongoCollection<Document> collection = database.getCollection(collectionName);
        new Thread(() -> {
            try {
                Document document = collection.find(Filters.eq(keyField, key)).first();
                future.complete(document);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }).start();

        return future;
    }

    public static CompletableFuture<Void> save(String collectionName, String keyField, Document data) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (!isConnected) {
            future.completeExceptionally(new IllegalStateException("MongoDB is offline!"));
            return future;
        }

        MongoCollection<Document> collection = database.getCollection(collectionName);
        String key = data.getString(keyField);
        if (key == null) {
            future.completeExceptionally(new IllegalArgumentException("Key field is missing in the document!"));
            return future;
        }

        new Thread(() -> {
            try {
                collection.replaceOne(Filters.eq(keyField, key), data, new ReplaceOptions().upsert(true));
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }).start();

        return future;
    }
}
