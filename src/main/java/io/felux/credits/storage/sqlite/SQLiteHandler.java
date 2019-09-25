package io.felux.credits.storage.sqlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import io.felux.credits.maven.MavenLibrary;
import io.felux.credits.maven.Repository;
import io.felux.credits.storage.PlayerData;
import io.felux.credits.storage.StorageHandler;
import io.felux.credits.util.SerializedMap;

import java.sql.SQLException;
import java.util.UUID;

@MavenLibrary(groupId = "com.github.j256", artifactId = "ormlite-core", version = "4.43", repo = @Repository(url = "https://jitpack.io"))
@MavenLibrary(groupId = "com.github.j256", artifactId = "ormlite-jdbc", version = "4.43", repo = @Repository(url = "https://jitpack.io"))
@MavenLibrary(groupId = "org.xerial", artifactId = "sqlite-jdbc", version = "3.7.2")
public class SQLiteHandler implements StorageHandler {

    private ConnectionSource connectionSource;
    private Dao<SQLitePlayerData, String> accountDao;

    public SQLiteHandler(String path) {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + path + "/playerdata.db");
            accountDao = DaoManager.createDao(connectionSource, SQLitePlayerData.class);
            TableUtils.createTableIfNotExists(connectionSource, SQLitePlayerData.class);
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pullData(String name, UUID uuid) {
        try {
            SQLitePlayerData user = accountDao.queryForId(uuid.toString());
            if (user == null) {
                user = new SQLitePlayerData();
                user.setUuid(uuid.toString());
                user.setUsername(name);
                user.setCredits(0);
                user.setPurchases(new SerializedMap<>());
                PlayerData.get().put(uuid, user);
            } else {
                PlayerData.get().put(uuid, user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pushData(UUID player) {
        PlayerData playerData = PlayerData.get().get(player);
        try {
            accountDao.createOrUpdate((SQLitePlayerData) playerData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
