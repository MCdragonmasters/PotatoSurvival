package com.mcdragonmasters.potatosurvival.listeners;

import com.mcdragonmasters.potatosurvival.PotatoSurvival;
import com.mcdragonmasters.potatosurvival.utils.BlockDataUtils;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class StringDuping implements Listener {

    private final BlockData disarmedTripwire = Bukkit.createBlockData("minecraft:tripwire[disarmed=true]");

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
    public void onWaterBreakString(BlockBreakBlockEvent e) {
        Block sourceBlock = e.getSource();
        Block block = e.getBlock();

        if (!(block.getType() == Material.TRIPWIRE)) return;
        if (!(block.getBlockData().toString().contains("disarmed=true"))) return;
        if (!sourceBlock.getType().name().endsWith("TRAPDOOR")) return;

        String blockKey = sourceBlock.getWorld().getName() + ":" + sourceBlock.getX() + "," + sourceBlock.getY() + "," + sourceBlock.getZ();

        if (processedBlocks.contains(blockKey)) {
            return;
        }
        processedBlocks.add(blockKey);
        new BukkitRunnable() { @Override public void run() {
            if (sourceBlock.getBlockData().toString().contains("powered=true")) {
                cancel();
            }
            if (block.getType() == Material.TRIPWIRE || block.getType() == Material.WATER) {
                block.setBlockData(disarmedTripwire);
            }
            processedBlocks.remove(blockKey);
          }
        }.runTaskLater(PotatoSurvival.getInstance(), 1);
    }
}