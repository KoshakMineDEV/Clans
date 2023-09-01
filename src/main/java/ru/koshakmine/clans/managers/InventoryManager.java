package ru.koshakmine.clans.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ru.koshakmine.clans.Clans;
import ru.koshakmine.clans.clan.Clan;
import ru.koshakmine.clans.clan.Inventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import me.iwareq.fakeinventories.CustomInventory;

public class InventoryManager {

    private Clan clan;

    private static Map<Clan, Map<Integer, Item>> contents = new HashMap<>();

    public InventoryManager(ClanManager manager) {
        this.clan = manager.getClan();
    }

    public Map<Integer, Item> getContents() {
        return contents.get(clan);
    }

    public void openClanInventory(Player player, Clan clan) {
        CustomInventory inventory = new CustomInventory(InventoryType.CHEST, "Хранилище клана");
        
        inventory.setDefaultItemHandler((item, event) -> {
            // event.setCancelled(true);
        
            Player source = event.getTransaction().getSource();
            
            (new Timer()).schedule(new TimerTask() {
                public void run() {
                    source.getEnderChestInventory().setContents(inventory.getContents());
                    for (Player viewer : inventory.getViewers()) {
                        openClanInventory(viewer, clan);
                    }
                }
            }, 1);
        });

        inventory.setItem(5, Item.getBlock(Item.BARRIER)
                .setCustomName("Этот слот пока что недоступен"), (item, event) -> {
            event.setCancelled(true);
        
            Player target = event.getTransaction().getSource();
            target.sendMessage("§cЭтот слот пока что недоступен.\n§7Чем выше уровень вашего клана тем больше слотов хранилища вам доступно.");
        
            target.removeWindow(inventory);
        });

        player.addWindow(inventory);
    }

    public static void loadInventories(Clans main) {
        if (main.getIDatabase().isInventoriesExists()) {
            main.getIDatabase().getAllInventories().forEach(i -> {
                contents.put(i.getClan(), i.getContents());
            });
        }
    }

    public static void saveInventories(Clans main) {
        contents.forEach((c, m) -> {
            if (!main.getIDatabase().isInventoriesExists(c.getId())) {
                main.getIDatabase().saveInventory(new Inventory(c, m));
            } else {
                main.getIDatabase().updateInventory(new Inventory(c, m));
            }
        });
    }
}
