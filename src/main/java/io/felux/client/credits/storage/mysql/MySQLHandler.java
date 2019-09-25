package io.felux.client.credits.storage.mysql;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import io.felux.client.credits.maven.MavenLibrary;
import io.felux.client.credits.maven.Repository;
import io.felux.client.credits.storage.PlayerData;
import io.felux.client.credits.storage.StorageHandler;
import io.felux.client.credits.util.SerializedMap;

import java.sql.SQLException;
import java.util.UUID;

@MavenLibrary(groupId = "com.github.j256", artifactId = "ormlite-core", version = "4.43", repo = @Repository(url = "https://jitpack.io"))
@MavenLibrary(groupId = "com.github.j256", artifactId = "ormlite-jdbc", version = "4.43", repo = @Repository(url = "https://jitpack.io"))
public class MySQLHandler implements StorageHandler {

    private ConnectionSource connectionSource;
    private Dao<MySQLPlayerData, String> accountDao;

    public MySQLHandler(String prefix, String host, int port, String database, String username, String password) {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            accountDao = DaoManager.createDao(connectionSource, MySQLPlayerData.class);
            TableUtils.createTableIfNotExists(connectionSource, MySQLPlayerData.class);
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pullData(String name, UUID uuid) {
        try {
            MySQLPlayerData user = accountDao.queryForId(uuid.toString());
            if (user == null) {
                user = new MySQLPlayerData();
                user.setUuid(uuid.toString());
                user.setUsername(name);
                user.setCredits(0);
                user.setPurchases(new SerializedMap<>());
                PlayerData.get().put(uuid, user);
                //accountDao.create(user);
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
            accountDao.createOrUpdate((MySQLPlayerData) playerData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
