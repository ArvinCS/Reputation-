package me.arvin.reputationp.event;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.arvin.reputationp.Main;
import me.arvin.reputationp.file.ArvinYML;
import me.arvin.reputationp.sql.Errors;
import me.arvin.reputationp.utility.CooldownUtil;
import me.arvin.reputationp.utility.Rep;
import net.md_5.bungee.api.ChatColor;

public class JoinLeave implements Listener {
	private String prefix = ChatColor.GREEN + "[Rep+] ";
	private File msgLang = new File(Main.get().getDataFolder(), "messages.yml");
	private FileConfiguration msg = YamlConfiguration.loadConfiguration(msgLang);
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if (!checkData(e.getPlayer())){
			insertData(e.getPlayer());
		}	
		if (Main.SilentJoinQuit == true){
			Player player = e.getPlayer();
			File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", player.getUniqueId().toString() + ".yml");
			FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
			e.setJoinMessage(null);
			for (Player all : Bukkit.getServer().getOnlinePlayers()){
				if (Lang.getStringList("Follower").contains(all.getUniqueId().toString())){
					String message1 = msg.getString("message-joined");
					String message2 = message1.replace("{USERNAME}", player.getName());
					all.sendMessage(prefix + ChatColor.WHITE + message2);
				}
			}
		}
		
		if(CooldownUtil.isExpired(e.getPlayer(), "daily point")) {
			int dp = ArvinYML.getYML("config.yml").getInt("Daily-Point");
			e.getPlayer().sendMessage(Main.prefixplugin + ArvinYML.getYML("messages.yml").getString("message-dailypoint").replace("{POINT}", String.valueOf(dp)));
			Rep.addPoint(e.getPlayer(), dp);
			CooldownUtil.setCooldown(e.getPlayer(), "daily point", 86400);
		}
		
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		if (Main.SilentJoinQuit == true){
			Player player = event.getPlayer();
			File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", player.getUniqueId().toString() + ".yml");
			FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
			for (Player all : Bukkit.getServer().getOnlinePlayers()){
				if (Lang.getStringList("Follower").contains(all.getUniqueId().toString())){
					event.setQuitMessage(null);
					String message1 = msg.getString("message-leave");
					String message2 = message1.replace("{USERNAME}", player.getName());
					all.sendMessage(prefix + ChatColor.WHITE + message2);
				} else {
					event.setQuitMessage(null);
				}
			}
		}
	}
	
	private boolean checkData(Player player) {
    	String table = "reputation";
    	boolean data = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Main.get().getRDatabase().getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+ player.getUniqueId().toString() + "';");
            rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getString("player") != null){ // Tell database to search for the player you sent into the method. e.g getTokens(sam) It will look for sam.
                	data = true;
                } 
            }
        } catch (SQLException ex) {
            Main.get().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            Main.get().getRDatabase().close(ps,rs);
        }
        return data;
    }
    
    private void insertData(Player player) {
    	String table = "reputation";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Main.get().getRDatabase().getSQLConnection();
        	ps = conn.prepareStatement("INSERT INTO " + table + " VALUES ('" + player.getUniqueId().toString() + "', '0', '0', '0', '0', '', '');");
            ps.executeUpdate();
        } catch (SQLException ex) {
            Main.get().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            Main.get().getRDatabase().close(ps,null);
        }
    }
}
