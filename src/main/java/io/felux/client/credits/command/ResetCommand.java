package io.felux.client.credits.command;

import io.felux.client.credits.Credits;
import io.felux.client.credits.command.util.Command;
import io.felux.client.credits.shop.ShopItem;
import io.felux.client.credits.storage.PlayerData;
import io.felux.client.credits.util.Common;
import io.felux.client.credits.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@SuppressWarnings("deprecation")
public class ResetCommand {
    @Command(aliases = {"reset"}, about = "Reset a players balance.", permission = "credits.reset", usage = "reset <player> <item>", requiredArgs = 2)
    public static void execute(final CommandSender sender, final Credits plugin, final String[] args) {

        ShopItem shopItem = plugin.getShopManager().getShop(args[1]);

        if (shopItem == null) {
            Lang.NO_ITEM_FOUND.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), args[1], Lang.FOOTER.asString());
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        PlayerData playerData = Common.getPlayer(player.getUniqueId());
        playerData.getPurchases().remove(shopItem.getId());
        Lang.RESET_ITEM_COUNT.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), player.getName(), shopItem.getId(), Lang.FOOTER.asString());
    }
}
