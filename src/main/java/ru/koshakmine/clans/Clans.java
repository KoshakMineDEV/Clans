package ru.koshakmine.clans;

import cn.nukkit.plugin.PluginBase;
import ru.koshakmine.clans.commands.ClanCommand;
import ru.koshakmine.clans.database.Database;
import ru.koshakmine.clans.database.InventoryDatabase;
import ru.koshakmine.clans.database.UDatabse;
import ru.koshakmine.clans.forms.Forms;
import ru.koshakmine.clans.listeners.EventListener;
import ru.koshakmine.clans.managers.InventoryManager;
import ru.koshakmine.clans.managers.InviteManager;

import lombok.Getter;

@Getter
public class Clans extends PluginBase {

    @Getter private static Clans instance; 

    private Database database;
    private UDatabse uDatabase;
    private InventoryDatabase iDatabase;

    private InviteManager inviteManager;
    
    private CachedClans cachedClans;
    private CachedUsers cachedUsers;

    private Forms forms;

    @Override
    public void onLoad() {
        instance = this;

        this.database = new Database(this);
        this.uDatabase = new UDatabse(this);
        this.iDatabase = new InventoryDatabase(this);
    }

    @Override
    public void onEnable() {
        this.getDataFolder().mkdir();
    
        this.inviteManager = new InviteManager();    
        this.forms = new Forms(this);
        
        this.cachedClans = new CachedClans();
        this.cachedUsers = new CachedUsers();

        InventoryManager.loadInventories(this);
        cachedClans.loadClans(this);
        cachedUsers.loadUsers(this);
        this.register();
    }

    @Override
    public void onDisable() {
        InventoryManager.saveInventories(this);
        cachedClans.saveClans(this);
        cachedUsers.saveUsers(this);
    }

    private void register() {
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getServer().getCommandMap().register("Clans", new ClanCommand(this));
    }
}