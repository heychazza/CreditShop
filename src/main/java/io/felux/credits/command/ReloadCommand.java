package io.felux.credits.command;

import io.felux.credits.Credits;
import io.felux.credits.command.util.Command;
import io.felux.credits.util.Lang;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
    @Command(aliases = {"reload"}, about = "Reload plugin.", permission = "friends.reload", usage = "reload")
    public static void execute(final CommandSender sender, final Credits plugin, final String[] args) {
        plugin.handleReload();
        Lang.RELOAD.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), Lang.FOOTER.asString());
    }
}