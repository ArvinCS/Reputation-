package me.arvin.reputationp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.arvin.reputationp.command.Reputation;
import me.arvin.reputationp.command.ReputationDislike;
import me.arvin.reputationp.command.ReputationLike;
import me.arvin.reputationp.event.Chat;
import me.arvin.reputationp.event.ClickPlayer;
import me.arvin.reputationp.event.JoinLeave;
import me.arvin.reputationp.event.PlaceholderRegister;
import me.arvin.reputationp.event.RegisterNameTag;
import me.arvin.reputationp.file.ArvinYML;
import me.arvin.reputationp.gui.ArvinGUIListener;
import me.arvin.reputationp.sql.Database;
import me.arvin.reputationp.sql.MySQL;
import me.arvin.reputationp.sql.SQLite;
import net.minecraft.util.com.google.common.io.ByteStreams;


public class Main extends JavaPlugin {
	public static HashMap<String, Integer> ver = new HashMap<String, Integer>();
	public static boolean prefixchat = true;
	public static String prefix = null;
	public static String prefixplugin = ChatColor.translateAlternateColorCodes('&', "&a[&bREP+&a]&r ");
	public static boolean SilentJoinQuit = true;
	public static boolean RightClickGUI = true;
	public static int CooldownRequest = 0;
	public static boolean Animated = false;
	public static boolean ChatF = true;
	private static String sqltype = null;
	private static Main plugin;
	private Database db;
	public void onEnable(){
		System.out.println("[!] Reputation+ Enabled !");
		plugin = this;
		checkUpdates();
		// ---=== Settings ===--- //
		new ArvinYML("config.yml").replaceIfNotExist().replaceOldKey();
		new ArvinYML("messages.yml").replaceIfNotExist().replaceOldKey();
		new ArvinYML("gui.yml").replaceIfNotExist().replaceOldKey();
		// ---=== Misc ===--- //
		registerSupport();
		checkSettings();
		CheckVersion();
		// ---=== Core ===--- //
		RegisterCommands();
		RegisterEvents();
		// ---=== Message ===--- //
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "*------====== &aReputation+ &b======------*");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Liked this plugin ? Dont forget to review !");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Author: Arvin");
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "*----------=================----------*");
		// ---=== Database ===--- //
		if(sqltype.equalsIgnoreCase("SQLite")) {
			this.db = new SQLite(this);
			this.db.load();
		} else if (sqltype.equalsIgnoreCase("MySQL")) {
			this.db = new MySQL(this, ArvinYML.getYML("config.yml").getString("MySQL.Host"), ArvinYML.getYML("config.yml").getInt("MySQL.Port"), ArvinYML.getYML("config.yml").getString("MySQL.Database"), ArvinYML.getYML("config.yml").getString("MySQL.User"), ArvinYML.getYML("config.yml").getString("MySQL.Password"));
			this.db.load();
		}
	}
		
	@SuppressWarnings("deprecation")
	public void registerSupport(){
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")){
			RegisterNameTag.Register();
		}
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
			new PlaceholderRegister(this).hook();
		}
	}
	public void onDisable(){
		System.out.println("[!] Reputation+ Disabled !");
	}
	
	public static final Main get() {
		return plugin;
	}
	
	public static void RegisterCommands(){
		plugin.getCommand("reputation").setExecutor(new Reputation());
		plugin.getCommand("replike").setExecutor(new ReputationLike());
		plugin.getCommand("repdislike").setExecutor(new ReputationDislike());
	}
	
	public void RegisterEvents(){
		Bukkit.getServer().getPluginManager().registerEvents(new ArvinGUIListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinLeave(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Chat(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ClickPlayer(), this);
	}

	
	public Database getRDatabase() {
		return this.db;
	}
	
	public File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);

        try {
            if (!resourceFile.exists() && resourceFile.createNewFile()) {
                try (InputStream in = plugin.getResource(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception ignored) {}
        return resourceFile;
    }
	
	private static void CheckVersion(){
		if (Bukkit.getVersion().contains("1.7")){
			ver.put("Version", 7);
		} else if (Bukkit.getVersion().contains("1.8")){
			ver.put("Version", 8);
		} else if (Bukkit.getVersion().contains("1.9")){
			ver.put("Version", 9);
		} else if (Bukkit.getVersion().contains("1.10")){
			ver.put("Version", 10);
		} else if (Bukkit.getVersion().contains("1.11")){
			ver.put("Version", 11);
		} else if (Bukkit.getVersion().contains("1.12")){
			ver.put("Version", 12);
		}
	}
	
	public static void checkSettings(){
		if(ArvinYML.getYML("config.yml").get("Prefix Plugin") != null){
			prefixplugin = ChatColor.translateAlternateColorCodes('&', ArvinYML.getYML("config.yml").getString("Prefix Plugin"));
		}
		//
		if(ArvinYML.getYML("config.yml").getBoolean("Prefix-Chat")){
			prefixchat = true;
			prefix = ChatColor.translateAlternateColorCodes('&', ArvinYML.getYML("config.yml").getString("Prefix"));
		} else {
			prefixchat = false;
		}
		//
		if(ArvinYML.getYML("config.yml").getBoolean("Silent-JoinQuit")){
			SilentJoinQuit = true;
		} else {
			SilentJoinQuit = false;
		}
		//
		if(ArvinYML.getYML("config.yml").getBoolean("RightClick-GUI")){
			RightClickGUI = true;
		} else {
			RightClickGUI = false;
		}
		//
		if(ArvinYML.getYML("config.yml").get("Cooldown-Request") != null){
			CooldownRequest = ArvinYML.getYML("config.yml").getInt("Cooldown-Request");
		} else {
			CooldownRequest = 3;
		}
		//
		if(ArvinYML.getYML("config.yml").get("Cooldown-Request") != null){
			Animated = ArvinYML.getYML("config.yml").getBoolean("AnimatedNames-Tag");
		} else {
			Animated = false;
		}
		//
		if(ArvinYML.getYML("config.yml").get("Chat-Fiture") != null){
			ChatF = ArvinYML.getYML("config.yml").getBoolean("Chat-Fiture");
		} else {
			ChatF = false;
		}
		//
		if(ArvinYML.getYML("config.yml").get("Database Type") != null) {
			sqltype = ArvinYML.getYML("config.yml").getString("Database Type");
		} else {
			sqltype = "SQLite";
		}
	}
	
	public void checkUpdates() {
	    URL checkURL;
	    String newVersion = "";
		try {
			checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=36001");
	        URLConnection con = checkURL.openConnection();
	        newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
	        if(!plugin.getDescription().getVersion().equals(newVersion)) {
                plugin.getLogger().info("There is a nev version available!");
                plugin.getLogger().info("["+newVersion+"] www.spigotmc.org/resources/36001/");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
