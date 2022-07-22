package me.arvin.reputationp.event;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.arvin.reputationp.Main;
import me.clip.placeholderapi.external.EZPlaceholderHook;

@SuppressWarnings("deprecation")
public class PlaceholderRegister extends EZPlaceholderHook {
	
	@SuppressWarnings("unused")
	private Plugin ourPlugins;
	public PlaceholderRegister(Plugin ourPlugin){
		super(ourPlugin, "reputation");
		this.ourPlugins = ourPlugin;
		System.out.println("Placeholder for PlaceholderAPI registered");
	}
	
	@Override
	public String onPlaceholderRequest(Player player, String text){
		if (text.equals("reputation")){
			return String.valueOf(Main.get().getRDatabase().getReputation(player));
		}
		if (text.equals("like")){
			return String.valueOf(Main.get().getRDatabase().getLike(player));
		}
		if (text.equals("dislike")){
			return String.valueOf(Main.get().getRDatabase().getDislike(player));
		}
		return text;
		
	}
}
