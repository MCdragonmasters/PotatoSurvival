package com.mcdragonmasters.potatosurvival.enchantments;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class AutoSmelt {

        public static void register(@NotNull BootstrapContext context) {
        // Register a new handled for the freeze lifecycle event on the enchantment registry
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            event.registry().register(
                    // The key of the registry
                    // Plugins should use their own namespace instead of minecraft or papermc
                    TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("papermc:pointy")),
                    b -> b.description(Component.text("Pointy"))
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.SWORDS))
                            .anvilCost(1)
                            .maxLevel(25)
                            .weight(10)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                            .activeSlots(EquipmentSlotGroup.ANY)
            );
        }));
    }
}
