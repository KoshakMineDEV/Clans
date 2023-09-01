package ru.koshakmine.clans.database;

import ru.koshakmine.clans.Clans;
import ru.koshakmine.clans.clan.Inventory;
import ru.koshakmine.clans.utils.InventoryUtils;

import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.TaskHandler;

import me.hteppl.data.database.SQLiteDatabase;
import org.sql2o.Connection;

import java.sql.ResultSet;
import java.util.List;

public class InventoryDatabase extends SQLiteDatabase {
    
    private Clans main;

    public InventoryDatabase(Clans main) {
        super("plugins/" + main.getName(), "database");
        this.executeScheme("CREATE TABLE if not exists `Inventories` ( `clanID` TEXT NOT NULL UNIQUE , `items` TEXT NOT NULL );");
        this.main = main;
    }

    public TaskHandler saveInventory(Inventory inventory) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String insertQuery =
                    "INSERT INTO `Inventories`(`clanID`, `items`) VALUES (:clanID, :items);";

                try (Connection con = openConnection()) {
                    con.createQuery(insertQuery)
                        .addParameter("clanID", inventory.getClan().getId())
                        .addParameter("items", inventory.toString())
                        .executeUpdate();
                }
            }
        });
    }

    public TaskHandler deleteInventory(String id) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String insertQuery =
                    "DELETE FROM 'Inventories' WHERE clanID = :clanID;";

                try (Connection con = openConnection()) {
                    con.createQuery(insertQuery)
                        .addParameter("clanID", id)
                        .executeUpdate();
                }
            }
        });
    }

    public TaskHandler updateInventory(Inventory inventory) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String query = 
                    "UPDATE 'Inventories' SET clanID = :clanID, items = :items;";
                try (Connection connection = openConnection()) {
                    connection.createQuery(query)
                        .addParameter("clanID", inventory.getClan().getId())
                        .addParameter("name", inventory.toString())
                        .executeUpdate();   
                }
            }
        });
    }

    public Inventory getInventory(String id) {
        try (Connection con = openConnection()) {
            final String query = "SELECT * FROM `Inventories` WHERE `clanID`= :clanID;";
        
            return con.createQuery(query)
                .addParameter("clanID", id)
                .executeAndFetch((ResultSet rs) -> {
                    Inventory inventory = new Inventory(
                        main.getDatabase().getClan(rs.getString("clanID")), 
                        InventoryUtils.fromString(rs.getString("items"))
                    );
                    return inventory;
                })
                .get(0);
        } 
    }


    public List<Inventory> getAllInventories() {
        try (Connection con = openConnection()) {
            final String query = "SELECT * FROM `Inventories`;";
        
            return con.createQuery(query)
            .executeAndFetch((ResultSet rs) -> {
                Inventory inventory = new Inventory(
                    main.getDatabase().getClan(rs.getString("clanID")), 
                    InventoryUtils.fromString(rs.getString("items"))
                );
                return inventory;
            });
        } 
    }

    public boolean isInventoriesExists() {
        try (Connection con = openConnection()) {
            String query = "SELECT COUNT(*) FROM Inventories;";
            int count = con.createQuery(query).executeScalar(Integer.class);
            return count > 0;
        }
    }

    public boolean isInventoriesExists(String id) {
        try (Connection con = openConnection()) {
            String query = "SELECT COUNT(*) FROM Inventories WHERE clanID = :clanID;";
            int count = con.createQuery(query)
                    .addParameter("clanID", id)
                    .executeScalar(Integer.class);
            return count > 0;
        }
    }
}
