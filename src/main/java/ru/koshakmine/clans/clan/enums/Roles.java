package ru.koshakmine.clans.clan.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ru.contentforge.formconstructor.form.element.SelectableElement;

@Getter
public enum Roles {
    
    VISITOR(-1, "Посетитель"),
    MEMBER(0, "Участник"),
    OFFICER(1, "Офицер"),
    OWNER(2, "Владелец");

    private Integer role;
    private String name;

    Roles(int role, String name) {
        this.role = role;
        this.name = name;
    }

    public static Roles parseRole(int i) {
        Roles role = MEMBER;
        switch(i) {
            case -1:
                role = VISITOR;
            case 0:
                role = MEMBER;
            case 1:
                role = OFFICER;
            case 2: 
                role = OWNER;
        }
        return role;
    }

    public static List<SelectableElement> getAllRolesExcept(Roles role) {
        List<SelectableElement> list = new ArrayList<>();
        for (Roles roles : Roles.values()) {
            if (roles != role || role != Roles.OWNER) list.add(new SelectableElement(roles.getName(), roles.getRole()));
        }
        return list;
    }
}
