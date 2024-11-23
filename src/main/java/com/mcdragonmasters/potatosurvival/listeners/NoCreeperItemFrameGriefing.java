package com.mcdragonmasters.potatosurvival.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class NoCreeperItemFrameGriefing implements Listener {
    @EventHandler
    public void onItemFrameDamageByEntityExplosion(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.ITEM_FRAME) {
            if (e.getCause() == DamageCause.ENTITY_EXPLOSION) {
                e.setCancelled(true);
            }
        }
    }
}
