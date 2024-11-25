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
public class DeleteWarpCommand {
    public static void register() {
        List<Argument<?>> arguments = new ArrayList<>();
        arguments.add(new StringArgument("warpName").replaceSuggestions(ArgumentSuggestions.strings(info ->
                WarpsManager.getPlayerWarps(info.sender())
        )));

        new CommandAPICommand("deletewarp")
                .withAliases("delwarp")
                .withPermission("potatosurvival.deletewarp")
                .withArguments(arguments)
                .withOptionalArguments(new StringArgument("flags"))
                .executesPlayer((sender, args) -> {

                    String warpName = (String) args.get("warpName");
                    String flags = args.get("flags") != null ? (String) args.get("flags") : "";
                    if (WarpsManager.getWarp(sender.getUniqueId().toString(), warpName) != null) {
                        if (flags != null && flags.contains("--confirm")) {
                            sender.sendMessage(prefix + ChatColor.GREEN + " Warp " + ChatColor.YELLOW +
                                    "'" + warpName + "'" + ChatColor.GREEN + " Deleted");
                            WarpsManager.removeWarp(sender.getUniqueId().toString(), warpName);
                        } else {
                            sender.sendMessage(prefix + ChatColor.RED +
                                    " To Confirm please add " + ChatColor.YELLOW + "'--confirm'" + ChatColor.RED +
                                    " to the end of your command");
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.RED + " This Warp does not exist!");
                    }

                })
                .register();
    }
}
