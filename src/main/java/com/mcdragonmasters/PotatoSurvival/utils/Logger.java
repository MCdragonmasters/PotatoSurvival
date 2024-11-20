package com.mcdragonmasters.PotatoSurvival.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

@SuppressWarnings({"deprecation", "unused"})
public class Logger {
    private static final String prefix = net.md_5.bungee.api.ChatColor.of("#40ff00")
            + "[PotatoSurvival] " + ChatColor.RESET;
    public static class getLogger{
        static ConsoleCommandSender console = Bukkit.getConsoleSender();
        public static void success(String message){
            console.sendMessage(prefix + ChatColor.GREEN + message);
        }

        public static void log(String message){
            console.sendMessage(prefix + ChatColor.WHITE + message);
        }

        public static void warn(String message){
            console.sendMessage(prefix + ChatColor.YELLOW + message);
        }

        public static void error(String message){
            console.sendMessage(prefix + ChatColor.RED + message);
        }
    }
}
