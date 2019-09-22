package net.chazza.credits.storage;

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

    void setUsername(String username);

    int credits = 0;

    void addCredits(int amount);

    void removeCredits(int amount);

    int getCredits();

    void setCredits(int amount);
}
