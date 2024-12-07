package com.mcdragonmasters.potatosurvival;

import com.mcdragonmasters.potatosurvival.commands.RegisterCommands;
import com.mcdragonmasters.potatosurvival.jsonDatabase.HomesManager;
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

    public static String nameSpace = "potatosurvival";
    public static String prefix = ChatColor.GOLD + "Potato Survival" + ChatColor.GRAY + " >" + ChatColor.RESET;
    public static PotatoSurvival instance;
    public static FileConfiguration config;
    public static String prefixMini = "<gold>Potato Survival<gray> ><reset>";

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
        RegisterCommands.register();

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
        return prefix;
    }
    public static Plugin getInstance() {
        return instance;
    }
}
