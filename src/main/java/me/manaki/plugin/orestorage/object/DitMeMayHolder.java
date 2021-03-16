package me.manaki.plugin.orestorage.object;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class DitMeMayHolder implements InventoryHolder {

	private String owner;
	
	public DitMeMayHolder(String owner) {
		this.owner = owner;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

}
