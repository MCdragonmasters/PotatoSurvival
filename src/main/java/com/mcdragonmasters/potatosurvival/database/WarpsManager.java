package com.mcdragonmasters.potatosurvival.database;

import com.google.gson.*;
import com.mcdragonmasters.potatosurvival.PotatoSurvival;
import com.mcdragonmasters.potatosurvival.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

@SuppressWarnings("CallToPrintStackTrace")
public class WarpsManager {
    private static final File warpsFile = new File(PotatoSurvival.getInstance().getDataFolder(), "warps.json");
    private static final Map<String, Map<String, Location>> warpsMap = new HashMap<>();

    static {
        loadWarps();
    }

    public static String[] getPlayerWarps(CommandSender sender) {
        if (sender instanceof Player player) {
            Map<String, Location> playerWarps = warpsMap.get(player.getUniqueId().toString());
            if (playerWarps == null || playerWarps.isEmpty()) {
                return new String[0]; // Return an empty array if no warps exist
            }
            return playerWarps.keySet().toArray(new String[0]); // Convert the warp names to a String array
        }
        return new String[0];
    }


    public static void loadWarps() {
        if (!warpsFile.exists()) {
            return;
        }

        try (Reader reader = new FileReader(warpsFile)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (element == null || !element.isJsonObject()) {
                return;
            }

            JsonObject jsonObject = element.getAsJsonObject();

            for (Map.Entry<String, JsonElement> playerEntry : jsonObject.entrySet()) {
                String playerUUID = playerEntry.getKey();
                JsonObject warpsData = playerEntry.getValue().getAsJsonObject();

                Map<String, Location> playerWarps = new HashMap<>();
                for (Map.Entry<String, JsonElement> warpEntry : warpsData.entrySet()) {
                    String warpName = warpEntry.getKey();
                    JsonObject warpData = warpEntry.getValue().getAsJsonObject();

                    if (warpData.has("world")) {
                        Location location = new Location(
                                Bukkit.getWorld(warpData.get("world").getAsString()),
                                warpData.get("x").getAsDouble(),
                                warpData.get("y").getAsDouble(),
                                warpData.get("z").getAsDouble(),
                                warpData.get("yaw").getAsFloat(),
                                warpData.get("pitch").getAsFloat()
                        );
                        playerWarps.put(warpName, location);
                    }
                }
                warpsMap.put(playerUUID, playerWarps);
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            Logger.getLogger.warn("Could not load warps from the JSON file.");
        }
    }

    public static void saveWarps() {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, Map<String, Location>> playerEntry : warpsMap.entrySet()) {
            String playerUUID = playerEntry.getKey();
            JsonObject playerWarpsJson = new JsonObject();

            for (Map.Entry<String, Location> warpEntry : playerEntry.getValue().entrySet()) {
                String warpName = warpEntry.getKey();
                Location location = warpEntry.getValue();

                JsonObject warpData = new JsonObject();
                warpData.addProperty("world", location.getWorld().getName());
                warpData.addProperty("x", location.getX());
                warpData.addProperty("y", location.getY());
                warpData.addProperty("z", location.getZ());
                warpData.addProperty("yaw", location.getYaw());
                warpData.addProperty("pitch", location.getPitch());

                playerWarpsJson.add(warpName, warpData);
            }
            jsonObject.add(playerUUID, playerWarpsJson);
        }

        try (Writer writer = new FileWriter(warpsFile)) {
            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
            gsonPretty.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getWarp(String playerUUID, String warpName) {
        Map<String, Location> playerWarps = warpsMap.get(playerUUID);
        return playerWarps != null ? playerWarps.get(warpName) : null;
    }

    public static void saveWarp(String playerUUID, String warpName, Location location) {
        warpsMap.computeIfAbsent(playerUUID, k -> new HashMap<>()).put(warpName, location);
        saveWarps();
    }

    public static void removeWarp(String playerUUID, String warpName) {
        Map<String, Location> playerWarps = warpsMap.get(playerUUID);
        if (playerWarps != null) {
            playerWarps.remove(warpName);
            if (playerWarps.isEmpty()) {
                warpsMap.remove(playerUUID);
            }
            saveWarps();
        }
    }
}
