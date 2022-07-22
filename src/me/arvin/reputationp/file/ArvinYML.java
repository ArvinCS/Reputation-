package me.arvin.reputationp.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.arvin.reputationp.Main;
import me.arvin.lib.config.ArvinConfig;
import me.arvin.lib.config.ArvinConfigManager;

public class ArvinYML {
	private static List<ArvinYML> list = new ArrayList<ArvinYML>();
	
	private String name;
	private ArvinConfig config;
	private HashMap<String, Object> data = new HashMap<String, Object>();
	public ArvinYML(String filename) {
		this.name = filename;
		list.add(this);
	}
	
	public static ArvinYML getArvinYML(String filename) {
		for(ArvinYML yml : list) {
			if(yml.getName().equalsIgnoreCase(filename)) return yml;
		}
		ArvinYML yml = new ArvinYML(filename);
		return yml;
	}
	
	public static ArvinConfig getYML(String filename) {
		for(ArvinYML yml : list) {
			if(yml.getName().equalsIgnoreCase(filename)) return yml.toFile();
		}
		ArvinYML yml = new ArvinYML(filename);
		return yml.toFile();
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArvinYML replaceOldKey() {
		for(String key : toFile().getKeys()) {
			if(toFile().get(key) != null && !key.startsWith("#") && !key.startsWith(Main.get().getDescription().getName() + "_COMMENT_")) {
				if(toFile().getConfigurationSection(key) != null) {
					loopKey(key);
				} else {
					data.put(key, config.get(key));
				}
			}
		}
		config.getManager().copyResource(Main.get().getResource(getName()), getFile());
		refresh();
		for(String key : data.keySet()) {
			if(toFile().get(key) != null && toFile().getConfigurationSection(key) == null) {
				toFile().set(key, data.get(key));
			}
		}
		
		toFile().saveConfig();
		
		return this;
	}
	
	private void loopKey(String key) {
		for(String keys: config.getConfigurationSection(key).getKeys(false)) {
			keys = config.getConfigurationSection(key).getCurrentPath() + "." + keys;
			if(config.getConfigurationSection(keys) == null ){
				data.put(keys, config.get(keys));
			} else {
				loopKey(keys);
			}
		}
	}
	
	public ArvinConfig toFile() {
		if(this.config == null) this.config = new ArvinConfigManager(Main.get()).getNewConfig(getName());
		return this.config;
	}
	
	public File getFile() {
		return new File(Main.get().getDataFolder(), getName());
	}
	
	public void refresh() {
		this.config = new ArvinConfigManager(Main.get()).getNewConfig(getName());
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
