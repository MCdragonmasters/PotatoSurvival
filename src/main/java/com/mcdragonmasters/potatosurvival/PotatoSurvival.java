package com.mcdragonmasters.potatosurvival;

import com.mcdragonmasters.potatosurvival.commands.RegisterCommands;
import com.mcdragonmasters.potatosurvival.jsonDatabase.HomesManager;
import com.mcdragonmasters.potatosurvival.jsonDatabase.WarpsManager;
import com.mcdragonmasters.potatosurvival.listeners.RegisterListeners;
import com.mcdragonmasters.potatosurvival.utils.Logger;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


@SuppressWarnings({"unused", "deprecation"})
public class PotatoSurvival extends JavaPlugin {

    public final static String nameSpace = "potatosurvival";
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

        HomesManager.loadHomes();
        WarpsManager.loadWarps();

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
