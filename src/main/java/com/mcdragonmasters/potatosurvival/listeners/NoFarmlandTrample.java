package com.mcdragonmasters.potatosurvival.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class NoFarmlandTrample implements Listener {
    @EventHandler
    public void onFarmlandTrample(EntityChangeBlockEvent e) {
        if (e.getBlock().getType() == Material.FARMLAND && e.getTo() == Material.DIRT) {
            e.setCancelled(true);
        }
    }
}
