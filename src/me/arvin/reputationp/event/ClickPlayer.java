package me.arvin.reputationp.event;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.arvin.reputationp.Main;
import me.arvin.reputationp.gui.Profile;
import me.arvin.reputationp.utility.ActionBar;
import me.arvin.reputationp.utility.Citizens;
import me.arvin.reputationp.utility.Message;
import me.arvin.reputationp.utility.SoundUtil;

public class ClickPlayer implements Listener {
	HashMap<Player,Integer> totalclick = new HashMap<Player,Integer>();
	HashMap<Player,Player> clickedplayer = new HashMap<Player,Player>();
	@EventHandler
	public void rightRep(PlayerInteractEntityEvent event){
		if(Main.RightClickGUI == true){
			Player clicker = event.getPlayer();
			if (event.getRightClicked() instanceof Player){
				Player clicked = (Player) event.getRightClicked();
				if (Citizens.checkNPC(clicked) == false){
					if (clickedplayer.get(clicker) == clicked && clickedplayer.get(clicker) != null){
						if (totalclick.get(clicker) != null){
							clickedplayer.remove(clicker);
							totalclick.remove(clicker);
							clicker.openInventory(new Profile(clicked,clicker).getInventory());
							SoundUtil.onBeam(clicker);
						}
					} else {//if (clickedplayer.get(clicker) == null){
						clickedplayer.put(clicker, clicked);
						totalclick.put(clicker, 1);
						deleteRep(clicker);
						String teks = Message.read("message-rightclickgui");
						teks = teks.replace("{CLICKED}", clicked.getName());
						ActionBar.sendActionBarTime(clicker, ChatColor.GREEN + "" + ChatColor.BOLD + "[Rep] " + ChatColor.BLUE + teks, 60);
					}
				}
			}
		}
	}
	
	public void deleteRep(Player player){
		Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
			@Override
	        public void run(){
				if (totalclick.get(player) == null) return;
	        	if (totalclick.get(player) <= 1){
	        		clickedplayer.remove(player);
	        		totalclick.remove(player);
	        	}
			}
		}, 60L);
	}
}
