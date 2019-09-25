package io.felux.client.credits.util;

import io.felux.client.credits.Credits;
import io.felux.client.credits.storage.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Common {
    private static Credits credits = JavaPlugin.getPlugin(Credits.class);

    private static String prefix = "[C]";

    public static String translate(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static PlayerData getPlayer(UUID player) {
        if (PlayerData.get().containsKey(player)) {
            return PlayerData.get().get(player);
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        credits.getStorageHandler().pullData(offlinePlayer.getName(), offlinePlayer.getUniqueId());
        return PlayerData.get().get(player);
    }

    public static void log(String message) {
        if (credits.getConfig().getBoolean("debug")) credits.getLogger().info("[DEBUG] " + message);
    }

    public static void loading(String object) {
        sendConsoleMessage(prefix + " Loading " + object + "..");
    }

    public static void sendMessage(String object) {
        sendConsoleMessage(prefix + " " + object);
    }

    public static void sendConsoleMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(translate(msg));
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
