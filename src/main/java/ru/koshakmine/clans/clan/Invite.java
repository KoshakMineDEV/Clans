package ru.koshakmine.clans.clan;

import lombok.Getter;
import java.util.UUID;

@Getter
public class Invite {

    private Clan clan;
    private UUID player;

    public Invite(Clan clan, UUID player) {
        this.clan = clan;
        this.player = player;
    }
}
