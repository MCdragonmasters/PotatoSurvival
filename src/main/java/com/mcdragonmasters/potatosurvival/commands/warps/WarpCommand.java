package com.mcdragonmasters.potatosurvival.commands.warps;

import com.mcdragonmasters.potatosurvival.jsonDatabase.WarpsManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

import static com.mcdragonmasters.potatosurvival.PotatoSurvival.prefixMini;

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
                            sender.sendRichMessage("<gold>Warping to <yellow>'" + args.get("warpName") + "'");
                        } else {
                            sender.sendRichMessage(prefixMini + "<red> This Warp does not exist!");
                        }
                })
                .register();
    }

}
