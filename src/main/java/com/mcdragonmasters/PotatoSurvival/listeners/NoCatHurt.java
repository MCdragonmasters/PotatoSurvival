package com.mcdragonmasters.PotatoSurvival.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoCatHurt implements Listener {
    @EventHandler
    public void onCatHurt(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.CAT){
            e.setCancelled(true);
        }
    }
}
