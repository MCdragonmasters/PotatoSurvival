package com.mcdragonmasters.potatosurvival.commands.warps;

import com.mcdragonmasters.potatosurvival.database.WarpsManager;
import static com.mcdragonmasters.potatosurvival.PotatoSurvival.prefix;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"deprecation"})
public class RenameWarpCommand {
    public static void register() {
        List<Argument<?>> arguments = new ArrayList<>();
        arguments.add(new StringArgument("oldWarpName").replaceSuggestions(ArgumentSuggestions.strings(info ->
                WarpsManager.getPlayerWarps(info.sender())
        )));

        new CommandAPICommand("renamewarp")
                .withPermission("potatosurvival.renamewarp")
                .withArguments(arguments)
                .withArguments(new StringArgument("newWarpName"))
                .executesPlayer((sender, args) -> {

                    String oldWarpName = (String) args.get("oldWarpName");
                    String newWarpName = (String) args.get("newWarpName");
                    String uuid = sender.getUniqueId().toString();
                    if (WarpsManager.getWarp(sender.getUniqueId().toString(), oldWarpName) != null) {
                        if (WarpsManager.getWarp(sender.getUniqueId().toString(), newWarpName) == null) {
                            WarpsManager.saveWarp(sender.getUniqueId().toString(), newWarpName, WarpsManager.getWarp(sender.getUniqueId().toString(), oldWarpName));
                            WarpsManager.removeWarp(uuid, oldWarpName);
                            sender.sendMessage(prefix + ChatColor.GREEN + " Renamed Warp " + ChatColor.YELLOW + "'" + oldWarpName + "'" +
                                    ChatColor.GREEN + " to " + ChatColor.YELLOW + "'" + newWarpName + "'");
                        } else {
                            sender.sendMessage(prefix + ChatColor.RED + " A Warp with this name already exists!");
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.RED + " This Warp does not exist!");
                    }

                })
                .register();
    }
}
