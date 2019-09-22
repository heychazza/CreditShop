package net.chazza.credits;

import net.chazza.credits.command.util.CommandExecutor;
import net.chazza.credits.command.util.CommandManager;
import net.chazza.credits.maven.MavenLibrary;
import net.chazza.credits.maven.Repository;
import net.chazza.credits.storage.StorageHandler;
import net.chazza.credits.storage.mongodb.MongoDBHandler;
import net.chazza.credits.storage.mysql.MySQLHandler;
import net.chazza.credits.storage.sqlite.SQLiteHandler;
import net.chazza.credits.util.Common;
import net.chazza.credits.util.Lang;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

@MavenLibrary(groupId = "dev.morphia.morphia", artifactId = "core", version = "1.5.2")
@MavenLibrary(groupId = "com.github.j256", artifactId = "ormlite-core", version = "4.43", repo = @Repository(url = "https://jitpack.io"))
@MavenLibrary(groupId = "com.github.j256", artifactId = "ormlite-jdbc", version = "4.43", repo = @Repository(url = "https://jitpack.io"))
@MavenLibrary(groupId = "org.apache.logging.log4j", artifactId = "log4j-core", version = "2.7")
@MavenLibrary(groupId = "org.xerial", artifactId = "sqlite-jdbc", version = "3.7.2")
public class Credits extends JavaPlugin {

    private StorageHandler storageHandler;
    private CommandManager commandManager;

    public StorageHandler getStorageHandler() {
        return storageHandler;
    }

    public void log(String message) {
        if (getConfig().getBoolean("debug")) this.getLogger().info("[DEBUG] " + message);
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        saveDefaultConfig();
        Lang.init(this);
        getBanner();

        Common.loading("libraries");
//        LibraryLoader.loadAll(Friends.class);
//        setupChat();

        Common.loading("events");
//        new JoinListener(this);
//
//        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
//        mongoLogger.setLevel(Level.SEVERE);
//        org.apache.logging.log4j.core.Logger logger;
//        logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
//        logger.addFilter(new ConsoleFilter());
        handleReload();

        Common.loading("commands");
        registerCommands();

        Common.loading("hooks");

        Common.sendConsoleMessage(" ");
        getLogger().info("Successfully enabled in " + (System.currentTimeMillis() - start) + "ms.");
    }

    private void getBanner() {
        Common.sendConsoleMessage("&b ");
        Common.sendConsoleMessage("&b    ____");
        Common.sendConsoleMessage("&b   / __/");
        Common.sendConsoleMessage("&b  / _/" + "  &7" + getDescription().getName() + " v" + getDescription().getVersion());
        Common.sendConsoleMessage("&b /_/" + "    &7Running on Bukkit - " + getServer().getName());
        Common.sendConsoleMessage("&b ");
    }

    @Override
    public void onDisable() {
//        PlayerData.users.forEach(((uuid, playerData) -> {
//            getStorageHandler().pushData(uuid);
//        }));
    }

    private void setupStorage() {
        String storageType = Objects.requireNonNull(getConfig().getString("settings.storage.type", "SQLITE")).toUpperCase();

        if (!Arrays.asList("SQLITE", "MYSQL", "MONGODB").contains(storageType)) {
            storageType = "SQLITE";
        }

        Common.loading(storageType.toLowerCase() + " storage");

        switch (storageType) {
            case "SQLITE":
                storageHandler = new SQLiteHandler(getDataFolder().getPath());
                break;
            case "MYSQL":
                storageHandler = new MySQLHandler(
                        getConfig().getString("settings.storage.prefix", ""),
                        getConfig().getString("settings.storage.host", "localhost"),
                        getConfig().getInt("settings.storage.port", 3306),
                        getConfig().getString("settings.storage.database", "credits"),
                        getConfig().getString("settings.storage.username", "root"),
                        getConfig().getString("settings.storage.password", "qwerty123"));
                break;
            case "MONGODB":
                storageHandler = new MongoDBHandler(
                        getConfig().getString("settings.storage.prefix", ""),
                        getConfig().getString("settings.storage.host", "localhost"),
                        getConfig().getInt("settings.storage.port", 27017),
                        getConfig().getString("settings.storage.database", "credits"),
                        getConfig().getString("settings.storage.username", ""),
                        getConfig().getString("settings.storage.password", "")
                );
                break;
        }
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    private void registerCommands() {
        commandManager = new CommandManager(this);
        getCommand("credits").setExecutor(new CommandExecutor(this));
        if (getCommand("credits").getPlugin() != this) {
            getLogger().warning("/credits command is being handled by plugin other than " + getDescription().getName() + ". You must use /credits:credits instead.");
        }
    }

    public void handleReload() {
        reloadConfig();

        Common.loading("config");
        Lang.init(this);

        setupStorage();
    }
}
