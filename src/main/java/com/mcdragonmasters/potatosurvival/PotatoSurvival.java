package com.mcdragonmasters.potatosurvival;

import com.mcdragonmasters.potatosurvival.commands.EnderChestCommand;
import com.mcdragonmasters.potatosurvival.commands.HomeCommand;
import com.mcdragonmasters.potatosurvival.commands.PvPCommand;
import com.mcdragonmasters.potatosurvival.commands.SetHomeCommand;
import com.mcdragonmasters.potatosurvival.database.HomesManager;
import com.mcdragonmasters.potatosurvival.listeners.RegisterListeners;
import com.mcdragonmasters.potatosurvival.utils.Logger;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@SuppressWarnings({"unused", "deprecation", "CallToPrintStackTrace"})
public class PotatoSurvival extends JavaPlugin {

    public static String nameSpace = "potatosurvival:";
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

        File homesFile = new File(getDataFolder(), "homes.json");
        if (!homesFile.exists()) {
            try {
                // Create the file if it does not exist
                boolean newFile = homesFile.createNewFile();
            } catch (Exception e) {
                getLogger().warning("Could not create homes.json file!");
                e.printStackTrace();
            }
        }
        HomesManager.loadHomes();

        //Register Event RegisterListeners
        RegisterListeners.register(getServer());

        Logger.getLogger.success("Enabled");

        //Register Commands
        PvPCommand.register();
        EnderChestCommand.register();
        HomeCommand.register();
        SetHomeCommand.register();

    }

    @Override
    public void onDisable() {
        HomesManager.saveHomes();
        CommandAPI.onDisable();
    }
    public static String getNameSpace() {
        return nameSpace;
    }
    public static String getPrefix() {
        return ChatColor.GOLD + "Potato Survival" + ChatColor.GRAY + " >" + ChatColor.RESET;
    }
    public static Plugin getInstance() {
        return instance;
    }
}
