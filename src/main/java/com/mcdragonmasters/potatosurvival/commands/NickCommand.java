package com.mcdragonmasters.potatosurvival.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.StringArgument;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;


public class NickCommand {
    public static void register() {
        Argument<String> arg = new StringArgument("nick");

        new CommandAPICommand("nick")
                .withAliases("ec")
                .withArguments(arg)
                .withPermission("potato.nick")
                .executesPlayer((player, args) -> {
                    @SuppressWarnings("deprecation")
                    String name = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(args.getByArgument(arg)));
//                    if (name.length() > 16) {
//                        player.sendRichMessage("<red>Error: Name cannot be more than 16 characters");
//                        return;
//                    }
                    updatePlayerNametag(player, name);
                }).register();
    }
    public static void updatePlayerNametag(Player player, String newNametag) {

    }
}