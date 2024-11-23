package com.mcdragonmasters.potatosurvival.commands;

import com.mcdragonmasters.potatosurvival.PotatoSurvival;
import com.mcdragonmasters.potatosurvival.database.HomesManager;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.text.DecimalFormat;

@SuppressWarnings({"deprecation"})
public class SetHomeCommand {
    public static String format(double num) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(num);
    }
    public static void register() {
        new CommandAPICommand("sethome")
                .withPermission("potatosurvival.sethome")
                .executesPlayer((sender, args) -> {
                    Location location = sender.getLocation();
                        HomesManager.removeOldHome(sender.getUniqueId().toString());
                        HomesManager.saveHome(sender.getUniqueId().toString(), location);
                        sender.sendMessage(PotatoSurvival.getPrefix() + ChatColor.GRAY + " Set Home to " +
                                ChatColor.YELLOW + format(location.getX()) + ", " +
                                format(location.getY()) + ", " + format(location.getZ()));
                })
                .register();
    }
}
