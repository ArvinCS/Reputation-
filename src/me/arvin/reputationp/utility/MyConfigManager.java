package me.arvin.reputationp.utility;

import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.bukkit.plugin.java.JavaPlugin;

import me.arvin.reputationp.Main;

public class MyConfigManager 
{
	private JavaPlugin plugin;

	public MyConfigManager(JavaPlugin plugin) 
	{
		this.plugin = plugin;
	}

	public MyConfig getNewConfig(String fileName, String[] header) 
	{
		return this.getNewConfig(fileName, null, header);
	}

	public MyConfig getNewConfig(String fileName, String resource, String[] header) 
	{
		File file = this.getConfigFile(fileName);
		
		if(!file.exists())
		{
			if(resource != null) this.prepareFile(fileName, resource);
			else this.prepareFile(fileName);
			if(header != null && header.length != 0) 
				this.setHeader(file, header);
		}
		
		MyConfig config = new MyConfig(this.getConfigContent(fileName), file, getCommentsNum(file), plugin);
		return config;
	}
	
	public MyConfig getNewConfig(String fileName) 
	{
		return getNewConfig(fileName, null, null);
	}

	public MyConfig getNewConfig(String fileName, String resource) 
	{
		return getNewConfig(fileName, resource, null);
	}
	
	private File getConfigFile(String file) 
	{
		if(file.isEmpty() || file == null) 
			return null;

		File configFile;

		if(file.contains("/")) 
		{
			if(file.startsWith("/"))
				configFile = new File(plugin.getDataFolder() + file.replace("/", File.separator));
			else configFile = new File(plugin.getDataFolder() + File.separator + file.replace("/", File.separator));
		} 
		else configFile = new File(plugin.getDataFolder(), file);

		return configFile;
	}

	public void prepareFile(String filePath, String resource) 
	{
		File file = this.getConfigFile(filePath);

		if(file.exists()) 
			file.delete();

		try 
		{
			file.getParentFile().mkdirs();
			file.createNewFile();

			if(resource != null) 
				if(!resource.isEmpty())
					this.copyResource(Main.get().getResource(resource), file);

		} 
		catch (IOException e){e.printStackTrace();}
	}

	public void prepareFile(String filePath)
	{
		this.prepareFile(filePath, null);
	}

	public void setHeader(File file, String[] header)
	{
		if(!file.exists()) 
			return;

		try
		{
			String currentLine;
			StringBuilder config = new StringBuilder("");
			BufferedReader reader = new BufferedReader(new FileReader(file));

			while((currentLine = reader.readLine()) != null)
				config.append(currentLine + "\n");

			reader.close();
			config.append("# +----------------------------------------------------+ #\n");

			for(String line : header) 
			{
				if(line.length() > 50) 
					continue;

				int lenght = (50 - line.length()) / 2;
				StringBuilder finalLine = new StringBuilder(line);

				for(int i = 0; i < lenght; i++) 
				{
					finalLine.append(" ");
					finalLine.reverse();
					finalLine.append(" ");
					finalLine.reverse();
				}

				if(line.length() % 2 != 0)
					finalLine.append(" ");

				config.append("# < " + finalLine.toString() + " > #\n");
			}
			config.append("# +----------------------------------------------------+ #");

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(this.prepareConfigString(config.toString()));
			writer.flush();
			writer.close();
		} 
		catch (IOException e){e.printStackTrace();}
	}
	
	public InputStream getConfigContent(File file) 
	{
		if(!file.exists()) 
			return null;
		try 
		{
			int commentNum = 0;

			String addLine;
			String currentLine;
			String pluginName = this.getPluginName();

			StringBuilder whole = new StringBuilder("");
			BufferedReader reader = new BufferedReader(new FileReader(file));

			while((currentLine = reader.readLine()) != null) 
			{
				if(currentLine.trim().startsWith("#")) 
				{
					addLine = currentLine.replaceFirst("#", pluginName + "_COMMENT_" + commentNum + ": ");
					addLine = addLine.replace("#", pluginName + "_REPLACE_");
					whole.append(addLine + " \n");
					commentNum++;
				} 
				else whole.append(currentLine + " \n");
			}

			String config = whole.toString();
			InputStream configStream = new ByteArrayInputStream(config.getBytes(Charset.forName("UTF-8")));

			reader.close();
			return configStream;
		}
		catch (IOException e){e.printStackTrace();return null;}
	}

	private int getCommentsNum(File file)
	{
		if(!file.exists())
			return 0;
		try 
		{
			int comments = 0;
			String currentLine;

			BufferedReader reader = new BufferedReader(new FileReader(file));

			while((currentLine = reader.readLine()) != null)
				if(currentLine.startsWith("#")) 
					comments++;

			reader.close();
			return comments;
		} 
		catch (IOException e){e.printStackTrace();return 0;}
	}

	public InputStream getConfigContent(String filePath) 
	{
		return this.getConfigContent(this.getConfigFile(filePath));
	}

	private String prepareConfigString(String configString) 
	{
		int lastLine = 0;
		int headerLine = 0;

		String[] lines = configString.split("\n");
		StringBuilder config = new StringBuilder("");

		for(String line : lines)
		{
			String comment = "#";
			//if(!line.trim().startsWith(this.getPluginName() + "_COMMENT")) comment = line.substring(0, line.indexOf(this.getPluginName()));// + "#";
			if(line.trim().startsWith(this.getPluginName() + "_COMMENT")) 
			{
				if(!line.startsWith(this.getPluginName()) )comment = line.substring(0, line.indexOf(this.getPluginName())) + "#";
				/*if(!line.startsWith(this.getPluginName())) {
					//line = line.replace("    ", "?");
					comment = line.substring(0, 2) + comment;
					//comment = line.substring(line.indexOf("?"), line.indexOf(this.getPluginName() + "_COMMENT")) + 1 + comment;
					//comment = line.substring(1, line.indexOf("#") + 1);
				}*/
				line = line.replace(this.getPluginName() + "_REPLACE_", "#");
				comment = comment + line.substring(line.indexOf(":") + 1);
				if(comment.startsWith("# +-"))
				{
					if(headerLine == 0) 
					{
						config.append(comment + "\n");
						lastLine = 0;
						headerLine = 1;
					} 
					else if(headerLine == 1)
					{
						config.append(comment + "\n\n");
						lastLine = 0;
						headerLine = 0;
					}
				} 
				else
				{
					String normalComment;
					if(comment.startsWith("# '"))
						normalComment = comment.substring(0, comment.length() - 1).replaceFirst("# '", "# ");
					else normalComment = comment;

					if(lastLine == 0)
						config.append(normalComment + "\n");
					else if(lastLine == 1)
						config.append("\n" + normalComment + "\n");

					lastLine = 0;
				}
			} 
			else 
			{
				
				config.append(line + "\n");
				lastLine = 1;
			}
		}
		return config.toString();
	}

	public void saveConfig(String configString, File file)
	{
		String configuration = this.prepareConfigString(configString);

		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(configuration);
			writer.flush();
			writer.close();

		}
		catch (IOException e){e.printStackTrace();}
	}

	public String getPluginName() 
	{
		return plugin.getDescription().getName();
	}
	
	private void copyResource(InputStream resource, File file) 
	{
		try
		{
			OutputStream out = new FileOutputStream(file);

			int length;
			byte[] buf = new byte[4096];

			while((length = resource.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
				out.close();
				resource.close();
		}
		catch (Exception e) {e.printStackTrace();}
	}
}