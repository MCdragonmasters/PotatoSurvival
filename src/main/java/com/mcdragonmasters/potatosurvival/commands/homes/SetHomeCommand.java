package com.mcdragonmasters.potatosurvival.commands.homes;

import com.mcdragonmasters.potatosurvival.jsonDatabase.HomesManager;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Location;

import static com.mcdragonmasters.potatosurvival.utils.Utils.formatCoords;
import static com.mcdragonmasters.potatosurvival.PotatoSurvival.prefixMini;

public class SetHomeCommand {
    public static void register() {
        new CommandAPICommand("sethome")
                .withPermission("potatosurvival.sethome")
                .executesPlayer((sender, args) -> {
                    Location loc = sender.getLocation();
                        HomesManager.removeOldHome(sender.getUniqueId().toString());
                        HomesManager.saveHome(sender.getUniqueId().toString(), loc);
                        sender.sendRichMessage(
                                prefixMini + "<gray> Set Home to <yellow>" + formatCoords(loc.getX(), loc.getZ(), loc.getZ()));
                })
                .register();
    }
}
