package net.chazza.credits.storage.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import net.chazza.credits.storage.PlayerData;
import net.chazza.credits.storage.StorageHandler;

import java.util.ArrayList;
import java.util.UUID;

public class MongoDBHandler implements StorageHandler {

    private MongoClient client;
    private Datastore datastore;

    public MongoDBHandler(String prefix, String host, int port, String database, String username, String password) {
        String newPrefix = prefix.isEmpty() ? "" : "+" + prefix;
        String auth = (username.isEmpty() && password.isEmpty()) ? "" : username + ":" + password + "@";

        String connection = "mongodb" + newPrefix + "://" + auth + host + ":" + port + "/" + database + "?ssl=true&replicaSet=Cluster0-shard-0&authSource=" + username + "&retryWrites=true&w=majority?";

        client = new MongoClient(new MongoClientURI(connection));
        client.setWriteConcern(WriteConcern.SAFE);

        datastore = new Morphia().createDatastore(client, database);
        datastore.ensureIndexes();
        datastore.ensureCaps();
    }

    @Override
    public void pullData(String name, UUID uuid) {
        MongoDBPlayerData playerData = datastore.createQuery(MongoDBPlayerData.class).filter("uuid", uuid.toString()).get();

        if (playerData == null) {
            MongoDBPlayerData newPlayerData = new MongoDBPlayerData();
            newPlayerData.setUuid(uuid.toString());
            newPlayerData.setUsername(name);
            newPlayerData.setFriends(new ArrayList<>());
            playerData = newPlayerData;
        }

        PlayerData.get().put(uuid, playerData);
    }

    @Override
    public void pushData(UUID player) {
        PlayerData playerData = PlayerData.get().get(player);
        datastore.save((MongoDBPlayerData) playerData);
    }
}