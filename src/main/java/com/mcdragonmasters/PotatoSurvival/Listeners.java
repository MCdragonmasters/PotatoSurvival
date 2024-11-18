package com.mcdragonmasters.PotatoSurvival;

import com.mcdragonmasters.PotatoSurvival.Utils.BlockDataUtils;
import com.mcdragonmasters.PotatoSurvival.commands.PvPCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Listeners implements Listener {
    @EventHandler
    public void onCatHurt(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.CAT){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntityType() == EntityType.CREEPER) {
            e.blockList().clear();
        }
    }
    @EventHandler
    public void onTripwireBreak(BlockBreakEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.SHEARS) {
             BlockData blockData = e.getBlock().getBlockData();
             if (!BlockDataUtils.getBooleanBlockDataTag(blockData, "disarmed")) {
                 e.setCancelled(true);
                 e.getBlock().setBlockData(BlockDataUtils.setBlockDataTag(blockData, "disarmed", true));
            }
        }
    }

    private final Set<String> processedBlocks = new HashSet<>();

    @EventHandler
    public void onFlow(BlockFromToEvent e) {
        Block block = e.getBlock();
        Block toBlock = e.getToBlock();

        if (block.getType().name().endsWith("TRAPDOOR")) {
            String blockKey = block.getWorld().getName() + ":" + block.getX() + "," + block.getY() + "," + block.getZ();

            if (processedBlocks.contains(blockKey)) {
                return;
            }

            if (BlockDataUtils.getBooleanBlockDataTag(toBlock.getBlockData(), "disarmed")) {
                processedBlocks.add(blockKey);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (BlockDataUtils.getBooleanBlockDataTag(block.getBlockData(), "powered")) {
                            cancel();
                        }
                        if (toBlock.getType() == Material.TRIPWIRE || toBlock.getType() == Material.WATER) {
                            toBlock.setBlockData(Bukkit.createBlockData("minecraft:tripwire[disarmed=true]"));
                        }
                        processedBlocks.remove(blockKey);
                    }
                }.runTaskTimer(PotatoSurvival.getInstance(), 1, 5);
            }
        }
    }
    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (!PvPCommand.isPvPEnabled()) {
                e.setCancelled(true);
            }
        }
    }
}
