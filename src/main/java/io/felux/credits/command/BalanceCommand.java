package io.felux.credits.command;

import io.felux.credits.Credits;
import io.felux.credits.command.util.Command;
import io.felux.credits.storage.PlayerData;
import io.felux.credits.util.Common;
import io.felux.credits.util.Lang;
import org.bukkit.entity.Player;

import java.util.Map;

public class BalanceCommand {
    @Command(aliases = {"balance"}, about = "View balance.", permission = "credits.balance", usage = "balance")
    public static void execute(final Player sender, final Credits plugin, final String[] args) {
        PlayerData playerData = Common.getPlayer(sender.getUniqueId());
        Lang.BALANCE.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), playerData.getCredits(), Lang.FOOTER.asString());

        for (Map.Entry<String, Integer> stringIntegerEntry : playerData.getPurchases().entrySet()) {
            sender.sendMessage("You've purchased " + stringIntegerEntry.getKey() + " a total of " + stringIntegerEntry.getValue() + " time(s).");
        }
    }
}
