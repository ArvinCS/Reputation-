package me.arvin.reputationp.event;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.arvin.reputationp.Main;

public class Chat implements Listener {
	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", player.getUniqueId().toString() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang); 
		for (Player all : Bukkit.getServer().getOnlinePlayers()){
			if (Lang.getString("Ignorer") != null){
				if (Lang.getStringList("Ignorer").contains(all.getUniqueId().toString())){
					e.getRecipients().remove(all);
				}
			}
		}
	}
	
	@EventHandler
	public void PrefixChat(AsyncPlayerChatEvent e) {
		if (Main.prefixchat == true){
			Player player = e.getPlayer();
			String rep = null;
			String prefix = Main.prefix;
			rep = Main.get().getRDatabase().getReputation(player).toString();
			if (prefix.contains("{REP}")){
				prefix = prefix.replace("{REP}", rep);
				prefix = ChatColor.translateAlternateColorCodes('&', prefix);
				//if (player.getDisplayName().contains(prefix)) return;
			}
			if (!player.getDisplayName().contains(prefix))player.setDisplayName(prefix + player.getDisplayName());
		}
	}
	
	@EventHandler
	public void typeShow(AsyncPlayerChatEvent e){
		if (Main.ChatF){
			String message = e.getMessage();
			if (message.contains("!rep")){
				String rep = Main.get().getRDatabase().getReputation(e.getPlayer()).toString();
				message = message.replace("!rep", rep + " Reputation");
			}
			if (message.contains("!like")){
				String like = Main.get().getRDatabase().getLike(e.getPlayer()).toString();
				message = message.replace("!like", like + " Likes");
			}
			if (message.contains("!dislike")){
				String dislike = Main.get().getRDatabase().getDislike(e.getPlayer()).toString();
				message = message.replace("!dislike", dislike + " Dislikes");
			}
			e.setMessage(message);
		}
	}
}
