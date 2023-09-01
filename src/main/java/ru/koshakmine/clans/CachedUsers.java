package ru.koshakmine.clans;

import ru.koshakmine.clans.clan.Clan;
import ru.koshakmine.clans.clan.User;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

public class CachedUsers {
    
    @Getter private static HashMap<UUID, User> cachedUsers = new HashMap<>();

    public void add(User user) {
        cachedUsers.put(user.getUuid(), user);
    }

    public void remove(User user) {
        cachedUsers.remove(user.getUuid());    
    }
    
    public boolean exists(UUID uuid) {
        return cachedUsers.containsKey(uuid);
    }

    public User getUser(UUID uuid) {
        return cachedUsers.get(uuid);
    }

    public Clan getUserClan(UUID uuid) {
        return CachedClans.getCachedClans().get(cachedUsers.get(uuid).getClanID());
    }

    public Clan getUserClan(User user) {
        return this.getUserClan(user.getUuid());
    }

    public void loadUsers(Clans main) {
        main.getUDatabase().getAllMembers().forEach(u -> {
            cachedUsers.put(u.getUuid(), u);
        });
    }

    public void saveUsers(Clans main) {
        main.getUDatabase().getAllMembers().forEach(u -> {
           if (exists(u.getUuid())) main.getUDatabase().deleteMember(u.getUuid());
        });
        cachedUsers.forEach((uuid, u) -> {
            if (!main.getUDatabase().isInClan(uuid)) {
                main.getUDatabase().addMember(u);
            } else {
                main.getUDatabase().updateUser(u);
            }
        });
    }
}
