package net.chazza.credits.command;

import net.chazza.credits.command.util.Command;
import net.chazza.friends.Friends;
import net.chazza.friends.util.Lang;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
    @Command(aliases = {"reload"}, about = "Reload plugin.", permission = "friends.reload", usage = "reload")
    public static void execute(final CommandSender sender, final Friends plugin, final String[] args) {
        plugin.handleReload();
        Lang.RELOAD.send(sender);
    }
}
