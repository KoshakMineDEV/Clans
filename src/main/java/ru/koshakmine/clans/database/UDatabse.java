package ru.koshakmine.clans.database;

import ru.koshakmine.clans.Clans;
import ru.koshakmine.clans.clan.Clan;
import ru.koshakmine.clans.clan.User;
import ru.koshakmine.clans.clan.enums.Roles;

import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.TaskHandler;

import me.hteppl.data.database.SQLiteDatabase;
import org.sql2o.Connection;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

/* 
 *  Управление базой данных игроков
 */

public class UDatabse extends SQLiteDatabase {
    
    private Clans main;

    public UDatabse(Clans main) {
        super("plugins/" + main.getName(), "database");

                executeScheme("CREATE TABLE if not exists `Users` ( `clanID` TEXT NOT NULL, `uuid` TEXT NOT NULL UNIQUE, `role` TEXT NOT NULL );");

        this.main = main;
    }

    public TaskHandler addMember(User user) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String query =
                    "INSERT INTO 'Users'('clanID', 'uuid', 'role') VALUES (:clanID, :uuid, :role);";

                try (Connection con = openConnection()) {
                    con.createQuery(query)
                        .addParameter("clanID", user.getClanID())
                        .addParameter("uuid", user.getUuid())
                        .addParameter("role", user.getRole())
                        .executeUpdate();
                }
            }
        });
    }

    public TaskHandler deleteMember(UUID uuid) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String insertQuery =
                    "DELETE FROM 'Users' WHERE uuid = :uuid;";

                try (Connection con = openConnection()) {
                    con.createQuery(insertQuery)
                        .addParameter("uuid", uuid)
                        .executeUpdate();
                }
            }
        });
    }

    public TaskHandler deleteAllMembers(Clan clan) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String insertQuery =
                    "DELETE FROM 'Users' WHERE clanID = :clanID;";

                try (Connection con = openConnection()) {
                    con.createQuery(insertQuery)
                        .addParameter("clanID", clan.getId())
                        .executeUpdate();
                }
            }
        });
    }

    public TaskHandler updateUser(User user) {
        return main.getServer().getScheduler().scheduleAsyncTask(main, new AsyncTask() {
            @Override
            public void onRun() {
                final String query = "UPDATE 'Users' SET clanID = :clanID, uuid = :uuid, role = :role;";
                try (Connection connection = openConnection()) {
                    connection.createQuery(query)
                        .addParameter("clanID", user.getClanID())
                        .addParameter("uuid", user.getUuid())
                        .addParameter("role", user.getRole())
                        .executeUpdate();   
                }
            }
        });
    }

    public List<User> getClanMembers(Clan clan) {
        try (Connection con = openConnection()) {
            final String query = "SELECT * FROM `Users` WHERE `clanID`= :clanID;";
        
            return con.createQuery(query)
                .addParameter("clanID", clan.getId())
                .executeAndFetch((ResultSet rs) -> {
                    User user = new User(rs.getString("clanID"), UUID.fromString(rs.getString("uuid")), Roles.valueOf(rs.getString("role")));
                    return user;
                });
        } 
    }

    public User getUser(UUID uuid) {
        try (Connection con = openConnection()) {
            final String query = "SELECT * FROM `Users` WHERE `uuid`= :uuid;";
        
            return con.createQuery(query)
                .addParameter("uuid", uuid)
                .executeAndFetch((ResultSet rs) -> {
                    User user = new User(rs.getString("clanID"), UUID.fromString(rs.getString("uuid")), Roles.valueOf(rs.getString("role")));
                    return user;
                }).get(0);
        } 
    }

    public Clan getUserClan(UUID uuid) {
        try (Connection con = openConnection()) {
            final String query = "SELECT * FROM `Users` WHERE `uuid`= :uuid;";
        
            String clanID = con.createQuery(query)
                .addParameter("uuid", uuid)
                .executeAndFetch((ResultSet rs) -> {
                    return rs.getString("clanID");
                })
                .get(0);

            return main.getDatabase().getClan(clanID);
        } 
    }

    public List<User> getAllMembers() {
        try (Connection con = openConnection()) {
            final String query = "SELECT * FROM `Users`;";
        
            return con.createQuery(query)
            .executeAndFetch((ResultSet rs) -> {
                User user = new User(rs.getString("clanID"), UUID.fromString(rs.getString("uuid")), Roles.valueOf(rs.getString("role")));
                return user;
            });
        } 
    }

    public boolean isInClan(String uuid, Clan clan) {
        try (Connection con = openConnection()) {
            String query = "SELECT COUNT(*) FROM Users WHERE uuid = :uuid AND clanID = :clanID";
            int count = con.createQuery(query)
                    .addParameter("uuid", uuid)
                    .addParameter("clanID", clan.getId())
                    .executeScalar(Integer.class);
            return count > 0;
        }
    }

    public boolean isInClan(UUID uuid) {
        try (Connection con = openConnection()) {
            String query = "SELECT COUNT(*) FROM Users WHERE uuid = :uuid";
            int count = con.createQuery(query)
                    .addParameter("uuid", uuid)
                    .executeScalar(Integer.class);
            return count > 0;
        }
    }
}
