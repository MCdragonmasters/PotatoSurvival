package com.mcdragonmasters.PotatoSurvival;

import com.mcdragonmasters.PotatoSurvival.commands.EnderChestCommand;
import com.mcdragonmasters.PotatoSurvival.commands.HomeCommand;
import com.mcdragonmasters.PotatoSurvival.commands.PvPCommand;
import com.mcdragonmasters.PotatoSurvival.commands.SetHomeCommand;
import com.mcdragonmasters.PotatoSurvival.database.DatabaseManager;
import com.mcdragonmasters.PotatoSurvival.listeners.Listeners;
import com.mcdragonmasters.PotatoSurvival.utils.Logger;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"unused", "deprecation"})
public class PotatoSurvival extends JavaPlugin {

    private static DatabaseManager databaseManager;
    public static PotatoSurvival instance;
    public static FileConfiguration config;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).usePluginNamespace());
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        instance = this;
        CommandAPI.onEnable();
        databaseManager = new DatabaseManager();
        try {
            databaseManager.connect();
            getLogger().info("Database connected!");
        } catch (Exception e) {
            getLogger().severe("Could not connect to database: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //Register Event Listeners
        Listeners.registerListeners(getServer());

        Logger.getLogger.success("Enabled");

        //Register Commands
        PvPCommand.register();
        EnderChestCommand.register();
        HomeCommand.register();
        SetHomeCommand.register();

    }

    @Override
    public void onDisable() {
        try {
            databaseManager.disconnect();
        } catch (Exception e) {
            getLogger().severe("Could not close database connection: " + e.getMessage());
        }
        CommandAPI.onDisable();
    }
    public static DatabaseManager getDatabaseManager() {
        return  databaseManager;
    }
    public static String getPrefix() {
        return ChatColor.GOLD + "Potato Survival" + ChatColor.GRAY + " >" + ChatColor.RESET;
    }
    public static Plugin getInstance() {
        return instance;
    }
}
