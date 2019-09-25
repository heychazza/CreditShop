package io.felux.client.credits.storage;

import io.felux.client.credits.util.SerializedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface PlayerData {

    Map<UUID, PlayerData> users = new HashMap<>();

    static Map<UUID, PlayerData> get() {
        return users;
    }

    static PlayerData get(UUID uuid) {
        return users.get(uuid);
    }

    String getUuid();

    String getUsername();

    SerializedMap<String, Integer> getPurchases();

    void setUsername(String username);

    void addCredits(int amount);

    void removeCredits(int amount);

    int getCredits();

    void setCredits(int amount);

    void setPurchases(SerializedMap<String, Integer> purchases);
}
