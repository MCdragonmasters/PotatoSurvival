package com.mcdragonmasters.PotatoSurvival.commands;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import org.bukkit.Bukkit;

public class PvPCommand {

    private static boolean pvp = false;
    @SuppressWarnings("deprecation")
    public static void register() {
        new CommandAPICommand("pvp")
                .withPermission(CommandPermission.fromString("potatosurvival.pvp"))               // Required permissions
                .executes((sender, args) -> {
                    pvp = !pvp;

                    if (pvp) {
                        Bukkit.getServer().broadcastMessage("PvP is now Enabled");
                    } else {
                        Bukkit.getServer().broadcastMessage("PvP is now Disabled");
                    }
                })
                .register();
    }

    public static boolean isPvPEnabled() {
        return pvp;
    }
}
