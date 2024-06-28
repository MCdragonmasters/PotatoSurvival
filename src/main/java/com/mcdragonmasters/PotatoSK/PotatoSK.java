package com.mcdragonmasters.PotatoSK;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.bstats.bukkit.Metrics;
import ch.njol.skript.bstats.charts.SimplePie;
import com.mcdragonmasters.PotatoSK.utils.PackageLoader;
import com.olyno.skriptmigrate.SkriptMigrate;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;

@SuppressWarnings({"unused", "RedundantSuppression", "deprecation"})
public class PotatoSK extends JavaPlugin {

    public static PotatoSK instance;
    public static FileConfiguration config;
    public static HashMap<String, Boolean> element_map = new HashMap<>();
    private static Metrics metrics;
    private static final String prefix = net.md_5.bungee.api.ChatColor.of("#40ff00")
            + "[PotatoSK] " + ChatColor.RESET;
    SkriptAddon addon;

    public void onEnable() {

        instance = this;
        addon = Skript.registerAddon(this);

        try {
            addon.loadClasses("com.mcdragonmasters.PotatoSK.skript");
            registerHooks("WorldEdit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Register Metrics
        Metrics metrics = new Metrics(this, 22275);
        metrics.addCustomChart(new SimplePie("skript_version", () -> Skript.getVersion().toString()));
        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "PotatoSK has been enabled!");

        //Register events
        PluginManager pm = getServer().getPluginManager();
        //pm.registerEvents(new JoinLeave(), this);
        new PackageLoader<Listener>("com.mcdragonmasters.PotatoSK.skript.events.bukkit", "register bukkit events").getList()
                .thenAccept(events -> {
                    for (Listener evt : events) {
                        getServer().getPluginManager().registerEvents(evt, this);
                    }
                });

        // Setup migrations
        if (classExist()) {
            SkriptMigrate.load(this);
        }

        if (!getDataFolder().exists()) {
            saveDefaultConfig();
        }

        config = getConfig();

    }

    private boolean classExist() {
        try {
            Class.forName("com.olyno.skriptmigrate.SkriptMigrate");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    public void registerHooks(String plugin) throws IOException {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled(plugin) || config.getBoolean(plugin.toLowerCase()+"-hook", true)) {
            addon.loadClasses("com.mcdragonmasters.PotatoSK.hooks."+plugin.toLowerCase());
            Logger.success(plugin+" hook loaded!");
        } else{
            Logger.error(plugin+" hook not loaded.");
        }
    }
    public static class Logger{

        public static void success(String message){
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + message);
        }

        public static void log(String message){
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.WHITE + message);
        }

        public static void warn(String message){
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + message);
        }

        public static void error(String message){
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + message);
        }
    }
}
