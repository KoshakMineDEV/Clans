package ru.koshakmine.clans.clan;

import java.util.UUID;

import ru.koshakmine.clans.clan.enums.Access;
import ru.koshakmine.clans.clan.enums.Roles;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Clan {

    private String id;
    private String name;
    private UUID owner;
    private Access access;
    private Roles defaultRole;
    private Integer money;
    private Integer kills;
    private Integer exp;
    private Integer rating;

    public Clan(String id, String name, UUID owner, Access access, Roles defaultRole) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.access = access;
        this.money = 0;
        this.kills = 0;
        this.exp = 0;
        this.rating = 0;
        this.defaultRole = defaultRole;
    }

    public Clan(String id, String name, UUID owner, Access access, Roles defaultRole, int money, int kills) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.access = access;
        this.money = money;
        this.kills = kills;
        this.exp = 0;
        this.rating = 0;
        this.defaultRole = defaultRole;
    }

    public Clan(String id, String name, UUID owner, Access access, Roles defaultRole, int money, int kills, int exp, int rating) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.access = access;
        this.money = money;
        this.kills = kills;
        this.exp = exp;
        this.rating = rating;
        this.defaultRole = defaultRole;
    }
}
