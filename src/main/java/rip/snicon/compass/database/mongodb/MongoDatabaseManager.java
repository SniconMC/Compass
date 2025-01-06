package rip.snicon.compass.database.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MongoDatabaseManager {

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static boolean isConnected = false;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Connect to the MongoDB database.
     *
     * @param uri           MongoDB connection URI.
     * @param databaseName  Name of the database to connect to.
     */
    public static synchronized void connect(String uri, String databaseName) {
        try {
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(databaseName);
            isConnected = true;
            System.out.println("Connected to MongoDB!");
        } catch (Exception e) {
            isConnected = false;
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Disconnect from MongoDB.
     */
    public static synchronized void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            isConnected = false;
            System.out.println("Disconnected from MongoDB.");
        }
        executorService.shutdown();
    }

    /**
     * Check if MongoDB is connected.
     *
     * @return true if connected, false otherwise.
     */
    public static boolean isConnected() {
        return isConnected;
    }

    /**
     * Fetch a document from a collection asynchronously.
     *
     * @param collectionName Name of the collection.
     * @param keyField       Field to match.
     * @param key            Value to search for.
     * @return CompletableFuture containing the found document or null.
     */
    public static CompletableFuture<Document> fetch(String collectionName, String keyField, String key) {
        if (!isConnected) {
            return CompletableFuture.failedFuture(new IllegalStateException("MongoDB is offline!"));
        }

        return CompletableFuture.supplyAsync(() -> {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            return collection.find(Filters.eq(keyField, key)).first();
        }, executorService);
    }

    /**
     * Save a document to a collection asynchronously.
     *
     * @param collectionName Name of the collection.
     * @param keyField       Field to match for upsert.
     * @param data           Document to save.
     * @return CompletableFuture indicating completion.
     */
    public static CompletableFuture<Void> save(String collectionName, String keyField, Document data) {
        if (!isConnected) {
            return CompletableFuture.failedFuture(new IllegalStateException("MongoDB is offline!"));
        }

        String key = data.getString(keyField);
        if (key == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Key field is missing in the document!"));
        }

        return CompletableFuture.runAsync(() -> {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.replaceOne(Filters.eq(keyField, key), data, new ReplaceOptions().upsert(true));
        }, executorService);
    }

    /**
     * Create a collection if it does not exist.
     *
     * @param collectionName Name of the collection.
     */
    public static synchronized void createCollection(String collectionName) {
        if (!isConnected) {
            throw new IllegalStateException("MongoDB is offline!");
        }

        if (!database.listCollectionNames().into(new java.util.ArrayList<>()).contains(collectionName)) {
            database.createCollection(collectionName);
            System.out.println("Created collection: " + collectionName);
        } else {
            System.out.println("Collection already exists: " + collectionName);
        }
    }

    /**
     * Delete a document from a collection asynchronously.
     *
     * @param collectionName Name of the collection.
     * @param keyField       Field to match.
     * @param key            Value to match.
     * @return CompletableFuture indicating completion.
     */
    public static CompletableFuture<Void> delete(String collectionName, String keyField, String key) {
        if (!isConnected) {
            return CompletableFuture.failedFuture(new IllegalStateException("MongoDB is offline!"));
        }

        return CompletableFuture.runAsync(() -> {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.deleteOne(Filters.eq(keyField, key));
        }, executorService);
    }
}
