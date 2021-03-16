package me.manaki.plugin.orestorage.object;

import org.bukkit.Material;

public class SBDGUIItem {
	
	public Material material;
	public int leftClick;
	public int rightClick;
	public int shiftLeftClick;
	public int shiftRightClick;
	
	public SBDGUIItem(Material material, int leftClick, int rightClick, int shiftLeftClick, int shiftRightClick) {
		this.leftClick = leftClick;
		this.rightClick = rightClick;
		this.material = material;
		this.shiftLeftClick = shiftLeftClick;
		this.shiftRightClick = shiftRightClick;
	}
	
}
