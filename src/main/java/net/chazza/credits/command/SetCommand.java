package net.chazza.credits.command;

import net.chazza.credits.Credits;
import net.chazza.credits.command.util.Command;
import net.chazza.credits.storage.PlayerData;
import net.chazza.credits.util.Common;
import net.chazza.credits.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class SetCommand {
    @Command(aliases = {"set"}, about = "Set a players balance.", permission = "credits.set", usage = "set <player> <amount>", requiredArgs = 2)
    public static void execute(final Player sender, final Credits plugin, final String[] args) {

        if (!Common.isInteger(args[1])) {
            // Must be a number
            Lang.MUST_BE_A_NUMBER.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), Lang.FOOTER.asString());
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        PlayerData playerData = Common.getPlayer(player.getUniqueId());
        playerData.setCredits(Integer.parseInt(args[1]));
        Lang.SET_BALANCE.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), player.getName(), playerData.getCredits(), Lang.FOOTER.asString());
    }
}
