package com.mcdragonmasters.potatosurvival.commands.warps;

import com.mcdragonmasters.potatosurvival.jsonDatabase.WarpsManager;
import static com.mcdragonmasters.potatosurvival.PotatoSurvival.prefixMini;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;

import java.util.ArrayList;
import java.util.List;

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
                            sender.sendRichMessage(prefixMini + "<green> Warp <yellow>'" + warpName + "'<green> Deleted");
                            WarpsManager.removeWarp(sender.getUniqueId().toString(), warpName);
                        } else {
                            sender.sendRichMessage(prefixMini + "<red> To Confirm please add <yellow>" +
                                    "'--confirm'<red> to the end of your command");
                        }
                    } else {
                        sender.sendRichMessage(prefixMini + "<red> This Warp does not exist!");
                    }

                })
                .register();
    }
}
