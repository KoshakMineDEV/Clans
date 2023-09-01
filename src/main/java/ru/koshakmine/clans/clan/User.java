package ru.koshakmine.clans.clan;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import ru.koshakmine.clans.clan.enums.Roles;

@Getter
@Setter 
public class User {

    private String clanID;
    private UUID uuid;
    private Roles role;

    public User(String clanID, UUID uuid, Roles role) {
        this.clanID = clanID;
        this.uuid = uuid;
        this.role = role;
    }
}
