package me.arvin.reputationp.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;	
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ArvinGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof ArvinGUI) {
        	if(e.getCurrentItem() != null && e.getCurrentItem().getType() != null && e.getCurrentItem().getType() != Material.AIR) {
        		e.setCancelled(true);
	            ArvinGUI gui = (ArvinGUI) e.getInventory().getHolder();
	            gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem(), e.getClick());
        	}
        }  
    }
}
