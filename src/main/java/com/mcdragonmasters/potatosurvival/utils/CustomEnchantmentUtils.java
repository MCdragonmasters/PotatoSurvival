package com.mcdragonmasters.potatosurvival.utils;


import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import static com.mcdragonmasters.potatosurvival.PotatoSurvival.nameSpace;

public class CustomEnchantmentUtils {

    public static Registry<Enchantment> EnchantRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);

    public static Enchantment getFromString(String enchantment) {

        NamespacedKey key = new NamespacedKey(nameSpace, enchantment);
        return EnchantRegistry.get(key);
    }
}
