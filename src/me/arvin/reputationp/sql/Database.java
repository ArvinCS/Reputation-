package me.arvin.reputationp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.arvin.reputationp.Main;
import me.arvin.reputationp.sql.Error;
import me.arvin.reputationp.sql.Errors;


public abstract class Database {
    public JavaPlugin plugin;
    public Connection connection;

    private String table = "reputation";
    public Database(JavaPlugin instance){
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + ";");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);
     
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }
    
    public Integer getLike(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int rep = 0;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+player.getUniqueId().toString()+"';");
     
            rs = ps.executeQuery();
            while(rs.next()){
            	if (rs.getString("player") != null){
	            	rep = rs.getInt("likes");
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,rs);
        }
        return rep;  
    }
    
    public void addDislike(Player player, int like) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	int total = getDislike(player) + like;
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET dislikes = '"+ total +"' WHERE player = '" + player.getUniqueId().toString() + "';");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,null);
        }
    }
    
    public void removeDislike(Player player, int like) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	int total = getDislike(player) - like;
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET dislikes = '"+ total +"' WHERE player = '" + player.getUniqueId().toString() + "';");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,null);
        }
    }
    
    public Integer getDislike(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int rep = 0;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+player.getUniqueId().toString()+"';");
     
            rs = ps.executeQuery();
            while(rs.next()){
            	if (!(rs.getString("player") == null)){
	            	rep = rs.getInt("dislikes");
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,rs);
        }
        return rep;  
    }
    

    public void addLike(Player player, int like) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	int total = getLike(player) + like;
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET likes = '"+ total +"' WHERE player='" + player.getUniqueId().toString() + "';");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,null);
        }
    }
    
    public void removeLike(Player player, int like) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	int total = getLike(player) - like;
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET likes = '"+ total +"' WHERE player = '" + player.getUniqueId().toString() + "';");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,null);
        }    
    }

    public Integer getReputation(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int rep = 0;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+ player.getUniqueId().toString()+"';");
     
            rs = ps.executeQuery();
            while(rs.next()){
            	if (rs.getString("player") != null){
	                rep = rs.getInt("reputation");
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,rs);
        }
        return rep;  
    }
    
    public Integer getPoint(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int rep = 0;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+ player.getUniqueId().toString() +"';");
     
            rs = ps.executeQuery();
            while(rs.next()){
            	if (rs.getString("player") != null){
	            	rep = rs.getInt("point");
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,rs);
        }
        return rep;  
    }
    
    public void addPoint(Player player, int like) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	int total = getPoint(player) + like;
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET point = '"+ total +"' WHERE player = '" + player.getUniqueId().toString() + "';");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,null);
        }
    }
    
    public void removePoint(Player player, int like) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	int total = getPoint(player) - like;
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET point = '"+ total +"' WHERE player = '" + player.getUniqueId().toString() + "';");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,null);
        }
    }
    
    public void setPoint(Player player, int like) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	int total = like;
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET point = '"+ total +"' WHERE player = '" + player.getUniqueId().toString() + "';");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,null);
        }
    }
    
    public void addReputation(Player player, int like) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	int total = getReputation(player) + like;
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET reputation = '"+ total +"' WHERE player = '" + player.getUniqueId().toString() + "';");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,null);
        }    
    }
    public void removeReputation(Player player, int like) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	int total = getReputation(player) - like;
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET reputation = '"+ total +"' WHERE player = '" + player.getUniqueId().toString() + "';");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps,null);
        }
    }
    
    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }

}