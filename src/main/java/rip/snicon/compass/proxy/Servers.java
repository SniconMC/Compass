package rip.snicon.compass.proxy;

import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class Servers {

    private static final String GATE_API_HOST = System.getenv("GATE_API_HOST") != null
            ? System.getenv("GATE_API_HOST")
            : "gate-proxy-service";
    private static final String GATE_API_PORT = System.getenv("GATE_API_PORT") != null
            ? System.getenv("GATE_API_PORT")
            : "8080";
    private static final String AUTH_TOKEN = System.getenv("GATE_API_AUTH_TOKEN");

    public static void register() {
        String serverName = System.getenv("SERVER_NAME");
        String serverAddress = System.getenv("SERVER_ADDRESS");

        if (serverName == null || serverAddress == null) {
            System.err.println("Error: SERVER_NAME or SERVER_ADDRESS is not set!");
            return;
        }

        if (AUTH_TOKEN == null) {
            System.err.println("Error: GATE_API_AUTH_TOKEN is not set!");
            return;
        }

        // Retry loop for DNS resolution
        int maxRetries = 5;
        int retryDelay = 10; // seconds

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                InetAddress.getByName(serverAddress.split(":")[0]);
                System.out.println("DNS resolved successfully: " + serverAddress);
                break;
            } catch (UnknownHostException e) {
                System.err.printf("DNS resolution failed for %s, attempt %d of %d%n", serverAddress, attempt, maxRetries);
                if (attempt < maxRetries) {
                    try {
                        TimeUnit.SECONDS.sleep(retryDelay);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.err.println("Max retries reached. Could not resolve DNS.");
                    return;
                }
            }
        }

        // Prepare the payload
        JsonObject payload = new JsonObject();
        payload.addProperty("name", serverName);
        payload.addProperty("address", serverAddress);
        payload.addProperty("fallback", true);

        String url = "http://" + GATE_API_HOST + ":" + GATE_API_PORT + "/addserver";

        try {
            sendPostRequest(url, payload.toString());
            System.out.printf("Successfully registered server %s.%n", serverName);
        } catch (IOException e) {
            System.err.printf("Failed to register server %s: %s%n", serverName, e.getMessage());
        }
    }

    public static void unregister() {
        String serverName = System.getenv("SERVER_NAME");

        if (serverName == null) {
            System.err.println("Error: SERVER_NAME is not set!");
            return;
        }

        if (AUTH_TOKEN == null) {
            System.err.println("Error: GATE_API_AUTH_TOKEN is not set!");
            return;
        }

        // Prepare the payload
        JsonObject payload = new JsonObject();
        payload.addProperty("name", serverName);

        String url = "http://" + GATE_API_HOST + ":" + GATE_API_PORT + "/removeserver";

        try {
            sendPostRequest(url, payload.toString());
            System.out.printf("Successfully deregistered server %s.%n", serverName);
        } catch (IOException e) {
            System.err.printf("Failed to deregister server %s: %s%n", serverName, e.getMessage());
        }
    }

    private static void sendPostRequest(String urlString, String jsonPayload) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Configure the connection
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", AUTH_TOKEN);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Send the JSON payload
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Read the response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Request successful.");
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                throw new IOException("Request failed: " + response.toString());
            }
        }
    }
}
