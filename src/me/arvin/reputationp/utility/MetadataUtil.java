package me.arvin.reputationp.utility;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import me.arvin.reputationp.Main;



public class MetadataUtil {
	//METADATA UTILITY BY PRAYA
	public static final MetadataValue createMetadata(Object value){
		return new FixedMetadataValue(Main.get(), value);
	}
	public static final MetadataValue getMetadata (LivingEntity livingEntity, String metadata){
		return livingEntity.getMetadata(metadata).get(0);
	}

	public static final void removeMetadata(LivingEntity livingEntity, String metadata){
		livingEntity.removeMetadata(metadata, Main.get());
	}
	public static final boolean isExpired(Player player, String metadata){
		if (!player.hasMetadata(metadata)){
			return true;
		} else {
			final long expired = getMetadata(player, metadata).asLong();
			final long time = System.currentTimeMillis();
			
			if (time < expired){
				return false;
			} else {
				player.removeMetadata(metadata, Main.get());
				return true;
			}
		}
	}
	public static final void setCooldown(Player player, String metadata, long cooldown){
		final long cd = cooldown * 1000;
		final long expired= System.currentTimeMillis() + cd;

		player.setMetadata(metadata, createMetadata(expired));
	}
	
	public static final void createMetadata(Player player, String metadata){
		if (!player.hasMetadata(metadata)){
			player.setMetadata(metadata, createMetadata(metadata));
		}
	}
	
	public static final void removeMetadata(Player player, String metadata){
		if (player.hasMetadata(metadata)){
			player.removeMetadata(metadata, Main.get());
		}
	}
	///////
	public static final boolean isBoolean(Player player, String metadata){
		if (!player.hasMetadata(metadata)){
			return false;
		} else {
			return true;
		}
	}

	public static void setBoolean(Player player, String metadata, boolean b){
		player.setMetadata(metadata, createMetadata(b));
	}
	
	public static final void clrBoolean(Player player, String metadata){
		removeMetadata(player, metadata);
	}
}
