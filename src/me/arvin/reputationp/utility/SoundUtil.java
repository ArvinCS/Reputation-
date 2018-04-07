package me.arvin.reputationp.utility;

import org.bukkit.entity.Player;

import me.arvin.reputationp.Main;
public class SoundUtil {
	public static void onBeam(Player player){
		if (Main.ver.get("Version") == 8 || Main.ver.get("Version") == 7){
			player.playSound(player.getLocation(), "minecraft:random.orb", 1F, 1F);
		} else if (Main.ver.get("Version") == 9 || Main.ver.get("Version") == 10 || Main.ver.get("Version") == 11 || Main.ver.get("Version") == 12){
			player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1F, 1F);
		}
	}
	
	public static void onPling(Player player){
		player.playSound(player.getLocation(), "minecraft:note.pling", 1F, 1F);
	}
	
	public static void onTravel(Player player){
		player.playSound(player.getLocation(), "minecraft:portal.travel", 1F, 1F);
	}
}
