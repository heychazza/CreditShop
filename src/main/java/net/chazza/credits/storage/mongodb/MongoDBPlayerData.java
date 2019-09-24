package net.chazza.credits.storage.mongodb;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.PrePersist;
import net.chazza.credits.storage.PlayerData;
import net.chazza.credits.util.SerializedMap;
import org.bson.types.ObjectId;

import java.util.Date;

@Entity(value = "player", noClassnameStored = true)
public class MongoDBPlayerData implements PlayerData {
    @Id
    private ObjectId id;
    private Date creationDate;
    private Date lastChange;

    @Indexed
    private String uuid;
    private String username;
    private SerializedMap<String, Integer> purchases;
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

    @Override
    public SerializedMap<String, Integer> getPurchases() {
        return purchases;
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

    @Override
    public void setPurchases(SerializedMap<String, Integer> purchases) {
        this.purchases = purchases;
    }

    public ObjectId getId() {
        return this.id;
    }

    public long getCreationDate() {
        return this.creationDate.getTime();
    }

    public long getLastChange() {
        return this.lastChange.getTime();
    }

    @PrePersist
    public void prePersist() {
        this.creationDate = ((this.creationDate == null) ? new Date() : this.creationDate);
        this.lastChange = ((this.lastChange == null) ? this.creationDate : new Date());
    }
}
