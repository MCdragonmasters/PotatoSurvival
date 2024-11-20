package com.mcdragonmasters.PotatoSurvival.listeners;

import static com.mcdragonmasters.PotatoSurvival.PotatoSurvival.instance;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;

public class Listeners {
    public static void registerListeners(Server server) {
        PluginManager manager = server.getPluginManager();
        manager.registerEvents(new AutoReplaceCrop(), instance);
        manager.registerEvents(new NoCatHurt(), instance);
        manager.registerEvents(new NoCreeperBlockGriefing(), instance);
        manager.registerEvents(new PvP(), instance);
        manager.registerEvents(new StringDuping(), instance);
    }
}
