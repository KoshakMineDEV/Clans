package ru.koshakmine.clans.managers;

import ru.koshakmine.clans.clan.Clan;
import ru.koshakmine.clans.clan.Invite;

import cn.nukkit.Player;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class InviteManager {

    @Getter private List<Invite> invites = new ArrayList<>();

    public void createInvite(Invite invite) {
        invites.add(invite);
    }

    public void removeInvite(Invite invite) {
        invites.remove(invite);
    }

    public boolean inviteExists(Invite invite) {
        return invites.contains(invite);
    }

    public List<Invite> getInvites(Player player) {
        List<Invite> list = new ArrayList<>();
        if (invites.size() > 0) {
            for (Invite invite : invites) {
                if (invite.getPlayer().equals(player.getUniqueId())) {
                    list.add(invite);
                }
            }
        }
        return list;
    } 

    public List<Invite> getInvites(Clan clan) {
        List<Invite> list = new ArrayList<>();
        if (invites.size() > 0) {
            for (Invite invite : invites) {
                if (invite.getClan().equals(clan)) {
                    list.add(invite);
                }
            }
        }
        return list;
    } 
}
