package ru.koshakmine.clans.forms;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.Dropdown;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.SelectableElement;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ru.koshakmine.clans.Clans;
import ru.koshakmine.clans.clan.Clan;
import ru.koshakmine.clans.clan.User;
import ru.koshakmine.clans.clan.enums.Access;
import ru.koshakmine.clans.clan.enums.Roles;
import ru.koshakmine.clans.managers.ClanManager;
import ru.koshakmine.clans.utils.TextUtils;

public class Forms {
    
    private Clans main;

    public Forms(Clans main) {
        this.main = main;
    }

    public void sendMainForm(Player player) {
        SimpleForm form = new SimpleForm("Кланы");
        if (!main.getUDatabase().isInClan(player.getUniqueId())) {
            
            form.addButton("Приглашения в клан §c[" + 1 + "]", (pl, b) -> sendCreateClanForm(pl));

            form.addButton("Создать клан", (pl, b) -> sendCreateClanForm(pl))
                .addButton("Найти клан", (pl, b) -> sendFindClanForm(pl))
                .addButton("Топы", (pl, b) -> sendCreateClanForm(pl));
        } else {
            Clan clan = main.getUDatabase().getUserClan(player.getUniqueId());
            ClanManager manager = new ClanManager(clan.getId(), main);
            form.addContent(
                    "§rКлан: " + clan.getName() + "     §8[" + clan.getId() + "]\n" +
                    "§rМесто в топе: §e#" + 1 + "\n" +
                    "§rУчастников: §7(§a" +  manager.getMembers().size() + "§7/§c25§7)\n\n" +

                    "§rСписок участников:\n"
                );
                for (User user : manager.getMembers()) {
                    IPlayer target = main.getServer().getOfflinePlayer(user.getUuid());
                    form.addContent(
                        target.isOnline() ? "  §7[§a✔§7] " : "  §7[§cX§7] " +
                        target.getName() + " " + 
                        (user.getRole() != Roles.MEMBER ? "" : user.getRole().getName())
                    );
                }
                form.addButton("Участники", (pl, b) -> sendClanMembersForm(pl))
                    .addButton("Хранилище\n§cДоступно с 5 уровня клана", null)
                    .addButton("Клановая война\n§cДоступно с 7 уровня клана", null);
        }

        form.send(player);
    }

    public void sendClanMembersForm(Player player) {
        SimpleForm form = new SimpleForm("Участники");
        Clan clan = main.getUDatabase().getUserClan(player.getUniqueId());
        ClanManager manager = new ClanManager(clan.getId(), main);
        
        for (User user : manager.getMembers()) {
            IPlayer target = main.getServer().getOfflinePlayer(user.getUuid());
            if (user.getUuid() != player.getUniqueId()) {
                if (manager.getMemberRole(player) == Roles.OFFICER || manager.getOwner().getUuid() == player.getUniqueId()) {
                    if (clan.getOwner() != user.getUuid()) {
                        form.addButton(target.getName() + "\n§7[" + user.getRole().getName() + "]", (pl, b) -> sendEditMemberForm(pl, user.getUuid()));
                    } else {
                        form.addButton(target.getName() + "\n§7[" + user.getRole().getName() + "]", null);
                    }
                } else {
                    form.addButton(target.getName() + "\n§7[" + user.getRole().getName() + "]", null);
                }
            }
        }
        form.send(player);
    }

    public void sendEditMemberForm(Player player, UUID target) {
        IPlayer IPlayer = main.getServer().getOfflinePlayer(target);
        SimpleForm form = new SimpleForm("Управление участником '" + IPlayer.getName() + "'");

        Clan clan = main.getUDatabase().getUserClan(player.getUniqueId());
        ClanManager manager = new ClanManager(clan.getId(), main);

        form.addButton("Изменить роль", (pl, b) -> {
                this.sendEditMemberRoleForm(pl, target);
            })
            .addButton("§cПередать владение кланом", (pl, b) -> {
                manager.setOwner(target);
            })
            .addButton("§cВыгнать с клана", (pl, b) -> {
                manager.removeMember(target);
            });

        form.send(player);
    }

    public void sendEditMemberRoleForm(Player player, UUID target) {
        IPlayer IPlayer = main.getServer().getOfflinePlayer(target);
        CustomForm form = new CustomForm("Управление участником '" + IPlayer.getName() + "'");

        Clan clan = main.getUDatabase().getUserClan(player.getUniqueId());
        ClanManager manager = new ClanManager(clan.getId(), main);
        
        List<SelectableElement> roles = Roles.getAllRolesExcept(manager.getMemberRole(target));
        form.addElement("role", new Dropdown("§l§e>§r§f Выберите новую роль для игрока " + IPlayer.getName(), roles));

        form.setHandler((pl, response) -> {
            manager.changeMemberRole(target, Roles.parseRole(response.getDropdown("role").getValue().getValue(Integer.class)));
        });
        form.send(player);
    }

    public void sendCreateClanForm(Player player) {
        CustomForm form = new CustomForm("Создание клана");

        List<SelectableElement> elements = Arrays.asList(
            new SelectableElement(Access.PUBLIC.getName(), Access.PUBLIC.getAccess()),
            new SelectableElement(Access.INVITE.getName(), Access.INVITE.getAccess()),
            new SelectableElement(Access.PRIVATE.getName(), Access.PRIVATE.getAccess())
        );

        form.addElement("name", Input.builder()
                .setName("§l§e>§r§f Название клана")    
                .setPlaceholder("(не длинее 16 символов)")
                .build())
            .addElement("tag", Input.builder()
                .setName("§l§e>§r§f Тег клана (id)")    
                .setPlaceholder("(не длинее 8 символов)")
                .build())

            .addElement(
                "§eПубличный §7-§f Игрокам разрешено вступать без запроса на приглашение.\n" + 
                "§eПо приглашению §7-§f Игроки должны отправить запрос на вступление.\n" + 
                "§eПриватный §7-§f Игрокам разрешено вступать без приглашения от владельца.\n")

            .addElement("access", new Dropdown("§l§e>§r§f Настройки приватности клана", elements));

        form.setHandler((pl, response) -> {
            String tag = response.getInput("tag").getValue();
            String name = response.getInput("name").getValue();

            if (!TextUtils.checkClanTag(tag) || !TextUtils.checkClanName(name)) {
                pl.sendMessage("Неподходящее название или тег клана.");
                return;
            }

            ClanManager manager = new ClanManager(tag, main);
            SelectableElement access = response.getDropdown("access").getValue();
            manager.create(name, pl.getUniqueId(), Access.parseAccess(access.getValue(Integer.class)));
        });

        form.send(player);
    }

    public void sendFindClanForm(Player player) {
        SimpleForm form = new SimpleForm("Кланы");
        for (Clan clan : main.getDatabase().getAllClans()) {
            if (clan.getAccess() != Access.PRIVATE) {
                if (clan.getAccess() == Access.PUBLIC) {
                    form.addButton(clan.getName(), null);
                } else {
                    form.addButton(clan.getName() + "\n§7Только по заявке", null);
                }
            }
        }
        form.send(player);
    }
}
