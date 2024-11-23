package com.mcdragonmasters.potatosurvival.commands;

import com.mcdragonmasters.potatosurvival.database.HomesManager;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;


@SuppressWarnings({"deprecation"})
public class HomeCommand {
    public static void register() {
        new CommandAPICommand("home")
                .withPermission("potatosurvival.home")
                .executesPlayer((sender, args) -> {
                        Location homeLocation = HomesManager.getHome(sender.getUniqueId().toString());
                        if (homeLocation != null) {
                            sender.teleport(homeLocation);
                            sender.sendMessage(ChatColor.GOLD + "Teleporting to Home");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Your Home is not set!");
                        }
                })
                .register();
    }

}
