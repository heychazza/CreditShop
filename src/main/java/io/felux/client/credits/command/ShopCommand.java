package io.felux.client.credits.command;

import io.felux.client.credits.Credits;
import io.felux.client.credits.command.util.Command;
import io.felux.client.credits.shop.ShopInventory;
import org.bukkit.entity.Player;

public class ShopCommand {
    @Command(aliases = {"shop"}, about = "View shop.", permission = "credits.shop", usage = "shop")
    public static void execute(final Player sender, final Credits plugin, final String[] args) {
        new ShopInventory(plugin, sender).open(sender);
    }
}
