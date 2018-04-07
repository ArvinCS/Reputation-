package me.arvin.reputationp.utility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class MyConfig 
{
    private int comments;
    private MyConfigManager manager;
 
    private File file;
    private FileConfiguration config;
 
	public MyConfig(InputStream configStream, File configFile, int comments, JavaPlugin plugin) 
    {
        this.comments = comments;
        this.manager = new MyConfigManager(plugin);
        this.file = configFile;
        Reader targetReader = new InputStreamReader(configStream);
        this.config = YamlConfiguration.loadConfiguration(targetReader);
    }
 
    public static void streamtofile(String[] args) {

    	InputStream inputStream = null;
    	OutputStream outputStream = null;

    	try {
    		// read this file into InputStream
    		inputStream = new FileInputStream("/Users/mkyong/Downloads/holder.js");

    		// write the inputStream to a FileOutputStream
    		outputStream =
                        new FileOutputStream(new File("/Users/mkyong/Downloads/holder-new.js"));

    		int read = 0;
    		byte[] bytes = new byte[1024];

    		while ((read = inputStream.read(bytes)) != -1) {
    			outputStream.write(bytes, 0, read);
    		}

    		System.out.println("Done!");

    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (inputStream != null) {
    			try {
    				inputStream.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		if (outputStream != null) {
    			try {
    				// outputStream.flush();
    				outputStream.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}

    		}
    	}
        }
    public Object get(String path) {return this.config.get(path);}
 
    public Object get(String path, Object def) {return this.config.get(path, def);}
 
    public String getString(String path) {return ChatColor.translateAlternateColorCodes('&', this.config.getString(path));}
 
    public String getString(String path, String def) {return this.config.getString(path, def);}
 
    public int getInt(String path) {return this.config.getInt(path);}
 
    public int getInt(String path, int def) {return this.config.getInt(path, def);}
 
    public boolean getBoolean(String path) {return this.config.getBoolean(path);}
 
    public boolean getBoolean(String path, boolean def) {return this.config.getBoolean(path, def);}
 
    public void createSection(String path) {this.config.createSection(path);}
 
    public ConfigurationSection getConfigurationSection(String path) {return this.config.getConfigurationSection(path);}
 
    public double getDouble(String path) {return this.config.getDouble(path);}
 
    public double getDouble(String path, double def) {return this.config.getDouble(path, def);}
 
    public List<?> getList(String path) {return this.config.getList(path);}
 
    public List<?> getList(String path, List<?> def) {return this.config.getList(path, def);}
 
    public List<String> getStringList(String path) {return this.config.getStringList(path);}
    public boolean contains(String path) {return this.config.contains(path);}
 
    public void removeKey(String path) {this.config.set(path, null);}
 
    public void set(String path, Object value) {this.config.set(path, value);}
 
    public void set(String path, Object value, String comment)
    {
        if(!this.config.contains(path)) 
        {
            this.config.set(manager.getPluginName() + "_COMMENT_" + comments, " " + comment);
            comments++;
        }
        this.config.set(path, value);
    }
 
    public void comment(String comment) {
           this.config.set(manager.getPluginName() + "_COMMENT_" + comments, " " + comment);
           comments++;
    }
    
    public void set(String path, Object value, String[] comment) 
    {
        for(String comm : comment) 
        {
            if(!this.config.contains(path)) 
            {
                this.config.set(manager.getPluginName() + "_COMMENT_" + comments, " " + comm);
                comments++;
            }
        }
        this.config.set(path, value);
    }
 
    public void setHeader(String[] header) 
    {
        manager.setHeader(this.file, header);
        this.comments = header.length + 2;
        this.reloadConfig();
    }
 
	public void reloadConfig() {
    	Reader targetReader = new InputStreamReader(manager.getConfigContent(file));
    	this.config = YamlConfiguration.loadConfiguration(targetReader);
    }
 
    public void saveConfig() 
    {
        String config = this.config.saveToString();
        manager.saveConfig(config, this.file);
    }
 
    public Set<String> getKeys() {return this.config.getKeys(false);}
}
