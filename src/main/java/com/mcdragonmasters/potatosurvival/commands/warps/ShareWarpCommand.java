package com.mcdragonmasters.potatosurvival.commands.warps;

import com.mcdragonmasters.potatosurvival.jsonDatabase.WarpsManager;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Objects;

import static com.mcdragonmasters.potatosurvival.PotatoSurvival.prefixMini;

public class ShareWarpCommand {
    public static void register() {
         Argument<String> warpNameArg = new StringArgument("warpNameArg").replaceSuggestions(
                 ArgumentSuggestions.strings(info ->
                         WarpsManager.getPlayerWarps(info.sender()
        )));
         Argument<String> acceptOrDeclineArg = new MultiLiteralArgument("acceptOrDecline",
                 "accept", "decline").replaceSuggestions(ArgumentSuggestions.strings(Collections.emptyList()));

        new CommandTree("sharewarp")
                .withPermission("potatosurvival.sharewarp")
                .then(warpNameArg
                    .then(new PlayerArgument("player")
                        .executesPlayer((player, args) -> {
                            String warpName = args.getByArgument(warpNameArg);
                            Player receiver = (Player) Objects.requireNonNull(args.get("player"));
                            if (WarpsManager.getWarp(player.getUniqueId().toString(), warpName) != null) {
                                WarpsManager.shareWarp(player, receiver, warpName);
                            } else {
                                player.sendRichMessage(prefixMini + "<red> This Warp does not exist!");
                            }
                        })
                        .then(acceptOrDeclineArg
                                .executesPlayer((player, args) -> {
                                    String warpName = args.getByArgument(warpNameArg);
                                    Player sender = (Player) Objects.requireNonNull(args.get("player"));
                                    String acceptOrDecline = args.getByArgument(acceptOrDeclineArg);
                                    switch (acceptOrDecline) {
                                        case "accept":
                                            WarpsManager.acceptWarp(player, sender, warpName);
                                            break;
                                        case "decline":
                                            WarpsManager.declineWarp(player, sender, warpName);
                                            break;
                                        case null:
                                            player.sendRichMessage("<red>Error: null");
                                            break;
                                        default:
                                            player.sendRichMessage("<red>Error");
                                            break;
                                    }
                                }))))
                .register();
    }

}
