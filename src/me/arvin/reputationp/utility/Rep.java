package me.arvin.reputationp.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.arvin.reputationp.Main;
import me.arvin.reputationp.file.ArvinYML;

public class Rep {
	private static String prefix = Main.prefixplugin;
	public static void addLike(Player player, int like){
		Main.get().getRDatabase().addLike(player, like);
		for(String command : ArvinYML.getYML("config.yml").getStringList("Rep.Good.Command")){
			String cmd = command;
			if(cmd != null)Main.get().getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
		}
	}
	public static void rmvLike(Player player, int like){
		Main.get().getRDatabase().removeLike(player, like);
	}
	public static int getLike(Player player){
		return Main.get().getRDatabase().getLike(player);
	}
	
	public static void addDislike(Player player, int like){
		Main.get().getRDatabase().addDislike(player, like);
		for(String command : ArvinYML.getYML("config.yml").getStringList("Rep.Bad.Command")){
			String cmd = command;
			if(cmd != null)Main.get().getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
		}
	}
	public static void rmvDislike(Player player, int like){
		Main.get().getRDatabase().removeDislike(player, like);
	}
	public static int getDislike(Player player){
		return Main.get().getRDatabase().getDislike(player);
	}
	
	public static void addPoint(Player player, int like){
		Main.get().getRDatabase().addPoint(player, like);
	}
	public static void rmvPoint(Player player, int like){
		Main.get().getRDatabase().removePoint(player, like);
	}
	public static int getPoint(Player player){
		return Main.get().getRDatabase().getPoint(player);
	}
	public static void setPoint(Player player, int like){
		Main.get().getRDatabase().setPoint(player, like);
	}
	
	public static void addReputation(Player player, int like){
		Main.get().getRDatabase().addReputation(player, like);
	}
	public static void rmvReputation(Player player, int like){
		Main.get().getRDatabase().removeReputation(player, like);
	}
	public static int getReputation(Player player){
		return Main.get().getRDatabase().getReputation(player);
	}
	
	public static void Follow(Player p, Player t){
		File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", t.getUniqueId().toString() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
		if (Lang.getStringList("Follower").size() >= 1){
			List<String> ignorer = Lang.getStringList("Follower");
			List<String> follower = Lang.getStringList("Ignorer");
			if (follower.contains(p.getUniqueId().toString())){
				String message1 = Message.read("message-already-follow");
				String message2 = message1.replace("{USERNAME}", t.getName());
				p.sendMessage(prefix + ChatColor.WHITE + message2);
				return;
			} else {
				follower.add(p.getUniqueId().toString());
				Lang.set("Follower", follower);
				if (ignorer.contains(p.getUniqueId().toString())){
					ignorer.remove(p.getUniqueId().toString());
					Lang.set("Ignorer", ignorer);
				}
				try {
					Lang.save(fileLang);
					String message1 = Message.read("message-followed");
					String message2 = message1.replace("{USERNAME}", t.getName());
					p.sendMessage(prefix + ChatColor.WHITE + message2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			ArrayList<String> follower = new ArrayList<String>();
			follower.add(p.getUniqueId().toString());
			Lang.set("Follower", follower);
			List<String> ignorer = Lang.getStringList("Ignorer");
			if (ignorer.contains(p.getUniqueId().toString())){
				ignorer.remove(p.getUniqueId().toString());
				Lang.set("Ignorer", ignorer);
			}
			try {
				Lang.save(fileLang);
				String message1 = Message.read("message-followed");
				String message2 = message1.replace("{USERNAME}", t.getName());
				p.sendMessage(prefix + ChatColor.WHITE + message2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(String command : ArvinYML.getYML("config.yml").getStringList("Rep.Follow.Command")){
			String cmd = command;
			if(cmd != null)Main.get().getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
		}
	}
	
	public static void Unfollow(Player p, Player t){
		File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", t.getUniqueId().toString() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
		if (Lang.getStringList("Follower").size() >= 1){
			List<String> ignorer = Lang.getStringList("Follower");
			List<String> follower = Lang.getStringList("Ignorer");
			if (!follower.contains(p.getUniqueId().toString())){
				String message1 = Message.read("message-already-unfollow");
				String message2 = message1.replace("{USERNAME}", t.getName());
				p.sendMessage(prefix + ChatColor.WHITE + message2);
			} else {
				follower.remove(p.getUniqueId().toString());
				Lang.set("Follower", follower);
				if (ignorer.contains(p.getUniqueId().toString())){
					ignorer.remove(p.getUniqueId().toString());
					Lang.set("Ignorer", ignorer);
				}
				try {
					Lang.save(fileLang);
					String message1 = Message.read("message-unfollowed");
					String message2 = message1.replace("{USERNAME}", t.getName());
					p.sendMessage(prefix + ChatColor.WHITE + message2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			String message1 = Message.read("message-already-unfollow").replace("{USERNAME}", t.getName());
			p.sendMessage(prefix + ChatColor.WHITE + message1);
		}
	}
	
	public static void DailyPoint(Player player) throws IOException{
		if (CooldownUtil.isExpired(player, "Cooldown-Point") == true){
			CooldownUtil.setCooldown(player, "Cooldown-Point", 86400000);
			String message1 = Message.read("message-dailypoint").replace("{POINT}", ArvinYML.getYML("config.yml").getInt("Daily-Point") + "");
			player.sendMessage(ChatColor.GREEN + "[Rep+] " + ChatColor.WHITE + message1);
			Rep.setPoint(player, ArvinYML.getYML("config.yml").getInt("Daily-Point"));
		} else {
			return;
		}
	}
	

	public static void Ignore(Player p, Player t){
		File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", t.getUniqueId().toString() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
		if (Lang.getStringList("Ignorer").size() >= 1){
			List<String> ignorer = Lang.getStringList("Ignorer");
			List<String> follower = Lang.getStringList("Follower");
			if (ignorer.contains(p.getUniqueId().toString())){
				String message1 = Message.read("message-already-ignore").replace("{USERNAME}", t.getName());
				p.sendMessage(prefix + ChatColor.WHITE + message1);
				return;
			} else {
				ignorer.add(p.getUniqueId().toString());
				Lang.set("Ignorer", ignorer);
				if (follower.contains(p.getUniqueId().toString())){
					follower.remove(p.getUniqueId().toString());
					Lang.set("Follower", follower);
				}
				try {
					Lang.save(fileLang);
					String message1 = Message.read("message-ignored").replace("{USERNAME}", t.getName());
					p.sendMessage(prefix + ChatColor.WHITE + message1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			ArrayList<String> ignorer = new ArrayList<String>();
			ignorer.add(p.getUniqueId().toString());
			Lang.set("Ignorer", ignorer);
			List<String> follower = Lang.getStringList("Follower");
			if (follower.contains(p.getUniqueId().toString())){
				follower.remove(p.getUniqueId().toString());
				Lang.set("Follower", follower);
			}
			try {
				Lang.save(fileLang);
				String message1 = Message.read("message-ignored").replace("{USERNAME}", t.getName());
				p.sendMessage(prefix + ChatColor.WHITE + message1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(String command : ArvinYML.getYML("config.yml").getStringList("Rep.Ignore.Command")){
			String cmd = command;
			if(cmd != null)Main.get().getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
		}
	}
	
	public static void Unignore(Player p, Player t){
		File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", t.getName() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
		if (Lang.getStringList("Ignorer").size() >= 1){
			List<String> ignorer = Lang.getStringList("Ignorer");
			if (!ignorer.contains(p.getUniqueId().toString())){
				String message1 = Message.read("message-already-unblock").replace("{USERNAME}", t.getName());
				p.sendMessage(prefix + ChatColor.WHITE + message1);
			} else {
				ignorer.remove(p.getUniqueId().toString());
				Lang.set("Ignorer", ignorer);
				try {
					Lang.save(fileLang);
					String message1 = Message.read("message-unblocked").replace("{USERNAME}", t.getName());
					p.sendMessage(prefix + ChatColor.WHITE + message1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			String message1 = Message.read("message-already-unblock").replace("{USERNAME}", t.getName());
			p.sendMessage(prefix + ChatColor.WHITE + message1);
		}
	}
}
