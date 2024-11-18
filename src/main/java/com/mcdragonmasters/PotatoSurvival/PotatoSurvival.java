package com.mcdragonmasters.PotatoSurvival;

import com.mcdragonmasters.PotatoSurvival.commands.EnderChestCommand;
import com.mcdragonmasters.PotatoSurvival.commands.PvPCommand;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"unused", "deprecation"})
public class PotatoSurvival extends JavaPlugin {

    public static PotatoSurvival instance;
    public static FileConfiguration config;
    private static final String prefix = net.md_5.bungee.api.ChatColor.of("#40ff00")
            + "[PotatoSurvival] " + ChatColor.RESET;
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
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        Logger.success("Enabled");

        //Register Commands
        PvPCommand.register();
        EnderChestCommand.register();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
    public static Plugin getInstance() {
        return instance;
    }
    public static class Logger{
        static ConsoleCommandSender console = Bukkit.getConsoleSender();
        public static void success(String message){
            console.sendMessage(prefix + ChatColor.GREEN + message);
        }

        public static void log(String message){
            console.sendMessage(prefix + ChatColor.WHITE + message);
        }

        public static void warn(String message){
            console.sendMessage(prefix + ChatColor.YELLOW + message);
        }

        public static void error(String message){
            console.sendMessage(prefix + ChatColor.RED + message);
        }
    }
}
