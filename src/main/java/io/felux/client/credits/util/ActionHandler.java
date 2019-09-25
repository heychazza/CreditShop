package io.felux.client.credits.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionHandler {
    public static void executeActions(Player p, String actions) {
        handle(p, Collections.singletonList(actions));
    }

    public static void executeActions(Player p, String... actions) {
        handle(p, Arrays.asList(actions));
    }

    public static void executeActions(Player p, List<String> actions) {
        handle(p, actions);
    }

    private static void handle(Player p, List<String> list) {
        for (String msg : list) {
            if (!msg.contains(" ")) continue;
            String actionPrefix = msg.split(" ", 2)[0].toUpperCase();
            String actionData = msg.split(" ", 2)[1];
            actionData = ChatColor.translateAlternateColorCodes('&', actionData.replace("%player%", p.getName()));

            switch (actionPrefix) {
                case "[CONSOLE]":
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), actionData);
                    break;
                case "[PLAYER]":
                    p.performCommand(actionData);
                    break;
                case "[BROADCAST]":
                    Bukkit.broadcastMessage(actionData);
                    break;
                case "[MESSAGE]":
                    p.sendMessage(actionData);
                    break;
                case "[CHAT]":
                    p.chat(actionData);
            }
        }
    }
}
