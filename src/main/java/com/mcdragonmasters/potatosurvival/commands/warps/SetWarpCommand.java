package com.mcdragonmasters.potatosurvival.commands.warps;

import com.mcdragonmasters.potatosurvival.PotatoSurvival;
import com.mcdragonmasters.potatosurvival.database.WarpsManager;
import com.mcdragonmasters.potatosurvival.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.ChatColor;
import org.bukkit.Location;

@SuppressWarnings({"deprecation"})
public class SetWarpCommand {
    public static void register() {
        new CommandAPICommand("setwarp")
                .withPermission("potatosurvival.setwarp")
                .withArguments(new StringArgument("warpName"))
                .withOptionalArguments(new StringArgument("flags"))
                .executesPlayer((sender, args) -> {
                    String warpName = (String) args.get("warpName");
                    String flags = args.get("flags") != null ? (String) args.get("flags") : "";
                    if (WarpsManager.getWarp(sender.getUniqueId().toString(), warpName) != null) {
                        if (flags != null && !flags.contains("--override")) {
                            sender.sendMessage(PotatoSurvival.getPrefix() + ChatColor.RED + " This Warp is already set," + ChatColor.GRAY +
                                    " To Override please add" + ChatColor.YELLOW + " '--override'" + ChatColor.GRAY + " to the end of your command");
                            return;
                        }
                    }
                    Location location = sender.getLocation();
                        WarpsManager.removeWarp(sender.getUniqueId().toString(), warpName);
                        WarpsManager.saveWarp(sender.getUniqueId().toString(), warpName, location);
                        sender.sendMessage(PotatoSurvival.getPrefix() + ChatColor.GRAY + " Set warp " +
                                ChatColor.YELLOW + "'" + warpName + "'" + ChatColor.GRAY + " to " +
                                ChatColor.YELLOW + Utils.formatCoords(location.getX(), location.getY(), location.getZ()));
                })
                .register();
    }
}
