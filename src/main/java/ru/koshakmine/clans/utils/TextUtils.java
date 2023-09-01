package ru.koshakmine.clans.utils;

public class TextUtils {
    
    public static boolean checkClanName(String string) {
        String pattern = "^(?=.*[A-Za-zА-Яа-я])[A-Za-zА-Яа-я0-9]{4,20}$";
        return string.matches(pattern);
    }

    public static boolean checkClanTag(String string) {
        String pattern = "^(?![0-9])[A-Za-z][A-Za-z0-9]{3,19}$";
        return string.matches(pattern);
    }
}
