package com.mcdragonmasters.potatosurvival.listeners;

import static com.mcdragonmasters.potatosurvival.PotatoSurvival.config;
import static com.mcdragonmasters.potatosurvival.PotatoSurvival.instance;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {
    public static void register(Server server) {
        PluginManager manager = server.getPluginManager();
        manager.registerEvents(new AutoReplaceCrop(), instance);
        manager.registerEvents(new NoCatHurt(), instance);
        manager.registerEvents(new NoCreeperBlockGriefing(), instance);
        manager.registerEvents(new PvP(), instance);
        manager.registerEvents(new StringDuping(), instance);
        manager.registerEvents(new DiscoverRecipesOnJoin(), instance);
        manager.registerEvents(new NoFarmlandTrample(), instance);
        manager.registerEvents(new AutoSmelt(), instance);
        if (config.getBoolean("sit")) {
            manager.registerEvents(new Sit(), instance);
        }
    }
}
