package me.arvin.reputationp.utility;
 
import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.arvin.reputationp.Main;

public class ActionBar {
       
       
          final static HashMap<String, Integer> Count = new HashMap<String,Integer>();
       
          public static void sendActionBar(Player player, String Teks)
          {
                 final String TeksNeu = Teks.replace("_", " ");
            String s = ChatColor.translateAlternateColorCodes('&', TeksNeu);
            try {
				Object chat = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + s +"\"}");
				Object packet;
				if(Main.ver.get("Version") != 12) {
					Constructor<?> titleConstructor = Reflection.getNMSClass("PacketPlayOutChat").getConstructor(Reflection.getNMSClass("IChatBaseComponent"), byte.class);
					packet = titleConstructor.newInstance(chat, (byte)2);
				} else {
					//Object type = Reflection.getNMSClass("ChatMessageType").getDeclaredField("ACTION_BAR");
					Constructor<?> titleConstructor = Reflection.getNMSClass("PacketPlayOutChat").getConstructor(Reflection.getNMSClass("IChatBaseComponent"), Reflection.getNMSClass("ChatMessageType"));
					packet = titleConstructor.newInstance(chat, Reflection.getNMSClass("ChatMessageType").getEnumConstants()[2]);
				}
				
				Reflection.sendPacket(player, packet);
            }  catch (Exception e1) {
                e1.printStackTrace();
      	  	}
          }
       
          public static void sendActionBarTime(final Player player, final String Teks,final Integer Waktu)
          {
              final String TeksNeu = Teks.replace("_", " ");
              if(!Count.containsKey(player.getName())){
            	  String s = ChatColor.translateAlternateColorCodes('&', TeksNeu);
            	  try {
            		  Object chat = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + s +"\"}");
      				Object packet;
      				if(Main.ver.get("Version") != 12) {
      					Constructor<?> titleConstructor = Reflection.getNMSClass("PacketPlayOutChat").getConstructor(Reflection.getNMSClass("IChatBaseComponent"), byte.class);
      					packet = titleConstructor.newInstance(chat, (byte)2);
      				} else {
      					//Object type = Reflection.getNMSClass("ChatMessageType").getDeclaredField("ACTION_BAR");
      					Constructor<?> titleConstructor = Reflection.getNMSClass("PacketPlayOutChat").getConstructor(Reflection.getNMSClass("IChatBaseComponent"), Reflection.getNMSClass("ChatMessageType"));
      					packet = titleConstructor.newInstance(chat, Reflection.getNMSClass("ChatMessageType").getEnumConstants()[2]);
      				}
      				
      				Reflection.sendPacket(player, packet);
                  }  catch (Exception e1) {
                      e1.printStackTrace();
            	  }
              }
              Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
                  @Override
                  public void run() {
                	  String s = ChatColor.translateAlternateColorCodes('&', TeksNeu);
                	  try {
                		  Object chat = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + s +"\"}");
          				Object packet;
          				if(Main.ver.get("Version") != 12) {
          					Constructor<?> titleConstructor = Reflection.getNMSClass("PacketPlayOutChat").getConstructor(Reflection.getNMSClass("IChatBaseComponent"), byte.class);
          					packet = titleConstructor.newInstance(chat, (byte)2);
          				} else {
          					//Object type = Reflection.getNMSClass("ChatMessageType").getDeclaredField("ACTION_BAR");
          					Constructor<?> titleConstructor = Reflection.getNMSClass("PacketPlayOutChat").getConstructor(Reflection.getNMSClass("IChatBaseComponent"), Reflection.getNMSClass("ChatMessageType"));
          					packet = titleConstructor.newInstance(chat, Reflection.getNMSClass("ChatMessageType").getEnumConstants()[2]);
          				}
          				
          				Reflection.sendPacket(player, packet);
                      }  catch (Exception e1) {
                          e1.printStackTrace();
                	  	}
                      if(!Count.containsKey(player.getName())){
                    	  Count.put(player.getName(),0);
                      }
                      int count = Count.get(player.getName());
                      int newCount = count+20;
                      Count.put(player.getName(), newCount);
                      if(newCount < Waktu-20){
                    	  ActionBar.wait(player,Teks,Waktu);
                      }else{
                          Count.remove(player.getName());
                      }
                  }
              }, 10);
          }
 
          private static void wait(final Player player, final String Teks,final Integer Waktu){
        	  Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
	              @Override
	              public void run() {
	            	  sendActionBarTime(player,Teks,Waktu);
	              }
              }, 10);              
          }
         
}