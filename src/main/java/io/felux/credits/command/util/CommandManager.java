package io.felux.credits.command.util;

import io.felux.credits.Credits;
import io.felux.credits.command.*;
import io.felux.credits.util.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {

    private Map<String, Method> commands = new HashMap<>();

    public Map<String, Method> getCommands() {
        return commands;
    }

    private Credits plugin;

    public CommandManager(Credits plugin) {
        this.plugin = plugin;

        List<Class<?>> commandClasses = Arrays.asList(
                BalanceCommand.class,
                SetCommand.class,
                ReloadCommand.class,
                ShopCommand.class,
                ResetCommand.class,
                AddCommand.class
        );

        for (Class cmdClass : commandClasses) {
            for (Method method : cmdClass.getMethods()) {
                if (!method.isAnnotationPresent(Command.class)) continue; // make sure method is marked as an annotation

                if (method.getParameters().length != 3) {
                    plugin.getLogger().warning("Method " + method.toGenericString().replace("public static void ", "") + " annotated as command but parameters count != 2");
                    continue;
                }
                if (method.getParameters()[0].getType() != CommandSender.class && method.getParameters()[0].getType() != Player.class) {
                    plugin.getLogger().warning("Method " + method.toGenericString().replace("public static void ", "") + " annotated as command but parameter 1's type != CommandSender || Player");
                    continue;
                }
                if (method.getParameters()[2].getType() != String[].class) {
                    plugin.getLogger().warning("Method " + method.toGenericString().replace("public static void ", "") + " annotated as command but parameter 3's type != String[]");
                    continue;
                }

                Command annotation = method.getAnnotation(Command.class);
                for (String commandName : annotation.aliases()) commands.put(commandName.toLowerCase(), method);
            }
        }
    }

    boolean handle(CommandSender sender, String command, String[] args) {
        if (command == null) {
            Lang.HEADER.send(sender);
            for (Map.Entry<String, Method> commandEntry : commands.entrySet()) {
                Command commandAnnotation = commandEntry.getValue().getAnnotation(Command.class);
                Lang.COMMAND_FORMAT.send(sender, commandAnnotation.usage(), commandAnnotation.about());
            }
            Lang.FOOTER.send(sender);
            return true;
        }

        if (commands.containsKey(command.toLowerCase())) {
            try {
                Method commandMethod = commands.get(command.toLowerCase());
                Command commandAnnotation = commandMethod.getAnnotation(Command.class);

                if (!sender.hasPermission(commandAnnotation.permission()) && (sender instanceof Player)) {
                    Lang.NO_PERMISSION.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), Lang.FOOTER.asString());
                    return true;
                }

                if (commandMethod.getParameters()[0].getType() == Player.class && !(sender instanceof Player)) {
                    Lang.PLAYERS_ONLY.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), Lang.FOOTER.asString());
                    return true;
                }

                if (args.length < commandAnnotation.requiredArgs()) {
                    Lang.USAGE.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), commandAnnotation.usage(), Lang.FOOTER.asString());
                    return true;
                }

                commandMethod.invoke(null, sender, plugin, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Lang.INVALID_COMMAND.send(sender, Lang.HEADER.asString(), Lang.PREFIX.asString(), Lang.FOOTER.asString());
        }

        return true;
    }

}