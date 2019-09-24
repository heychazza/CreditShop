package net.chazza.credits;

import dev.chapi.api.exception.InvalidMaterialException;
import dev.chapi.api.item.ItemBuilder;
import net.chazza.credits.command.util.CommandExecutor;
import net.chazza.credits.command.util.CommandManager;
import net.chazza.credits.maven.LibraryLoader;
import net.chazza.credits.maven.MavenLibrary;
import net.chazza.credits.shop.ShopItem;
import net.chazza.credits.shop.ShopManager;
import net.chazza.credits.storage.StorageHandler;
import net.chazza.credits.storage.mongodb.MongoDBHandler;
import net.chazza.credits.storage.mysql.MySQLHandler;
import net.chazza.credits.storage.sqlite.SQLiteHandler;
import net.chazza.credits.util.Common;
import net.chazza.credits.util.Lang;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@MavenLibrary(groupId = "org.apache.logging.log4j", artifactId = "log4j-core", version = "2.7")
public class Credits extends JavaPlugin {

    private StorageHandler storageHandler;
    private ShopManager shopManager;
    private CommandManager commandManager;

    public StorageHandler getStorageHandler() {
        return storageHandler;
    }

    public ShopManager getShopManager() {
        return shopManager;
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

        Common.loading("shops");
        setupShop();

        Common.loading("hooks");

        Common.sendConsoleMessage(" ");
        getLogger().info("Successfully enabled in " + (System.currentTimeMillis() - start) + "ms.");
    }

    private void getBanner() {
        Common.sendConsoleMessage("&b ");
        Common.sendConsoleMessage("&b   _____");
        Common.sendConsoleMessage("&b  / ___/");
        Common.sendConsoleMessage("&b / /__  " + "  &7" + getDescription().getName() + " v" + getDescription().getVersion());
        Common.sendConsoleMessage("&b \\___/  " + "  &7Running on Bukkit - " + getServer().getName());
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
                LibraryLoader.loadAll(SQLiteHandler.class);
                storageHandler = new SQLiteHandler(getDataFolder().getPath());
                break;
            case "MYSQL":
                LibraryLoader.loadAll(MySQLHandler.class);
                storageHandler = new MySQLHandler(
                        getConfig().getString("settings.storage.prefix", ""),
                        getConfig().getString("settings.storage.host", "localhost"),
                        getConfig().getInt("settings.storage.port", 3306),
                        getConfig().getString("settings.storage.database", "credits"),
                        getConfig().getString("settings.storage.username", "root"),
                        getConfig().getString("settings.storage.password", "qwerty123"));
                break;
            case "MONGODB":
                LibraryLoader.loadAll(MongoDBHandler.class);
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

    private void setupShop() {
        this.shopManager = new ShopManager();

        for (String itemId : getConfig().getConfigurationSection("items").getKeys(false)) {
            String path = "items." + itemId;
            getLogger().info("Configuring the '" + itemId + "' shop..");

            int price = getConfig().getInt(path + ".cost", 1);

            int limit = getConfig().getInt(path + ".limit", -1);

            String itemName = getConfig().getString(path + ".item.name");
            List<String> itemLore = getConfig().getStringList(path + ".item.lore");
            itemLore.replaceAll(lore -> lore.replace("{cost}", price + ""));

            List<String> itemActions = getConfig().getStringList(path + ".actions");

            String material = getConfig().getString(path + ".item.material");

            try {
                shopManager.addItem(new ShopItem(itemId, price, limit, itemActions, new ItemBuilder(material != null ? material : "BARRIER").withName(itemName != null ? itemName : "&e" + itemId).withLore(itemLore).getItem()));
            } catch (InvalidMaterialException e) {
                e.printStackTrace();
            }
        }
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    private void registerCommands() {
        commandManager = new CommandManager(this);
        try {
            getCommand("credits").setExecutor(new CommandExecutor(this));
            if (getCommand("credits").getPlugin() != this) {
                getLogger().warning("/credits command is being handled by plugin other than " + getDescription().getName() + ". You must use /credits:credits instead.");
            }
        } catch (NullPointerException e) {
            getLogger().warning("The /credits command wasn't found in the plugin.yml file.");
        }
    }

    public void handleReload() {
        reloadConfig();

        Common.loading("config");
        Lang.init(this);
        setupShop();

        setupStorage();
    }
}
