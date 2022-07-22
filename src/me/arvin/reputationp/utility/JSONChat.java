package me.arvin.reputationp.utility;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import me.arvin.reputationp.utility.Reflection;

public class JSONChat {
	public static void Chat(Player player, String text, String name, String desc, String command){
		try{
    		//Object enumTitle = Reflection.getNMSClass("PacketPlayOutChat").getDeclaredClasses()[0].getField(null);//.getField("SUBTITLE").get(null);
			Object chat = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\""+ text + "\",\"extra\":[{\"text\":\""+ name +"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + desc + "\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + command + "\"}}]}");
			Constructor<?> titleConstructor = Reflection.getNMSClass("PacketPlayOutChat").getConstructor(Reflection.getNMSClass("IChatBaseComponent"));
			Object packet = titleConstructor.newInstance(chat);
			
			Reflection.sendPacket(player, packet);
	    } catch (Exception e1) {
            e1.printStackTrace();
  	  	}
	}
}
