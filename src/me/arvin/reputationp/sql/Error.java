package me.arvin.reputationp.sql;

import java.sql.SQLException;
import java.util.logging.Level;

import me.arvin.reputationp.Main;

public class Error {
    public static void execute(Main plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);  
    }
    public static void close(Main plugin, SQLException ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}