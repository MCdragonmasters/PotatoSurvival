package com.mcdragonmasters.PotatoSurvival.commands;

import com.mcdragonmasters.PotatoSurvival.PotatoSurvival;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings({"deprecation", "CallToPrintStackTrace "})
public class HomeCommand {
    public static void register() {
        new CommandAPICommand("home")
                .withPermission("potatosurvival.home")
                .executesPlayer((sender, args) -> {
                    try {
                        Location homeLocation = getHome(sender.getUniqueId().toString());
                        if (homeLocation != null) {
                            sender.teleport(homeLocation);
                            sender.sendMessage(ChatColor.GOLD + "Teleporting to Home");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Your Home is not set!");
                        }
                    } catch (SQLException e) {
                        sender.sendMessage(ChatColor.RED + "An error occurred while retrieving your home. Please try again.");
                        e.printStackTrace();
                    }
                })
                .register();
    }
    private static Location getHome(String playerUUID) throws SQLException {
        Connection connection = PotatoSurvival.getDatabaseManager().getConnection();
        String sql = "SELECT * FROM homes WHERE player_uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUUID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Extract home location data from the result
                String worldName = resultSet.getString("world");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                float yaw = resultSet.getFloat("yaw");
                float pitch = resultSet.getFloat("pitch");

                // Return the location
                return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            }
        }
        return null; // Return null if no home found
    }
}
