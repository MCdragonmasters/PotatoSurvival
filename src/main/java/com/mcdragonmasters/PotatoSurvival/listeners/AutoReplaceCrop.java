package com.mcdragonmasters.PotatoSurvival.listeners;

import com.mcdragonmasters.PotatoSurvival.PotatoSurvival;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoReplaceCrop implements Listener {
    @EventHandler
    public void onCropBreak(BlockBreakEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType().name().endsWith("HOE")) {
            if (Tag.CROPS.isTagged(e.getBlock().getType())) {
                Block block = e.getBlock();
                String data = block.getBlockData().getAsString();
                BlockData blockData = null;
                if (data.equals("minecraft:wheat[age=7]")) {
                    blockData = (Bukkit.createBlockData("minecraft:wheat[age=0]"));
                } else if (data.equals("minecraft:potatoes[age=7]")) {
                    blockData = (Bukkit.createBlockData("minecraft:potatoes[age=0]"));
                } else if (data.equals("minecraft:carrots[age=7]")) {
                    blockData = (Bukkit.createBlockData("minecraft:carrots[age=0]"));
                } else if (data.equals("minecraft:beetroots[age=3]")) {
                    blockData = (Bukkit.createBlockData("minecraft:beetroots[age=0]"));
                } else {
                    e.setCancelled(true);
                }
                BlockData finalBlockData = blockData;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (finalBlockData != null) {
                            block.setBlockData(finalBlockData);
                        }
                    }
                }.runTaskLater(PotatoSurvival.getInstance(), 3);
            }
        }
    }
}
