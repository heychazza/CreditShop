package net.chazza.credits.storage;

import java.util.ArrayList;
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

    ArrayList<UUID> getFriends();

    void addFriend(UUID uuid);

    void removeFriend(UUID uuid);

    void setFriends(ArrayList<UUID> friends);
}
