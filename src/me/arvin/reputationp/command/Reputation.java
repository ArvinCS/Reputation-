package me.arvin.reputationp.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.arvin.reputationp.Main;
import me.arvin.reputationp.file.ArvinYML;
import me.arvin.reputationp.gui.Profile;
import me.arvin.reputationp.utility.GeneralUtil;
import me.arvin.reputationp.utility.JSONChat;
import me.arvin.reputationp.utility.Message;
import me.arvin.reputationp.utility.MetadataUtil;
import me.arvin.reputationp.utility.Rep;
import me.arvin.reputationp.utility.SoundUtil;

public class Reputation implements CommandExecutor {
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
		
		if (cmd.getName().equals("reputation")){
			if (args.length < 1){
				player.openInventory(new Profile(player,player).getInventory());
				SoundUtil.onBeam(player);
			} else if (args.length == 1){
				if (args[0].equals("add")){
					if (player.hasPermission("reputation.admin")){
						player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
					} else {
						player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-nopermission"));
					}
				} else if (args[0].equals("remove")){
					if (player.hasPermission("reputation.admin")){
						player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
					} else {
						player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-nopermission"));
					}
				} else if (args[0].equals("request")){
					if (args.length == 1){
						if (MetadataUtil.isExpired(player, "rep-request")){
							MetadataUtil.setCooldown(player, "rep-request", Main.CooldownRequest);
							for (Player all : Bukkit.getOnlinePlayers()){
								player.chat(Message.read("message-requestrep"));
								JSONChat.Chat(all, "Click Like Button to Like", ChatColor.GREEN + "" + ChatColor.BOLD + "[Like]", ChatColor.GREEN + "Click to Like !", "/replike " + player.getName());
								JSONChat.Chat(all, "Click Dislike Button to Dislike", ChatColor.RED + "" + ChatColor.BOLD + "[Dislike]", ChatColor.GREEN + "Click to Dislike !", "/repdislike " + player.getName());
							}
						} else {
							player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-cooldown-request"));
						}
					} else {
						player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationrequest"));
					}
				} else if (args[0].equals("help")){
					if (args.length == 1){
						if (player.hasPermission("reputation.admin")){
							messageHelp(player);
						} else {
							player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-nopermission"));
						}
					} else {
						player.sendMessage(prefix + ChatColor.WHITE + "Correct command is : /rep help !");
					}
				} else if (args[0].equals("reload")){
					if (args.length == 1){
						if (player.hasPermission("reputation.admin")){
							Main.checkSettings();
							player.sendMessage(prefix + "Reloading Config...");
						} else {
							player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-nopermission"));
						}
					} else {
						player.sendMessage(prefix + ChatColor.WHITE + "Correct command is : /rep reload !");
					}
				} else {
					Player target = Bukkit.getServer().getPlayer(args[0]);
					if (target != null){
						//Profile.Point(player);
						player.openInventory(new Profile(target,player).getInventory());
						SoundUtil.onBeam(player);
					} else {
						player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-playernotonline"));
					}
				}
			} else if (args.length > 1) {
				if (player.hasPermission("reputation.admin")){
					if (args[0].equals("add")){
						if (args[1].equals("like")){
							if (args.length == 2){
								Rep.addLike(player, 1);
								Rep.addReputation(player, 1);
								String message1 = ArvinYML.getYML("messages.yml").getString("message-addlike-auto");
								String message2 = message1.replace("{LIKE}", "1");
								player.sendMessage(prefix + ChatColor.WHITE + message2);
							} else if (args.length == 3){
								if (GeneralUtil.isInt(args[2])){
									int amount = Integer.parseInt(args[2]);
									Rep.addLike(player, amount);
									Rep.addReputation(player, amount);
									String message1 = ArvinYML.getYML("messages.yml").getString("message-addlike-auto");
									String message2 = message1.replace("{LIKE}", args[2]);
									player.sendMessage(prefix + ChatColor.WHITE + message2);
								} else {
									player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
							}
						} else if (args[1].equals("dislike")){
							if (args.length == 2){
								Rep.addDislike(player, 1);
								Rep.rmvReputation(player, 1);
								String message1 = ArvinYML.getYML("messages.yml").getString("message-adddislike-auto");
								String message2 = message1.replace("{LIKE}", "1");
								player.sendMessage(prefix + ChatColor.WHITE + message2);
							} else if (args.length == 3){
								if (GeneralUtil.isInt(args[2])){
									int amount = Integer.parseInt(args[2]);
									Rep.addDislike(player, amount);
									Rep.rmvReputation(player, amount);
									String message1 = ArvinYML.getYML("messages.yml").getString("message-adddislike-auto");
									String message2 = message1.replace("{LIKE}", args[2]);
									player.sendMessage(prefix + ChatColor.WHITE + message2);
								} else {
									player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
							}
						} else if (args[1].equals("point")){
							if (args.length == 2){
								Rep.addPoint(player, 1);
								String message1 = ArvinYML.getYML("messages.yml").getString("message-addpoint-auto");
								String message2 = message1.replace("{POINT}", "1");
								player.sendMessage(prefix + ChatColor.WHITE + message2);
							} else if (args.length == 3){
								if (GeneralUtil.isInt(args[2])){
									int amount = Integer.parseInt(args[2]);
									Rep.addPoint(player, amount);
									String message1 = ArvinYML.getYML("messages.yml").getString("message-addpoint-auto");
									String message2 = message1.replace("{POINT}", args[2]);
									player.sendMessage(prefix + ChatColor.WHITE + message2);
								} else {
									player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
							}
						} else {
							player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
						}
					} else if (args[0].equals("remove")){
						if (args[1].equals("like")){
							if (args.length == 2){
								Rep.rmvLike(player, 1);
								Rep.rmvReputation(player, 1);
								String message1 = ArvinYML.getYML("messages.yml").getString("message-removelike-auto");
								String message2 = message1.replace("{LIKE}", "1");
								player.sendMessage(prefix + ChatColor.WHITE + message2);
							} else if (args.length == 3){
								if (GeneralUtil.isInt(args[2])){
									int amount = Integer.parseInt(args[2]);
									Rep.rmvLike(player, amount);
									Rep.rmvReputation(player, amount);
									String message1 = ArvinYML.getYML("messages.yml").getString("message-removelike-auto");
									String message2 = message1.replace("{LIKE}", args[2]);
									player.sendMessage(prefix + ChatColor.WHITE + message2);
								} else {
									player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
							}
						} else if (args[1].equals("dislike")){
							if (args.length == 2){
								Rep.rmvDislike(player, 1);
								Rep.addReputation(player, 1);
								String message1 = ArvinYML.getYML("messages.yml").getString("message-removedislike-auto");
								String message2 = message1.replace("{LIKE}", "1");
								player.sendMessage(prefix + ChatColor.WHITE + message2);
							} else if (args.length == 3){
								if (GeneralUtil.isInt(args[2])){
									int amount = Integer.parseInt(args[3]);
									Rep.rmvDislike(player, amount);
									Rep.addReputation(player, amount);
									String message1 = ArvinYML.getYML("messages.yml").getString("message-removedislike-auto");
									String message2 = message1.replace("{LIKE}", args[2]);
									player.sendMessage(prefix + ChatColor.WHITE + message2);
								} else {
									player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
							}
						} else if (args[1].equals("point")){
							if (args.length == 2){
								Rep.rmvPoint(player, 1);
								String message1 = ArvinYML.getYML("messages.yml").getString("message-removepoint-auto");
								String message2 = message1.replace("{POINT}", "1");
								player.sendMessage(prefix + ChatColor.WHITE + message2);
							} else if (args.length == 3){
								if (GeneralUtil.isInt(args[2])){
									int amount = Integer.parseInt(args[2]);
									Rep.rmvPoint(player, amount);
									String message1 = ArvinYML.getYML("messages.yml").getString("message-removepoint-auto");
									String message2 = message1.replace("{POINT}", args[2]);
									player.sendMessage(prefix + ChatColor.WHITE + message2);
								} else {
									player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
							}
						} else {
							player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
						}
					} else {
						Player target = Bukkit.getServer().getPlayer(args[0]);
						if (target != null){
							if (args[1].equals("add")){
								if (!(args.length <= 2)){
									if (args[2].equals("like")){
										if (args.length == 3){
											Rep.addLike(target, 1);
											Rep.addReputation(target, 1);
											String message1 = ArvinYML.getYML("messages.yml").getString("message-addlike-manual").replace("{USERNAME}", target.getName());
											String message2 = message1.replace("{LIKE}", "1");
											player.sendMessage(prefix + ChatColor.WHITE + message2);
										} else if (args.length == 4){
											if (GeneralUtil.isInt(args[3])){
												int amount = Integer.parseInt(args[3]);
												Rep.addLike(target, amount);
												Rep.addReputation(target, amount);
												String message1 = ArvinYML.getYML("messages.yml").getString("message-addlike-manual").replace("{USERNAME}", target.getName());
												String message2 = message1.replace("{LIKE}", args[3]);
												player.sendMessage(prefix + ChatColor.WHITE + message2);
											} else {
												player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
											}
										} else {
											player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
										}
									} else if (args[2].equals("dislike")){
										if (args.length == 3){
											Rep.addDislike(player, 1);
											Rep.rmvReputation(player, 1);
											String message1 = ArvinYML.getYML("messages.yml").getString("message-adddislike-manual").replace("{USERNAME}", target.getName());
											String message2 = message1.replace("{LIKE}", "1");
											player.sendMessage(prefix + ChatColor.WHITE + message2);
										} else if (args.length == 4){
											if (GeneralUtil.isInt(args[3])){
												int amount = Integer.parseInt(args[3]);
												Rep.addDislike(player, amount);
												Rep.rmvReputation(player, amount);
												String message1 = ArvinYML.getYML("messages.yml").getString("message-adddislike-manual").replace("{USERNAME}", target.getName());
												String message2 = message1.replace("{LIKE}", args[3]);
												player.sendMessage(prefix + ChatColor.WHITE + message2);
											} else {
												player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
											}
										} else {
											player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
										}
									} else if (args[2].equals("point")){
										if (args.length == 3){
											Rep.addPoint(target, 1);
											String message1 = ArvinYML.getYML("messages.yml").getString("message-addpoint-manual").replace("{USERNAME}", target.getName());
											String message2 = message1.replace("{POINT}", "1");
											player.sendMessage(prefix + ChatColor.WHITE + message2);
										} else if (args.length == 4){
											if (GeneralUtil.isInt(args[3])){
												int amount = Integer.parseInt(args[3]);
												Rep.addPoint(target, amount);
												String message1 = ArvinYML.getYML("messages.yml").getString("message-addpoint-manual").replace("{USERNAME}", target.getName());
												String message2 = message1.replace("{POINT}", args[3]);
												player.sendMessage(prefix + ChatColor.WHITE + message2);
											} else {
												player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
											}
										} else {
											player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
										}
									} else {
										player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
									}
								} else {
									player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationadd"));
								}
							} else if (args[1].equals("remove")){
								if (!(args.length <= 2)){
									if (args[2].equals("like")){
										if (args.length == 3){
											Rep.rmvLike(target, 1);
											Rep.rmvReputation(target, 1);
											String message1 = ArvinYML.getYML("messages.yml").getString("message-removelike-manual").replace("{USERNAME}", target.getName());
											String message2 = message1.replace("{LIKE}", "1");
											player.sendMessage(prefix + ChatColor.WHITE + message2);
										} else if (args.length == 4){
											if (GeneralUtil.isInt(args[3])){
												int amount = Integer.parseInt(args[3]);
												Rep.rmvLike(target, amount);
												Rep.rmvReputation(target, amount);
												String message1 = ArvinYML.getYML("messages.yml").getString("message-removelike-manual").replace("{USERNAME}", target.getName());
												String message2 = message1.replace("{LIKE}", args[3]);
												player.sendMessage(prefix + ChatColor.WHITE + message2);
											} else {
												player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
											}
										} else {
											player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
										}
									} else if (args[2].equals("dislike")){
										if (args.length == 3){
											Rep.rmvDislike(target, 1);
											Rep.addReputation(target, 1);
											String message1 = ArvinYML.getYML("messages.yml").getString("message-removedislike-manual").replace("{USERNAME}", target.getName());
											String message2 = message1.replace("{LIKE}", "1");
											player.sendMessage(prefix + ChatColor.WHITE + message2);
										} else if (args.length == 4){
											if (GeneralUtil.isInt(args[3])){
												int amount = Integer.parseInt(args[3]);
												Rep.rmvDislike(target, amount);
												Rep.addReputation(target, amount);
												String message1 = ArvinYML.getYML("messages.yml").getString("message-removedislike-manual").replace("{USERNAME}", target.getName());
												String message2 = message1.replace("{LIKE}", args[3]);
												player.sendMessage(prefix + ChatColor.WHITE + message2);
											} else {
												player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
											}
										} else {
											player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
										}
									} else if (args[2].equals("point")){
										if (args.length == 3){
											Rep.rmvPoint(target, 1);
											String message1 = ArvinYML.getYML("messages.yml").getString("message-removepoint-manual").replace("{USERNAME}", target.getName());
											String message2 = message1.replace("{POINT}", "1");
											player.sendMessage(prefix + ChatColor.WHITE + message2);
										} else if (args.length == 4){
											if (GeneralUtil.isInt(args[3])){
												int amount = Integer.parseInt(args[3]);
												Rep.rmvPoint(target, amount);
												String message1 = ArvinYML.getYML("messages.yml").getString("message-removepoint-manual").replace("{USERNAME}", target.getName());
												String message2 = message1.replace("{POINT}", args[3]);
												player.sendMessage(prefix + ChatColor.WHITE + message2);
											} else {
												player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
											}
										} else {
											player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
										}
									} else {
										player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
									}
								} else {
									player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
								}
							} else {
								player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputationremove"));
							}
						} else {
							player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-wrongcmd-reputation"));
						}
						
					}
				} else {
					player.sendMessage(prefix + ChatColor.WHITE + ArvinYML.getYML("messages.yml").getString("message-nopermission"));
				}
			}
		}
		return false;
	}

	public void messageHelp(Player p){
		for(String msg : ArvinYML.getYML("messages.yml").getStringList("message-help-command")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		}
	}
}
