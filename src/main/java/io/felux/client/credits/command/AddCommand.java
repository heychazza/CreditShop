package io.felux.client.credits.command;

import io.felux.client.credits.Credits;
import io.felux.client.credits.command.util.Command;
import io.felux.client.credits.storage.PlayerData;
import io.felux.client.credits.util.Common;
import io.felux.client.credits.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class AddCommand {
    @Command(aliases = {"add"}, about = "Add to a players balance.", permission = "credits.add", usage = "add <player> <amount>", requiredArgs = 2)
    public static void execute(final Player sender, final Credits plugin, final String[] args) {

        if (!Common.isInteger(args[1])) {
            // Must be a number
            Lang.MUST_BE_A_NUMBER.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), Lang.FOOTER.asString());
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        PlayerData playerData = Common.getPlayer(player.getUniqueId());
        playerData.setCredits(playerData.getCredits() + Integer.parseInt(args[1]));
        Lang.ADD_BALANCE.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), player.getName(), playerData.getCredits(), Lang.FOOTER.asString());
    }
}
