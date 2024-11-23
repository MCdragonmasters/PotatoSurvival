package com.mcdragonmasters.potatosurvival.commands;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;

public class EnderChestCommand {

    public static void register() {
        new CommandAPICommand("enderchest")
                .withAliases("ec")
                .withPermission(CommandPermission.fromString("potatosurvival.enderchest"))               // Required permissions
                .executesPlayer((sender, args) -> {
                    sender.openInventory(sender.getEnderChest());
                })
                .register();
    }
}
