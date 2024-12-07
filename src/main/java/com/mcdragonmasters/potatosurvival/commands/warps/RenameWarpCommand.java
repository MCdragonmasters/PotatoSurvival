package com.mcdragonmasters.potatosurvival.commands.warps;

import com.mcdragonmasters.potatosurvival.jsonDatabase.WarpsManager;
import static com.mcdragonmasters.potatosurvival.PotatoSurvival.prefixMini;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;

import java.util.ArrayList;
import java.util.List;

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
                            sender.sendRichMessage(prefixMini + "<green> Renamed Warp <yellow>'" + oldWarpName +
                                    "'<green> to <yellow>'" + newWarpName + "'");
                        } else {
                            sender.sendRichMessage(prefixMini + "<red> A Warp with this name already exists!");
                        }
                    } else {
                        sender.sendRichMessage(prefixMini + "<red> This Warp does not exist!");
                    }

                })
                .register();
    }
}
