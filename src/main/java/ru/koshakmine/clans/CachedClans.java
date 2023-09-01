package ru.koshakmine.clans;

import ru.koshakmine.clans.clan.Clan;
import lombok.Getter;

import java.util.HashMap;

public class CachedClans {
    
    @Getter private static HashMap<String, Clan> cachedClans = new HashMap<>();

    public void add(Clan clan) {
        cachedClans.put(clan.getId(), clan);
    }

    public void remove(Clan clan) {
        cachedClans.remove(clan.getId());    
    }
    
    public boolean exists(String id) {
        return cachedClans.containsKey(id);
    }

    public Clan getClan(String id) {
        return cachedClans.get(id);
    }

    public void loadClans(Clans main) {
        main.getDatabase().getAllClans().forEach(c -> {
            cachedClans.put(c.getId(), c);
        });
    }

    public void saveClans(Clans main) {
        main.getDatabase().getAllClans().forEach(c -> {
            if (exists(c.getId())) main.getDatabase().deleteClan(c.getId());
        });
        cachedClans.forEach((id, c) -> {
            if (!main.getDatabase().isClanExists(id)) {
                main.getDatabase().createClan(c);
            } else {
                main.getDatabase().createClan(c);
            }
        });
    }
}
