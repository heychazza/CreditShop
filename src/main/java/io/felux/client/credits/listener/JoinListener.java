package io.felux.client.credits.listener;

import io.felux.client.credits.Credits;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    private Credits plugin;

    public JoinListener(Credits plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(final AsyncPlayerPreLoginEvent e) {
        plugin.getStorageHandler().pullData(e.getName(), e.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getStorageHandler().pushData(e.getPlayer().getUniqueId());
            }
        }.runTaskLaterAsynchronously(plugin, 20);
    }
}
