package com.mcdragonmasters.potatosurvival.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.StringArgument;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;

public class NickCommand {
    public static void register() {
        Argument<String> arg = new StringArgument("nick");
        new CommandAPICommand("nick")
                .withAliases("ec")
                .withArguments(arg)
                .withPermission("potato.nick")
                .executesPlayer((player, args) -> {
                    ServerPlayer ep = ((CraftPlayer) player).getHandle();

                    String name = args.getByArgument(arg);
                    GameProfile playerProfile = ep.getGameProfile();
                    Field ff;
                    try {
                        ff = playerProfile.getClass().getDeclaredField("name");
                        ff.setAccessible(true);
                        ff.set(playerProfile, name);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                    for(Player p : Bukkit.getOnlinePlayers()) {
                        ServerPlayerConnection connection = ((CraftPlayer) p).getHandle().connection;
                        connection.send(new ClientboundPlayerInfoRemovePacket(List.of(ep.getUUID())));
                        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ep));
                        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, ep));
                    }
                }).register();
    }
}