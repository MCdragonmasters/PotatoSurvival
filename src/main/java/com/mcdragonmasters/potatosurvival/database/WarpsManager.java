package com.mcdragonmasters.potatosurvival.database;

import com.google.gson.*;
import com.mcdragonmasters.potatosurvival.PotatoSurvival;
import com.mcdragonmasters.potatosurvival.utils.Logger;
import com.mcdragonmasters.potatosurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

import static com.mcdragonmasters.potatosurvival.PotatoSurvival.prefix;
import static com.mcdragonmasters.potatosurvival.PotatoSurvival.prefixMini;

@SuppressWarnings({"CallToPrintStackTrace", "deprecation"})
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

    private static final Map<String, Map<String, WarpShareRequest>> pendingRequests = new HashMap<>();


    public static void shareWarp(Player sender, Player receiver, String warpName) {
        String senderUUID = sender.getUniqueId().toString();
        String receiverUUID = receiver.getUniqueId().toString();
        String requestKey = senderUUID + ":" + warpName;

        Location warpLocation = WarpsManager.getWarp(senderUUID, warpName);

        // Add request to the nested map
        pendingRequests
                .computeIfAbsent(receiverUUID, k -> new HashMap<>())
                .put(requestKey, new WarpShareRequest(sender, warpName, warpLocation));

        receiver.sendMessage(prefix + " §e '" + sender.getName() + "'§7 wants to share a warp with you: §a" + warpName);

        String acceptCommand = "/sharewarp " + warpName + " " + sender.getName() + " " + "accept";
        String declineCommand = "/sharewarp " + warpName + " " + sender.getName() + " " + "decline";
        Component msg = Utils.miniMessage().deserialize(prefixMini + " " +
                "<green><bold><click:run_command:" + acceptCommand + ">[Accept]<reset> " +
                "<red><bold><click:run_command:" + declineCommand + ">[Decline]");
        receiver.sendMessage(msg);
        sender.sendMessage("§6Request sent to §a" + receiver.getName());
    }


    public static void acceptWarp(Player receiver, Player sender1, String warpName) {
        String receiverUUID = receiver.getUniqueId().toString();
        String senderName = sender1.getName();
        Player sender = Bukkit.getPlayer(senderName);

        if (sender == null) {
            receiver.sendMessage(prefix + "§c Error: " + ChatColor.YELLOW + "'" + senderName + "'" +
                    ChatColor.RED + " is not online.");
            return;
        }

        String senderUUID = sender.getUniqueId().toString();
        String requestKey = senderUUID + ":" + warpName;

        Map<String, WarpShareRequest> requests = pendingRequests.get(receiverUUID);
        if (requests == null || !requests.containsKey(requestKey)) {
            receiver.sendMessage("§cNo warp share request found for '" + warpName + "' from " + senderName + ".");
            return;
        }

        WarpShareRequest request = requests.remove(requestKey);
        if (requests.isEmpty()) {
            pendingRequests.remove(receiverUUID); // Clean up if no more requests
        }

        WarpsManager.saveWarp(receiverUUID, request.getWarpName(), request.getWarpLocation());
        receiver.sendMessage("§aWarp '" + request.getWarpName() + "' from " + senderName + " has been added to your list.");
        sender.sendMessage("§a" + receiver.getName() + " accepted your warp request.");
    }


    public static void declineWarp(Player receiver, Player sender1, String warpName) {
        String receiverUUID = receiver.getUniqueId().toString();
        String senderName = sender1.getName();
        Player sender = Bukkit.getPlayer(senderName);

        if (sender == null) {
            receiver.sendMessage(prefix + "§c Error: " + ChatColor.YELLOW + "'" + senderName + "'" +
                    ChatColor.RED + " is not online.");
            return;
        }

        String senderUUID = sender.getUniqueId().toString();
        String requestKey = senderUUID + ":" + warpName;

        Map<String, WarpShareRequest> requests = pendingRequests.get(receiverUUID);
        if (requests == null || !requests.containsKey(requestKey)) {
            receiver.sendMessage("§cNo warp share request found for '" + warpName + "' from " + senderName + ".");
            return;
        }

        requests.remove(requestKey);
        if (requests.isEmpty()) {
            pendingRequests.remove(receiverUUID); // Clean up if no more requests
        }

        receiver.sendMessage("§cYou declined the warp '" + warpName + "' from " + senderName + ".");
        sender.sendMessage("§c" + receiver.getName() + " declined your warp request.");
    }


    private static class WarpShareRequest {
        private final Player sender;
        private final String warpName;
        private final Location warpLocation;

        public WarpShareRequest(Player sender, String warpName, Location warpLocation) {
            this.sender = sender;
            this.warpName = warpName;
            this.warpLocation = warpLocation;
        }

        @SuppressWarnings("unused")
        public Player getSender() {
            return sender;
        }

        public String getWarpName() {
            return warpName;
        }

        public Location getWarpLocation() {
            return warpLocation;
        }
    }
}
