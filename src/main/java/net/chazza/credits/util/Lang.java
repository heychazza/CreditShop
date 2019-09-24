package net.chazza.credits.util;

import net.chazza.credits.Credits;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public enum Lang {

    HEADER("&7"),
    FOOTER("&7"),

    PREFIX("&8[&eRC&8]&7"),

    NO_PERMISSION("{1} You don't have permission to use that."),
    INVALID_COMMAND("{1} Unknown sub-command, try &e/credits&7."),
    PLAYERS_ONLY("{1} This command can only be used in-game."),
    USAGE("{1} Usage: &e/credits {2}&7."),
    MUST_BE_A_NUMBER("{1} You must specify a valid number."),
    RELOAD("{1} Configuration reloaded."),

    COMMAND_FORMAT(" &6/credits &e{0} &7({1})"),

    BALANCE("{1} Credits: &e{2}"),

    SET_BALANCE("{1} You set &6{2}'s &7balance to &e{3}&7."),
    RESET_ITEM_COUNT("{1} You reset &6{2}'s &7purchase count for the &e{3} &7shop."),
    NO_ITEM_FOUND("{1} No shop item was found by the id '&e{2}&7'."),

    GUI_TITLE("Credits Shop (Balance: {0})"),

    CANNOT_AFFORD("{1} &7You need &e{2} &7more credits to purchase this item."),
    PURCHASED("{1} &7You've spent &6{2} &7credits and now have &e{3} &7left.");

    private String message;
    private static FileConfiguration c;

    Lang(final String... def) {
        this.message = String.join("\n", def);
    }

    private String getMessage() {
        return this.message;
    }

    public static String format(String s, final Object... objects) {
        for (int i = 0; i < objects.length; ++i) {
            s = s.replace("{" + i + "}", String.valueOf(objects[i]));
        }

        return Common.translate(s);
    }

    public static boolean init(Credits levellingTools) {
        Lang.c = levellingTools.getConfig();
        for (final Lang value : values()) {
            if (value.getMessage().split("\n").length == 1) {
                Lang.c.addDefault(value.getPath().toLowerCase(), value.getMessage());
            } else {
                Lang.c.addDefault(value.getPath().toLowerCase(), value.getMessage().split("\n"));
            }
        }
        Lang.c.options().copyDefaults(true);
        levellingTools.saveConfig();
        return true;
    }

    public String getPath() {
        return "message." + this.name().toLowerCase().toLowerCase();
    }

    public void send(final Player player, final Object... args) {
        final String message = this.asString(args);
        Arrays.stream(message.split("\n")).forEach(msg -> {
            if (msg.startsWith("[JSON]")) {
                player.spigot().sendMessage(toBaseComponent(msg));
                return;
            }

            player.sendMessage(msg);
        });
    }

    public void sendRaw(final Player player, final Object... args) {
        final String message = this.asString(args);
        Arrays.stream(message.split("\n")).forEach(player::sendRawMessage);
    }

    public void send(final CommandSender sender, final Object... args) {
        if (sender instanceof Player) {
            this.send((Player) sender, args);
        } else {
            Arrays.stream(this.asString(args).split("\n")).forEach(sender::sendMessage);
        }
    }

    public String asString(final Object... objects) {
        Optional<String> opt = Optional.empty();
        if (Lang.c.contains(this.getPath())) {
            if (Lang.c.isList(getPath())) {
                opt = Optional.of(String.join("\n", Lang.c.getStringList(this.getPath())));
            } else if (Lang.c.isString(this.getPath())) {
                opt = Optional.ofNullable(Lang.c.getString(this.getPath()));
            }
        }

        return format(opt.orElse(this.message), objects);
    }

    public static BaseComponent[] toBaseComponent(String json) {
        json = json.replace("[JSON]", "");
        return ComponentSerializer.parse(json);
    }

}
