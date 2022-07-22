package me.arvin.reputationp.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.arvin.reputationp.Main;

public class RegisterNameTag implements Listener {
	//static boolean yes = Bukkit.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
	public static void Register(){
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Main.get(), "rep",
				new be.maximvdw.placeholderapi.PlaceholderReplacer(){
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent event){
						Player player = event.getPlayer();
						if (player == null){
							return "Offline Player !";
						}
						return Main.get().getRDatabase().getReputation(player) + "";
					}
			});
			
			System.out.println("[Rep+] Placeholder for MvdwPlaceholderAPI registered");
	}
	
}
