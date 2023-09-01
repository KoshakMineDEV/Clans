package ru.koshakmine.clans.database;

import ru.koshakmine.clans.Clans;
import ru.koshakmine.clans.clan.Clan;
import ru.koshakmine.clans.clan.enums.Access;
import ru.koshakmine.clans.clan.enums.Roles;

import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.TaskHandler;

import me.hteppl.data.database.SQLiteDatabase;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

import org.sql2o.Connection;

/* 
 *  Управление базой данных непосредственно кланов
 */

public class Database extends SQLiteDatabase {
    
    private Clans main;

    public Database(Clans main) {
        super("plugins/" + main.getName(), "database");

                executeScheme("CREATE TABLE if not exists `Clans` " + 
                    "( `clanID` TEXT NOT NULL UNIQUE , `name` TEXT NOT NULL UNIQUE, " +
                    "`access` TEXT NOT NULL, `defaultRole` TEXT NOT NULL, `owner` TEXT NOT NULL UNIQUE, " + 
                    "`money` INT NOT NULL, `kills` INT NOT NULL, `exp` INT NOT NULL, `rating` INT NOT NULL );");
        this.main = main;
    }

    public TaskHandler createClan(Clan clan) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String insertQuery =
                    "INSERT INTO `Clans`(`clanID`, `name`, `owner`, `access`, `defaultRole`, `money`, `kills`, `exp`, `rating`) " +
                    "VALUES (:clanID, :name, :owner, :access, :defaultRole, :money, :kills, :exp, :rating);";

                try (Connection con = openConnection()) {
                    con.createQuery(insertQuery)
                        .addParameter("clanID", clan.getId())
                        .addParameter("name", clan.getName())
                        .addParameter("owner", clan.getOwner())
                        .addParameter("access", clan.getAccess())
                        .addParameter("defaultRole", clan.getDefaultRole())
                        .addParameter("money", clan.getMoney())
                        .addParameter("kills", clan.getKills())
                        .addParameter("exp", clan.getExp())
                        .addParameter("rating", clan.getRating())
                        .executeUpdate();
                }
            }
        });
    }

    public TaskHandler deleteClan(String id) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String insertQuery =
                    "DELETE FROM 'Clans' WHERE clanID = :clanID;";

                try (Connection con = openConnection()) {
                    con.createQuery(insertQuery)
                        .addParameter("clanID", id)
                        .executeUpdate();
                }
            }
        });
    }

    public TaskHandler deleteClanByName(String name) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String insertQuery =
                    "DELETE FROM 'Clans' WHERE name = :name;";

                try (Connection con = openConnection()) {
                    con.createQuery(insertQuery)
                        .addParameter("name", name)
                        .executeUpdate();
                }
            }
        });
    }

    public TaskHandler updateClan(Clan clan) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String query = 
                    "UPDATE 'Clans' SET clanID = :clanID, name = :name, owner = :owner, access, = :access" + 
                    "defaultRole = :defaultRole, money = :money, kills = :kills, exp = :exp, rating = :rating;";
                try (Connection connection = openConnection()) {
                    connection.createQuery(query)
                        .addParameter("clanID", clan.getId())
                        .addParameter("name", clan.getName())
                        .addParameter("owner", clan.getOwner())
                        .addParameter("access", clan.getAccess())
                        .addParameter("defaultRole", clan.getDefaultRole())
                        .addParameter("money", clan.getMoney())
                        .addParameter("kills", clan.getKills())
                        .addParameter("exp", clan.getExp())
                        .addParameter("rating", clan.getRating())
                        .executeUpdate();   
                }
            }
        });
    }

    public Clan getClan(String id) {
        try (Connection con = openConnection()) {
            final String query = "SELECT * FROM `Clans` WHERE `clanID`= :clanID;";
        
            return con.createQuery(query)
                .addParameter("clanID", id)
                .executeAndFetch((ResultSet rs) -> {
                    Clan clan = new Clan(
                        rs.getString("clanID"), 
                        rs.getString("name"), 
                        UUID.fromString(rs.getString("owner")), 
                        Access.valueOf(rs.getString("access")), 
                        Roles.valueOf(rs.getString("defaultRole")), 
                        rs.getInt("money"), 
                        rs.getInt("kills"), 
                        rs.getInt("exp"), 
                        rs.getInt("rating"));
                    return clan;
                })
                .get(0);
        } 
    }

    public Clan getClanByName(String name) {
        try (Connection con = openConnection()) {
            final String query = "SELECT * FROM `Clans` WHERE `name`= :name;";
        
            return con.createQuery(query)
                .addParameter("name", name)
                .executeAndFetch((ResultSet rs) -> {
                    Clan clan = new Clan(
                        rs.getString("clanID"), 
                        rs.getString("name"), 
                        UUID.fromString(rs.getString("owner")), 
                        Access.valueOf(rs.getString("access")), 
                        Roles.valueOf(rs.getString("defaultRole")), 
                        rs.getInt("money"), 
                        rs.getInt("kills"), 
                        rs.getInt("exp"), 
                        rs.getInt("rating"));
                    return clan;
                })
                .get(0);
        } 
    }

    public List<Clan> getAllClans() {
        try (Connection con = openConnection()) {
            final String query = "SELECT * FROM `Clans`;";
        
            return con.createQuery(query)
            .executeAndFetch((ResultSet rs) -> {
                Clan clan = new Clan(
                    rs.getString("clanID"), 
                    rs.getString("name"), 
                    UUID.fromString(rs.getString("owner")), 
                    Access.valueOf(rs.getString("access")), 
                    Roles.valueOf(rs.getString("defaultRole")), 
                    rs.getInt("money"), 
                    rs.getInt("kills"), 
                    rs.getInt("exp"), 
                    rs.getInt("rating"));
                return clan;
            });
        } 
    }

    public boolean isClanExists(String id) {
        try (Connection con = openConnection()) {
            String query = "SELECT COUNT(*) FROM Clans WHERE clanID = :clanID";
            int count = con.createQuery(query)
                    .addParameter("clanID", id)
                    .executeScalar(Integer.class);
            return count > 0;
        }
    }

    public boolean isClanNameExists(String name) {
        try (Connection con = openConnection()) {
            String query = "SELECT COUNT(*) FROM Clans WHERE name = :name";
            int count = con.createQuery(query)
                    .addParameter("name", name)
                    .executeScalar(Integer.class);
            return count > 0;
        }
    }
}
