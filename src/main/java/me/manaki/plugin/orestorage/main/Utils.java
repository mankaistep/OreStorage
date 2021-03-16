package me.manaki.plugin.orestorage.main;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.manaki.plugin.orestorage.object.ConfigManager;
import me.manaki.plugin.orestorage.object.SBDManager;
import mk.plugin.playerdata.storage.PlayerData;
import mk.plugin.playerdata.storage.PlayerDataAPI;

public class Utils {
	
	public static void saveMaxStorage(String name) {
		int max = getMaxStorage(name);
		PlayerData pd = PlayerDataAPI.get(name, "orestorage");
		pd.set("max", max + "");
		pd.save();
	}
	
	public static int getMaxStorage(String name) {
		Player p = Bukkit.getPlayer(name);
		if (p == null) {
			PlayerData pd = PlayerDataAPI.get(name, "orestorage");
			if (pd.hasData("max")) return Integer.valueOf(pd.getValue("max"));
			return ConfigManager.MAX_DEFAULT;
		}
		return SBDManager.getMax(p);
	}
	
	public static int randomInt(int min, int max) {
		return new Random().nextInt(max + 1 - min) + min;
	}
	
	public static String firstCharUppercase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	public static ItemStack getItem(Material m, int amount) {
		ItemStack is = new ItemStack(m, amount);
		if (m == Material.INK_SACK) is.setDurability((short) 4);
		return is;
	}
	
	public static String getTrans(Material m) {
		switch (m) {
		case STONE: return "Đá";
		case COBBLESTONE: return "Đá cuội";
		case COAL: return "Than";
		case COAL_BLOCK: return "Khối than";
		case REDSTONE: return "Đá đỏ";
		case REDSTONE_BLOCK: return "Khối đá đỏ";
		case INK_SACK: return "Lưu ly";
		case LAPIS_BLOCK: return "Khối lưu ly";
		case IRON_INGOT: return "Sắt";
		case IRON_BLOCK: return "Khối sắt";
		case GOLD_INGOT: return "Vàng";
		case GOLD_BLOCK: return "Khối vàng";
		case DIAMOND: return "Kim cương";
		case DIAMOND_BLOCK: return "Khối kim cương";
		case EMERALD: return "Lục bảo";
		case EMERALD_BLOCK: return "Khối lục bảo";
		case QUARTZ: return "Thạch anh";
		case QUARTZ_BLOCK: return "Khối thạch anh";
			
		default: return m.name();
		}
	}
	
}
