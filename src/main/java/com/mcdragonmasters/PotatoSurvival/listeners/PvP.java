package com.mcdragonmasters.PotatoSurvival.listeners;

import com.mcdragonmasters.PotatoSurvival.commands.PvPCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvP implements Listener {
    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (!PvPCommand.isPvPEnabled()) {
                e.setCancelled(true);
            }
        }
    }
}
