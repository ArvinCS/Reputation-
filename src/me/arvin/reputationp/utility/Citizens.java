package me.arvin.reputationp.utility;

import org.bukkit.entity.Player;

import me.arvin.reputationp.Main;

public class Citizens {
	public static boolean checkNPC(final Player player){
		if (Main.get().getServer().getPluginManager().isPluginEnabled("Citizens")){//.isPluginEnabled("Citizens")){//.getPluginManager().isPluginEnabled("Citizens")){
			if (player.hasMetadata("NPC")){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
