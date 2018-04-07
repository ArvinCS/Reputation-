package me.arvin.reputationp.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.arvin.reputationp.Main;
import me.arvin.reputationp.utility.MyConfig;
import me.arvin.reputationp.utility.MyConfigManager;

public class ArvinYML {
	private static List<ArvinYML> list = new ArrayList<ArvinYML>();
	
	private String name;
	private MyConfig config;
	private HashMap<String, Object> data = new HashMap<String, Object>();
	public ArvinYML(String filename) {
		this.name = filename;
		list.add(this);
	}
	
	public static ArvinYML getArvinYML(String filename) {
		for(ArvinYML yml : list) {
			if(yml.getName().equalsIgnoreCase(filename)) return yml;
		}
		return null;
	}
	
	public static MyConfig getYML(String filename) {
		for(ArvinYML yml : list) {
			if(yml.getName().equalsIgnoreCase(filename)) return yml.toFile();
		}
		return null;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArvinYML replaceOldKey() {
		for(String key : toFile().getKeys()) {
			if(toFile().get(key) != null && !key.startsWith("#") && !key.startsWith(Main.get().getDescription().getName() + "_COMMENT_")) {
				if(toFile().getConfigurationSection(key) != null) {
					for(String keys: config.getConfigurationSection(key).getKeys(false)) {
						keys = config.getConfigurationSection(key).getCurrentPath() + "." + keys;
						if(config.getConfigurationSection(keys) == null ) data.put(keys, config.get(keys));
						else loopKey(keys);
					}
				} else {
					data.put(key, config.get(key));
				}
			}
		}
		Main.get().saveResource(getName(), true);
		refresh();
		for(String key : data.keySet()) {
			if(toFile().get(key) != null) {
				toFile().set(key, data.get(key));
			}
		}
		
		toFile().saveConfig();
		
		return this;
	}
	
	public void loopKey(String key) {
		for(String keys: config.getConfigurationSection(key).getKeys(false)) {
			keys = config.getConfigurationSection(key).getCurrentPath() + "." + keys;
			if(config.getConfigurationSection(keys) == null ) data.put(keys, config.get(keys));
			else loopKey(keys);
		}
	}
	
	public MyConfig toFile() {
		if(this.config == null) this.config = new MyConfigManager(Main.get()).getNewConfig(getName());
		return this.config;
	}
	
	public void refresh() {
		this.config = new MyConfigManager(Main.get()).getNewConfig(getName());
	}
	
	public ArvinYML replaceIfNotExist() {
		if(!new File(Main.get().getDataFolder(), getName()).exists())Main.get().saveResource(getName(), true);
		return this;
	}
	
	public ArvinYML replaceIfNotExist(File folder) {
		if(!new File(folder, getName()).exists())Main.get().saveResource(getName(), true);
		return this;
	}
}
