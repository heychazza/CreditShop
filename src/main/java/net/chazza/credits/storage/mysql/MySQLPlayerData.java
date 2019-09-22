package net.chazza.credits.storage.mysql;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.chazza.credits.storage.PlayerData;

@DatabaseTable(tableName = "players")
public class MySQLPlayerData implements PlayerData {

    @DatabaseField(id = true, useGetSet = true)
    private String uuid;

    @DatabaseField(useGetSet = true)
    private String username;

    @DatabaseField(useGetSet = true)
    private int credits;

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
    public void addCredits(int amount) {
        credits += amount;
    }

    @Override
    public void removeCredits(int amount) {
        credits -= amount;
    }

    @Override
    public int getCredits() {
        return credits;
    }

    @Override
    public void setCredits(int amount) {
        credits = amount;
    }
}
