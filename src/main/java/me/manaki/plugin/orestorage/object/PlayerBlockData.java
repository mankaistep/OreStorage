package me.manaki.plugin.orestorage.object;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

public class PlayerBlockData {
	
	private Map<Material, Integer> blocks = new HashMap<Material, Integer> ();
	
	public PlayerBlockData() {}
	
	public PlayerBlockData(Map<Material, Integer> map) {
		this.blocks = map;
	}
	
	public void setBlock(Material material, int amount) {
		blocks.put(material, amount);
	}
	
	public int getAmount(Material material) {
		int amount = blocks.containsKey(material) ? blocks.get(material) : 0;
		return amount;
	}
	
	public void addBlock(Material material, int amount) {
		blocks.put(material, getAmount(material) + amount);
	}
	
	public Map<Material, Integer> getMap() {
		return this.blocks;
	}
	
}
