package io.felux.credits.command;

import io.felux.credits.Credits;
import io.felux.credits.command.util.Command;
import io.felux.credits.shop.ShopInventory;
import org.bukkit.entity.Player;

public class ShopCommand {
    @Command(aliases = {"shop"}, about = "View shop.", permission = "credits.shop", usage = "shop")
    public static void execute(final Player sender, final Credits plugin, final String[] args) {
        new ShopInventory(plugin, sender).open(sender);
    }
}
