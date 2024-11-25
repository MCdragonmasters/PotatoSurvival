package com.mcdragonmasters.potatosurvival.listeners;

import com.mcdragonmasters.potatosurvival.utils.CustomEnchantmentUtils;
import com.mcdragonmasters.potatosurvival.utils.Utils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.ArrayList;
import java.util.List;

import static com.mcdragonmasters.potatosurvival.PotatoSurvival.instance;


public class AutoSmelt implements Listener {

    public static Enchantment autoSmelt = CustomEnchantmentUtils.getFromString("auto_smelt");

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Location blockLocation = e.getBlock().getLocation();
        World world = e.getBlock().getWorld();
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (item.containsEnchantment(autoSmelt) && player.getGameMode() == GameMode.SURVIVAL) {
            e.setDropItems(false);
            List<ItemStack> list = new ArrayList<>();
            for (ItemStack drop : e.getBlock().getDrops(item, player)) {
                list.add(Utils.smeltedForm(drop));
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (ItemStack item : list) {
                        world.dropItem(blockLocation.toCenterLocation(), item);
                    }
                }
            }.runTaskLater(instance, 2);
        }
    }
}
