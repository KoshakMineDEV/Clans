package ru.koshakmine.clans.clan.enums;

import lombok.Getter;

@Getter
public enum Access {
    
    PUBLIC(0, "Публичный"),
    INVITE(1, "По приглашению"),
    PRIVATE(2, "Приватный");

    private Integer access;
    private String name;

    Access(int access, String name) {
        this.access = access;
        this.name = name;
    }

    public static Access parseAccess(int i) {
        Access access = PRIVATE;
        switch(i) {
            case 0:
                access = PUBLIC;
            case 1:
                access = INVITE;
            case 2: 
                access = PRIVATE;
        }
        return access;
    }
}
