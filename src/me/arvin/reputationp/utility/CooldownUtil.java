package me.arvin.reputationp.utility;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.arvin.reputationp.Main;

public class CooldownUtil {
	public static final boolean isExpired(Player player, String cooldownname){
		File uLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", player.getUniqueId().toString() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(uLang);
		if (Lang.getInt(cooldownname) == 0 || Lang.get(cooldownname) == null || uLang.exists() == false){
			return true;
		} else if (Lang.getInt(cooldownname) == -1) {
			return false;
		} else {
			final long expired = Lang.getLong(cooldownname);
			final long time = System.currentTimeMillis();
			
			if (time < expired){
				return false;
			} else {
				try {
					Lang.set(cooldownname, 0);
					Lang.save(uLang);
				} catch (IOException e) {
					player.sendMessage("EROR !");
					e.printStackTrace();
				}
				return true;
			}
		}
	}
	
	public static final void setCooldown(Player player, String cooldown, final long duration){
		File uLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", player.getUniqueId().toString() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(uLang);
		final long durasi = duration * 1000;
		final long expired = System.currentTimeMillis() + durasi;
		try {
			Lang.set(cooldown, expired);
			Lang.save(uLang);
		} catch (IOException e) {
			player.sendMessage("ERROR");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
