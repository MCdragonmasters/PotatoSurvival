package com.mcdragonmasters.PotatoSurvival.commands;

import com.mcdragonmasters.PotatoSurvival.PotatoSurvival;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;

@SuppressWarnings({"deprecation", "CallToPrintStackTrace"})
public class SetHomeCommand {
    public static String format(double num) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(num);
    }
    public static void register() {
        new CommandAPICommand("sethome")
                .withPermission("potatosurvival.sethome")
                .executesPlayer((sender, args) -> {
                    Location location = sender.getLocation();
                    try {
                        removeOldHome(sender.getUniqueId().toString());
                        saveHome(sender.getUniqueId().toString(), location);
                        sender.sendMessage(PotatoSurvival.getPrefix() + ChatColor.GRAY + " Set Home to " +
                                ChatColor.YELLOW + format(location.getX()) + ", " + format(location.getY()) + ", " + format(location.getZ()));
                    } catch (SQLException e) {
                        sender.sendMessage("An error occurred while saving your home. Please try again.");
                        e.printStackTrace();
                    }
                })
                .register();
    }

    private static void removeOldHome(String playerUUID) throws SQLException {
        Connection connection = PotatoSurvival.getDatabaseManager().getConnection();
        String sql = "DELETE FROM homes WHERE player_uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUUID);
            statement.executeUpdate();
        }
    }
    private static void saveHome(String playerUUID, Location location) throws SQLException {
        Connection connection = PotatoSurvival.getDatabaseManager().getConnection();
        String sql = "INSERT INTO homes (player_uuid, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUUID);
            statement.setString(2, location.getWorld().getName());
            statement.setDouble(3, location.getX());
            statement.setDouble(4, location.getY());
            statement.setDouble(5, location.getZ());
            statement.setFloat(6, location.getYaw());
            statement.setFloat(7, location.getPitch());
            statement.executeUpdate();
        }
    }
}
