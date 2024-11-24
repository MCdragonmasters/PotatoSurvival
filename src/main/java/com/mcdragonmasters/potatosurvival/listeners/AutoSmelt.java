package com.mcdragonmasters.potatosurvival.listeners;

import com.mcdragonmasters.potatosurvival.utils.CustomEnchantmentUtils;
import com.mcdragonmasters.potatosurvival.utils.Utils;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;


public class AutoSmelt implements Listener {

    public static Enchantment autoSmelt = CustomEnchantmentUtils.getFromString("auto_smelt");

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        World world = e.getBlock().getWorld();
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (item.containsEnchantment(autoSmelt)) {
            e.setDropItems(false);
            for (ItemStack drop : e.getBlock().getDrops(item, player)) {
                world.dropItem(block.getLocation(), Utils.smeltedForm(drop));
            }
        }
    }
}
