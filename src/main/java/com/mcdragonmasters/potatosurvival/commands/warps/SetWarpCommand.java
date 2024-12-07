package com.mcdragonmasters.potatosurvival.commands.warps;

import com.mcdragonmasters.potatosurvival.jsonDatabase.WarpsManager;
import com.mcdragonmasters.potatosurvival.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Location;

import static com.mcdragonmasters.potatosurvival.PotatoSurvival.prefixMini;

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
                            sender.sendRichMessage(prefixMini + "<red> This Warp is already set,<gray> To Override please add<yellow>" +
                                    " '--override'<gray> to the end of your command");
                            return;
                        }
                    }
                    Location loc = sender.getLocation();
                        WarpsManager.removeWarp(sender.getUniqueId().toString(), warpName);
                        WarpsManager.saveWarp(sender.getUniqueId().toString(), warpName, loc);
                        sender.sendRichMessage(prefixMini + "<gray> Set warp <yellow>'" + warpName +
                                "'<gray> to <yellow>" + Utils.formatCoords(loc.getX(), loc.getY(), loc.getZ()));
                })
                .register();
    }
}
