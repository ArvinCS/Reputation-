package me.arvin.reputationp.gui;

//import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
//import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.arvin.reputationp.Main;
import me.arvin.reputationp.file.ArvinYML;
import me.arvin.reputationp.utility.CooldownUtil;
import me.arvin.reputationp.utility.Rep;


public class Profile implements ArvinGUI {
	public Long cooldownlike = System.currentTimeMillis() + (long) Main.get().getConfig().getInt("Rep-Cooldown") * 1000;
	private Player player;
	private Player opener;
	public Profile(Player player, Player opener) {
		this.player = player;
		this.opener = opener;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Player getOpener() {
		return this.opener;
	}
	
	public ItemStack Skull(){
		ArrayList<String> lores = new ArrayList<String>();
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setDisplayName(ArvinYML.getYML("gui.yml").getString("Skull.Name").replace("{PLAYER}", getPlayer().getName()));
		for(String lore : ArvinYML.getYML("gui.yml").getStringList("Skull.Lore")) {
			lore = lore.replace("{REP}", String.valueOf(Rep.getReputation(getPlayer()))).replace("{LIKE}", String.valueOf(Rep.getLike(getPlayer()))).replace("{DISLIKE}", String.valueOf(Rep.getDislike(getPlayer()))).replace("{POINT}", String.valueOf(Rep.getPoint(getPlayer()))).replace("{FOLLOWER}", String.valueOf(follower())).replace("{IGNORER}", String.valueOf(ignorer()));
			lores.add(ChatColor.translateAlternateColorCodes('&', lore));
		}
		meta.setLore(lores);
		meta.setOwner(getPlayer().getName());
		
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skull.setItemMeta(meta);
		return skull;
	}
	
	public Integer follower() {
		File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", getPlayer().getUniqueId().toString() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
		return Lang.getStringList("Follower").size();
	}
	
	public Integer ignorer() {
		File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", getPlayer().getUniqueId().toString() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
		return Lang.getStringList("Ignorer").size();
	}
	
	public ItemStack Follow(){
		ArrayList<String> lores = new ArrayList<String>();
		File fileLang = new File(Main.get().getDataFolder() + File.separator + "PlayersData", getPlayer().getUniqueId().toString() + ".yml");
		FileConfiguration Lang = YamlConfiguration.loadConfiguration(fileLang);
		ItemStack clay = null;
		ItemMeta meta = null;
		if (Lang.getStringList("Follower").contains(getOpener().getUniqueId().toString())){
			clay = new ItemStack(Material.STAINED_CLAY, 1,(byte) 13);
			meta = clay.getItemMeta();
		} else if (Lang.getStringList("Ignorer").contains(getOpener().getUniqueId().toString())){
			clay = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
			meta = clay.getItemMeta();
		} else {
			clay = new ItemStack(Material.STAINED_CLAY, 1);
			meta = clay.getItemMeta();
		}
		meta.setDisplayName(ArvinYML.getYML("gui.yml").getString("Follow/IgnoreButton.Name").replace("{PLAYER}", getPlayer().getName()));
		for(String lore : ArvinYML.getYML("gui.yml").getStringList("Follow/IgnoreButton.Lore")) {
			lore = lore.replace("{REP}", String.valueOf(Rep.getReputation(getPlayer()))).replace("{LIKE}", String.valueOf(Rep.getLike(getPlayer()))).replace("{DISLIKE}", String.valueOf(Rep.getDislike(getPlayer()))).replace("{POINT}", String.valueOf(Rep.getPoint(getPlayer()))).replace("{FOLLOWER}", String.valueOf(follower())).replace("{IGNORER}", String.valueOf(ignorer()));
			lores.add(ChatColor.translateAlternateColorCodes('&', lore));
		}
		meta.setLore(lores);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		clay.setItemMeta(meta);
		return clay;
	}

	public ItemStack Reputation(){
		ArrayList<String> lores = new ArrayList<String>();
		ItemStack reputation = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = reputation.getItemMeta();
		meta.setDisplayName(ArvinYML.getYML("gui.yml").getString("Reputation.Name").replace("{PLAYER}", getPlayer().getName()));
		for(String lore : ArvinYML.getYML("gui.yml").getStringList("Reputation.Lore")) {
			lore = lore.replace("{REP}", String.valueOf(Rep.getReputation(getPlayer()))).replace("{LIKE}", String.valueOf(Rep.getLike(getPlayer()))).replace("{DISLIKE}", String.valueOf(Rep.getDislike(getPlayer()))).replace("{POINT}", String.valueOf(Rep.getPoint(getPlayer()))).replace("{FOLLOWER}", String.valueOf(follower())).replace("{IGNORER}", String.valueOf(ignorer()));
			lores.add(ChatColor.translateAlternateColorCodes('&', lore));
		}
		meta.setLore(lores);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		reputation.setItemMeta(meta);
		return reputation;
	}
	
	public ItemStack Point(){
		ArrayList<String> lores = new ArrayList<String>();
		ItemStack point = new ItemStack(Material.EMERALD);
		ItemMeta meta = point.getItemMeta();
		meta.setDisplayName(ArvinYML.getYML("gui.yml").getString("Point.Name").replace("{PLAYER}", getPlayer().getName()));
		for(String lore : ArvinYML.getYML("gui.yml").getStringList("Point.Lore")) {
			lore = lore.replace("{REP}", String.valueOf(Rep.getReputation(getPlayer()))).replace("{POINT}", String.valueOf(Rep.getPoint(getOpener())));
			lores.add(ChatColor.translateAlternateColorCodes('&', lore));
		}
		meta.setLore(lores);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		point.setItemMeta(meta);
		return point;
	}

	public ItemStack like(){
		ArrayList<String> lores = new ArrayList<String>();
		ItemStack like = new ItemStack(Material.WOOL, 1, (byte) 5);
		ItemMeta meta = like.getItemMeta();
		meta.setDisplayName(ArvinYML.getYML("gui.yml").getString("LikeButton.Name").replace("{PLAYER}", getPlayer().getName()));
		for(String lore : ArvinYML.getYML("gui.yml").getStringList("LikeButton.Lore")) {
			lore = lore.replace("{REP}", String.valueOf(Rep.getReputation(getPlayer()))).replace("{LIKE}", String.valueOf(Rep.getLike(getPlayer()))).replace("{DISLIKE}", String.valueOf(Rep.getDislike(getPlayer()))).replace("{POINT}", String.valueOf(Rep.getPoint(getPlayer()))).replace("{FOLLOWER}", String.valueOf(follower())).replace("{IGNORER}", String.valueOf(ignorer()));
			lores.add(ChatColor.translateAlternateColorCodes('&', lore));
		}
		meta.setLore(lores);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		like.setItemMeta(meta);
		return like;
	}
	
	public ItemStack dislike(){
		ArrayList<String> lores = new ArrayList<String>();
		ItemStack dislike = new ItemStack(Material.WOOL, 1, (byte) 14);
		ItemMeta meta = dislike.getItemMeta();
		meta.setDisplayName(ArvinYML.getYML("gui.yml").getString("DislikeButton.Name").replace("{PLAYER}", getPlayer().getName()));
		for(String lore : ArvinYML.getYML("gui.yml").getStringList("DislikeButton.Lore")) {
			lore = lore.replace("{REP}", String.valueOf(Rep.getReputation(getPlayer()))).replace("{LIKE}", String.valueOf(Rep.getLike(getPlayer()))).replace("{DISLIKE}", String.valueOf(Rep.getDislike(getPlayer()))).replace("{POINT}", String.valueOf(Rep.getPoint(getPlayer()))).replace("{FOLLOWER}", String.valueOf(follower())).replace("{IGNORER}", String.valueOf(ignorer()));
			lores.add(ChatColor.translateAlternateColorCodes('&', lore));
		}
		meta.setLore(lores);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		dislike.setItemMeta(meta);
		return dislike;
	}
	
	@Override
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(this, 18, player.getName() + " Reputation");
		inv.setItem(0, Skull());
		inv.setItem(4, Reputation());
		inv.setItem(8, Follow());
		inv.setItem(9, Point());
		inv.setItem(11, like());
		inv.setItem(15, dislike());
		return inv;
	}
	
	@Override
	public void onGUIClick(Player player, int slot, ItemStack clicked, ClickType click) {
		Player target = getPlayer();
		if (clicked.getType() == Material.WOOL){
			if (clicked.getItemMeta().getDisplayName().equals(ArvinYML.getYML("gui.yml").getString("LikeButton.Name").replace("{PLAYER}", target.getName()))){
				if(player.getTicksLived() > ArvinYML.getYML("config.yml").getInt("Min-PlayTime")) {
					if (player.equals(target)){
						player.closeInventory();
						player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-cantlike-yourself"));
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
								String message1 = ((String) ArvinYML.getYML("messages.yml").getString("message-liked"));
								message1 = message1.replace("{USERNAME}", target.getName());
								player.sendMessage(Main.prefixplugin + ChatColor.WHITE + message1);
								try {
									uLang.save(userLang);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-cooldown"));
							}
						} else {
							player.closeInventory();
							player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-nopoint"));
						}
					}
				} else {
					player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-minplaytime"));
				}
			}
			else if (clicked.getItemMeta().getDisplayName().equals(ArvinYML.getYML("gui.yml").getString("DislikeButton.Name").replace("{PLAYER}", target.getName()))){
				if(player.getTicksLived() > ArvinYML.getYML("config.yml").getInt("Min-PlayTime")) {
					if (player.equals(target)){
						player.closeInventory();
						player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-cantdislike-yourself"));
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
								String message1 = ArvinYML.getYML("messages.yml").getString("message-disliked");
								String message2 = message1.replace("{USERNAME}", target.getName());
								player.sendMessage(Main.prefixplugin + ChatColor.WHITE + message2);
								try {
									uLang.save(userLang);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-cooldown"));
							}
						} else {
							player.closeInventory();
							player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-nopoint"));
						}
					}
				} else {
					player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-minplaytime"));
				}
			}
		}
		else if (clicked.getType() == Material.STAINED_CLAY){
			if (clicked.getItemMeta().getDisplayName().equals(ArvinYML.getYML("gui.yml").getString("Follow/IgnoreButton.Name").replace("{PLAYER}", target.getName()))){
				if(player.getTicksLived() > ArvinYML.getYML("config.yml").getInt("Min-PlayTime")) {
					if (player.equals(target)){
						player.closeInventory();
						player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-cantfollow-yourself"));
					} else {
						if (click.isLeftClick() == true){
							player.closeInventory();
							Rep.Follow(player, target);
						} else if (click.isRightClick() == true) {
							player.closeInventory();
							Rep.Ignore(player, target);
						}
					}
				} else {
					player.sendMessage(Main.prefixplugin + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-minplaytime"));
				}
			}
		}
	}

}
