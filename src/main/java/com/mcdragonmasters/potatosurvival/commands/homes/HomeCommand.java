package com.mcdragonmasters.potatosurvival.commands.homes;

import com.mcdragonmasters.potatosurvival.jsonDatabase.HomesManager;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Location;

public class HomeCommand {
    public static void register() {
        new CommandAPICommand("home")
                .withPermission("potatosurvival.home")
                .executesPlayer((sender, args) -> {
                        Location homeLocation = HomesManager.getHome(sender.getUniqueId().toString());
                        if (homeLocation != null) {
                            sender.teleport(homeLocation);
                            sender.sendRichMessage("<gold>Teleporting to Home");
                        } else {
                            sender.sendRichMessage("<red>Your Home is not set!");
                        }
                })
                .register();
    }

}
