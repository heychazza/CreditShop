package net.chazza.credits.command;

import net.chazza.credits.Credits;
import net.chazza.credits.command.util.Command;
import net.chazza.credits.shop.ShopInventory;
import org.bukkit.entity.Player;

public class ShopCommand {
    @Command(aliases = {"shop"}, about = "View shop.", permission = "credits.shop", usage = "shop")
    public static void execute(final Player sender, final Credits plugin, final String[] args) {
        new ShopInventory(plugin, sender).open(sender);
    }
}
