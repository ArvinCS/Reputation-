package me.arvin.reputationp.utility;

import me.arvin.lib.config.ArvinConfig;
import me.arvin.reputationp.file.ArvinYML;

public class Message {
	static ArvinConfig msg = ArvinYML.getYML("messages.yml");
	
	public static String read(String text){
		if (text != null){
			return msg.getString(text);
		}
		return text;
	}
}
