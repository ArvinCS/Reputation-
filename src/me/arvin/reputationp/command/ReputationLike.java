package me.arvin.reputationp.command;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.arvin.reputationp.Main;
import me.arvin.reputationp.utility.CooldownUtil;
import me.arvin.reputationp.utility.Message;
import me.arvin.reputationp.utility.Rep;

public class ReputationLike implements CommandExecutor {
	String prefix = Main.prefixplugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player = null;
		if(sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("[Rep+] This command isnt for console !");
			return false;
		}
		
		if (cmd.getName().equals("reputationlike")){
			if (args.length == 1){
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target != null){
					if (player.getName().equals(target.getName())){
						player.closeInventory();
						player.sendMessage(prefix + ChatColor.WHITE + Message.read("message-cantlike-yourself"));
					} else {
						File userLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", player.getName() + ".yml");
						FileConfiguration uLang = YamlConfiguration.loadConfiguration(userLang);
						if (Rep.getPoint(player) > 0){
							player.closeInventory();
							String cooldownname = target + "-Cooldown";
							if (CooldownUtil.isExpired(player,cooldownname) == true){
								Rep.addLike(target, 1);
								Rep.addReputation(target, 1);
								Rep.rmvPoint(player, 1);
								String message1 = ((String) Message.read("message-liked")).replace("{USERNAME}", target.getName());
								player.sendMessage(prefix + ChatColor.WHITE + message1);
								try {
									uLang.set(cooldownname, System.currentTimeMillis() + (long) (24 * 60 * 1000));
									uLang.save(userLang);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + Message.read("message-cooldown"));
							}
						} else {
							player.closeInventory();
							player.sendMessage(prefix + ChatColor.WHITE + Message.read("message-nopoint"));
						}
					}
				}
			} else {
				player.sendMessage(prefix + ChatColor.WHITE + Message.read("message-wrongcmd-replike"));
			}
		}
		
		return true;
	}
}
