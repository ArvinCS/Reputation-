package me.arvin.reputationp.gui;

import java.io.File;
//import java.util.List;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
//import org.bukkit.Material;
//import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.arvin.lib.config.ArvinConfig;
import me.arvin.reputationp.Main;
import me.arvin.reputationp.file.ArvinYML;
import me.arvin.reputationp.utility.CooldownUtil;
import me.arvin.reputationp.utility.Rep;

public class InventoryClick implements Listener {
	private String prefix = ChatColor.GREEN + "[Rep+] ";
	private ArvinConfig msg = ArvinYML.getYML("messages.yml");
	private Long cooldownlike = System.currentTimeMillis() + (long) Main.get().getConfig().getInt("Rep-Cooldown") * 1000;//  (24 * 60 * 1000);
	private static String get(String text) {
		ArvinConfig config = ArvinYML.getYML("gui.yml");
		text = config.getString(text);
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked(); // The player that clicked the item
		ItemStack clicked = event.getCurrentItem(); // The item that was clicked
		Inventory inventory = event.getInventory();
		if (inventory.getName().contains(" Reputation")) { // The inventory is our custom Inventory
			if (clicked != null){
				if (clicked.getType() != null){
					String name = inventory.getName();
					String targetname = name.replace(" Reputation", "");
					Player target = Bukkit.getServer().getPlayer(targetname);
					if (clicked.getType() == Material.WOOL){
						event.setCancelled(true);
						if (clicked.getItemMeta().getDisplayName().equals(get("LikeButton.Name").replace("{PLAYER}", targetname))){
							//name.replace("A", "");
							if(player.getTicksLived() > ArvinYML.getYML("config.yml").getInt("Min-PlayTime")) {
								if (player.getName().equals(targetname)){
									player.closeInventory();
									player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-cantlike-yourself"));
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
											if(Main.get().getConfig().getInt("Rep-Cooldown") == -1) uLang.set(cooldownname, -1);
											else uLang.set(cooldownname, cooldownlike);
											String message1 = ((String) msg.get("message-liked"));
											message1 = message1.replace("{USERNAME}", targetname);
											player.sendMessage(prefix + ChatColor.WHITE + message1);
											try {
												uLang.save(userLang);
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										} else {
											player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-cooldown"));
										}
									} else {
										player.closeInventory();
										player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-nopoint"));
									}
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-minplaytime"));
							}
						}
						else if (clicked.getItemMeta().getDisplayName().equals(get("DislikeButton.Name").replace("{PLAYER}", targetname))){
							if(player.getTicksLived() > ArvinYML.getYML("config.yml").getInt("Min-PlayTime")) {
								if (player.getName().equals(targetname)){
									player.closeInventory();
									player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-cantdislike-yourself"));
								} else {
									File userLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", player.getName() + ".yml");
									FileConfiguration uLang = YamlConfiguration.loadConfiguration(userLang);
									if (Rep.getPoint(player) > 0){
										player.closeInventory();
										String cooldownname = target + "-Cooldown";
										if (CooldownUtil.isExpired(player,cooldownname) == true){
											Rep.addDislike(target, 1);
											Rep.rmvReputation(target, 1);
											Rep.rmvPoint(player, 1);
											if(Main.get().getConfig().getInt("Rep-Cooldown") == -1) uLang.set(cooldownname, -1);
											else uLang.set(cooldownname, cooldownlike);
											String message1 = msg.getString("message-disliked");
											String message2 = message1.replace("{USERNAME}", targetname);
											player.sendMessage(prefix + ChatColor.WHITE + message2);
											try {
												uLang.save(userLang);
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										} else {
											player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-cooldown"));
										}
									} else {
										player.closeInventory();
										player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-nopoint"));
									}
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-minplaytime"));
							}
						}
					}
					else if (clicked.getType() == Material.STAINED_CLAY){
						event.setCancelled(true);
						if (clicked.getItemMeta().getDisplayName().equals(get("Follow/IgnoreButton.Name").replace("{PLAYER}", targetname))){
							if(player.getTicksLived() > ArvinYML.getYML("config.yml").getInt("Min-PlayTime")) {
								if (player.equals(target)){
									player.closeInventory();
									player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-cantfollow-yourself"));
								} else {
									//File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", target + ".yml");
									//FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
									//List<String> follower = Lang.getStringList("Follower");
									//List<String> ignorer = Lang.getStringList("Ignorer");
									if (event.getClick().isLeftClick() == true){
										player.closeInventory();
										Rep.Follow(player, target);
									} else if (event.getClick().isRightClick() == true) {
										player.closeInventory();
										Rep.Ignore(player, target);
									}
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + msg.getString("message-minplaytime"));
							}
						}
					} else {
						event.setCancelled(true);
					}
				} else {
					return;
				}
			} else {
				return;
			}
		}
	}
}
