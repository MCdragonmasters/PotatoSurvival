package com.mcdragonmasters.PotatoSurvival.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class NoCreeperBlockGriefing implements Listener {
    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntityType() == EntityType.CREEPER) {
            e.blockList().clear();
        }
    }
}
