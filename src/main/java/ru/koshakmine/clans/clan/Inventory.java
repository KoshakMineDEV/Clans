package ru.koshakmine.clans.clan;

import cn.nukkit.item.Item;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

import ru.koshakmine.clans.utils.InventoryUtils;

@Getter
public class Inventory {

    private Clan clan;
    private Map<Integer, Item> contents = new HashMap<>();

    public Inventory(Clan clan, Map<Integer, Item> contents) {
        this.clan = clan;
        this.contents = contents;
    }

    public String toString() {
        return InventoryUtils.toString(contents);
    }
}
