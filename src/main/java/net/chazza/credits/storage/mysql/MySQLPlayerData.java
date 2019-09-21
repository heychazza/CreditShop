package net.chazza.credits.storage.mysql;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.chazza.credits.storage.PlayerData;

import java.util.ArrayList;
import java.util.UUID;

@DatabaseTable(tableName = "players")
public class MySQLPlayerData implements PlayerData {

    @DatabaseField(id = true, useGetSet = true)
    private String uuid;

    @DatabaseField(useGetSet = true)
    private String username;

    @DatabaseField(useGetSet = true, dataType = DataType.SERIALIZABLE)
    private ArrayList<UUID> friends;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public ArrayList<UUID> getFriends() {
        return friends;
    }

    @Override
    public void addFriend(UUID uuid) {
        friends.add(uuid);
    }

    @Override
    public void removeFriend(UUID uuid) {
        friends.remove(uuid);

    }

    @Override
    public void setFriends(ArrayList<UUID> friends) {
        this.friends = friends;
    }


}
