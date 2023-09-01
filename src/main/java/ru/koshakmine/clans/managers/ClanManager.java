package ru.koshakmine.clans.managers;

import java.util.List;
import java.util.UUID;

import ru.koshakmine.clans.Clans;
import ru.koshakmine.clans.clan.Clan;
import ru.koshakmine.clans.clan.User;
import ru.koshakmine.clans.clan.enums.Access;
import ru.koshakmine.clans.clan.enums.Roles;

import cn.nukkit.Player;
import lombok.Getter;

public class ClanManager {
    
    private Clans main;
    
    @Getter private String id;
    @Getter private Clan clan;
    @Getter private User owner;
    
    @Getter private InventoryManager inventoryManager;

    public ClanManager(String id, Clans main) {
        this.id = id;
        this.main = main;

        Clan retrievedClan = main.getDatabase().isClanExists(id) ? main.getDatabase().getClan(id) : null;
        if (retrievedClan != null) {
            this.clan = retrievedClan;
            this.owner = main.getUDatabase().getUser(clan.getOwner());
        }
        this.inventoryManager = new InventoryManager(this);
    }

    public ClanManager(Clan clan, Clans main) {
        this.id = clan.getId();
        this.main = main;

        this.clan = clan;
        this.owner = main.getUDatabase().getUser(clan.getOwner());
    }

    public void create(String name, UUID owner, Access access) {
        Roles defaultRole = access != Access.PUBLIC ? Roles.MEMBER : Roles.VISITOR; 
        main.getDatabase().createClan(new Clan(id, name, owner, access, defaultRole));
        main.getCachedClans().add(clan);
        this.addMember(owner, Roles.OWNER);
    }

    public void remove() {
        this.removeAllMembers();
        main.getDatabase().deleteClan(id);
        main.getCachedClans().remove(clan);
    }

    public List<User> getMembers() {
        return main.getUDatabase().getClanMembers(clan);
    }

    public void setOwner(UUID newOwner) {
        this.changeMemberRole(clan.getOwner(), Roles.OFFICER);
        this.changeMemberRole(newOwner, Roles.OWNER);
    }

    public void addMember(Player player) {
        main.getUDatabase().addMember(new User(id, player.getUniqueId(), clan.getDefaultRole()));
    }

    public void addMember(UUID uuid) {
        main.getUDatabase().addMember(new User(id, uuid, clan.getDefaultRole()));
    }

    public void addMember(Player player, Roles role) {
        main.getUDatabase().addMember(new User(id, player.getUniqueId(), role));
    }

    public void addMember(UUID uuid, Roles role) {
        main.getUDatabase().addMember(new User(id, uuid, role));
    }

    public void removeMember(Player player) {
        main.getUDatabase().deleteMember(player.getUniqueId());
    }

    public void removeMember(UUID uuid) {
        main.getUDatabase().deleteMember(uuid);
    }

    public void removeAllMembers() {
        main.getUDatabase().deleteAllMembers(clan);
    }

    public Roles getMemberRole(Player player) {
        return main.getUDatabase().getUser(player.getUniqueId()).getRole();
    }

    public Roles getMemberRole(UUID uuid) {
        return main.getUDatabase().getUser(uuid).getRole();
    }

    public User getMember(Player player) {
        return main.getUDatabase().getUser(player.getUniqueId());
    }

    public User getMember(UUID uuid) {
        return main.getUDatabase().getUser(uuid);
    }

    public void changeId(String id) {
        main.getDatabase().updateClan(new Clan(
            id, 
            clan.getName(), 
            clan.getOwner(), 
            clan.getAccess(),
            clan.getDefaultRole(),
            clan.getMoney(), 
            clan.getKills(), 
            clan.getExp(), 
            clan.getRating()
        ));
    }

    public void changeName(String name) {
        main.getDatabase().updateClan(new Clan(
            clan.getId(), 
            name, 
            clan.getOwner(), 
            clan.getAccess(),
            clan.getDefaultRole(),
            clan.getMoney(), 
            clan.getKills(), 
            clan.getExp(), 
            clan.getRating()
        ));
    }

    public void changeAccess(Access access) {
        main.getDatabase().updateClan(new Clan(
            clan.getId(), 
            clan.getName(), 
            clan.getOwner(), 
            access,
            clan.getDefaultRole(),
            clan.getMoney(), 
            clan.getKills(), 
            clan.getExp(), 
            clan.getRating()
        ));
    }

    public void changeMemberRole(UUID uuid, Roles role) {
        if (role == Roles.OWNER) {
            this.setOwner(uuid);
        } else {
            main.getUDatabase().updateUser(new User(id, uuid, role));
        }
    }
}
