package com.mcdragonmasters.potatosurvival.jsonDatabase;

import com.google.gson.*;
import com.mcdragonmasters.potatosurvival.PotatoSurvival;
import com.mcdragonmasters.potatosurvival.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("CallToPrintStackTrace")
public class HomesManager {

    // Static fields to store homes data
    private static final File homesFile = new File(PotatoSurvival.getInstance().getDataFolder(), "homes.json");
    private static final Map<String, Location> homesMap = new HashMap<>();

    // Static initializer block to load homes from the file when the plugin starts
    static {
        loadHomes();
    }

    // Load homes from the JSON file
    public static void loadHomes() {
        if (!homesFile.exists()) {
            return; // No file, so no homes to load
        }

        try (Reader reader = new FileReader(homesFile)) {
            JsonElement element = JsonParser.parseReader(reader);

            // Check if the element is not a valid JSON object
            if (element == null || !element.isJsonObject()) {
                return; // If the file doesn't contain valid JSON, return
            }

            JsonObject jsonObject = element.getAsJsonObject();

            // Parse the homes data into the map
            for (Map.Entry<String, ?> entry : jsonObject.entrySet()) {
                String playerUUID = entry.getKey();
                JsonObject homeData = (JsonObject) entry.getValue();

                // Ensure home data is valid before proceeding
                if (homeData != null && homeData.has("world")) {
                    Location location = new Location(
                            Bukkit.getWorld(homeData.get("world").getAsString()),
                            homeData.get("x").getAsDouble(),
                            homeData.get("y").getAsDouble(),
                            homeData.get("z").getAsDouble(),
                            homeData.get("yaw").getAsFloat(),
                            homeData.get("pitch").getAsFloat()
                    );
                    homesMap.put(playerUUID, location);
                }
            }
        } catch (IOException | com.google.gson.JsonSyntaxException e) {
            // Log and handle any issues during parsing
            e.printStackTrace();
            Logger.getLogger.warn("Could not load homes from the JSON file.");
        }
    }


    // Save homes to the JSON file
    public static void saveHomes() {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, Location> entry : homesMap.entrySet()) {
            String playerUUID = entry.getKey();
            Location location = entry.getValue();
            JsonObject homeData = new JsonObject();
            homeData.addProperty("world", location.getWorld().getName());
            homeData.addProperty("x", location.getX());
            homeData.addProperty("y", location.getY());
            homeData.addProperty("z", location.getZ());
            homeData.addProperty("yaw", location.getYaw());
            homeData.addProperty("pitch", location.getPitch());

            jsonObject.add(playerUUID, homeData);
        }

        // Use Gson with pretty printing
        try (Writer writer = new FileWriter(homesFile)) {
            // Pretty print JSON
            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
            gsonPretty.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Static method to get home for a specific player
    public static Location getHome(String playerUUID) {
        return homesMap.get(playerUUID);
    }

    // Static method to remove home for a specific player
    public static void removeOldHome(String playerUUID) {
        homesMap.remove(playerUUID);
        saveHomes(); // Save after removal
    }

    // Static method to save a player's home location
    public static void saveHome(String playerUUID, Location location) {
        homesMap.put(playerUUID, location);
        saveHomes(); // Save after adding/updating
    }
}