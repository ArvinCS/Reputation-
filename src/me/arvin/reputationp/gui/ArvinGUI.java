package me.arvin.reputationp.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface ArvinGUI extends InventoryHolder {
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType click);
}