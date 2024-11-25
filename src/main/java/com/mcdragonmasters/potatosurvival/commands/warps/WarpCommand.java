package com.mcdragonmasters.potatosurvival.commands.warps;

import com.mcdragonmasters.potatosurvival.PotatoSurvival;
import com.mcdragonmasters.potatosurvival.database.WarpsManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"deprecation"})
public class WarpCommand {
    public static void register() {
        List<Argument<?>> arguments = new ArrayList<>();
        arguments.add(new StringArgument("warpName").replaceSuggestions(ArgumentSuggestions.strings(info ->
                WarpsManager.getPlayerWarps(info.sender())
        )));
        new CommandAPICommand("warp")
                .withPermission("potatosurvival.warp")
                .withArguments(arguments)
                .executesPlayer((sender, args) -> {
                        Location warpLocation = WarpsManager.getWarp(sender.getUniqueId().toString(), (String) args.get("warpName"));
                        if (warpLocation != null) {
                            sender.teleport(warpLocation);
                            sender.sendMessage(ChatColor.GOLD + "Warping to " + ChatColor.YELLOW + "'" + args.get("warpName") + "'");
                        } else {
                            sender.sendMessage(PotatoSurvival.getPrefix() + ChatColor.RED + " This Warp does not exist!");
                        }
                })
                .register();
    }

}
