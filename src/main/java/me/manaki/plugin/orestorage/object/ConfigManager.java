package me.manaki.plugin.orestorage.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ConfigManager {
	
	public static boolean FILE_STORAGE = true;
	public static Map<Material, Material> REPLACED_BLOCKS = new HashMap<Material, Material> ();
	public static Set<Material> ALLOWED_BLOCKS = new HashSet<Material> ();
	public static boolean AUTO_MATCH = true;
	public static Map<Material, Material> MATCH = new HashMap<Material, Material> ();
	public static Map<Integer, SBDGUIItem> GUIITEMS = new HashMap<Integer, SBDGUIItem> ();
	public static int GUI_SIZE = 9;
	public static String GUI_TITLE = "";
	public static String GUI_OPEN_CMD = "";
	public static int MAX_DEFAULT = 256;
	public static String MAX_PERMISSION = "";
	public static List<String> WORLDS = new ArrayList<String> ();
	public static List<String> LOOT_BONUS_BLOCKS = Lists.newArrayList();
	
	public static boolean SELL_ENABLE = false;
	public static Map<Material, Double> SELL_PRICES = Maps.newHashMap();
	
	public static void load(FileConfiguration config) {
		FILE_STORAGE = config.getBoolean("storage.file.enable");
		
		REPLACED_BLOCKS.clear();
		for (String s : config.getConfigurationSection("option.replace").getKeys(false)) {
			REPLACED_BLOCKS.put(Material.valueOf(s), Material.valueOf(config.getString("option.replace." + s)));
		}
		
		ALLOWED_BLOCKS.clear();
		config.getStringList("option.allow-block").forEach(s -> {
			ALLOWED_BLOCKS.add(Material.valueOf(s));
		});
		
		AUTO_MATCH = config.getBoolean("option.auto-match");

		MATCH.clear();
		for (String s : config.getConfigurationSection("option.match").getKeys(false)) {
			MATCH.put(Material.valueOf(s), Material.valueOf(config.getString("option.match." + s)));
		}
		
		GUIITEMS.clear();
		config.getConfigurationSection("gui").getKeys(false).forEach(s -> {
			int slot = Integer.parseInt(s);
			Material m = Material.valueOf(config.getString("gui." + s + ".type"));
			int leftClick = config.getInt("gui." + s + ".left-click");
			int rightClick = config.getInt("gui." + s + ".right-click");
			int shiftLeftClick = config.getInt("gui." + s + ".shift-left-click");
			int shiftRightClick = config.getInt("gui." + s + ".shift-right-click");
			GUIITEMS.put(slot, new SBDGUIItem(m, leftClick, rightClick, shiftLeftClick, shiftRightClick));
		});
		
		GUI_SIZE = config.getInt("gui-size");
		GUI_TITLE = config.getString("gui-title").replace("&", "§");
		GUI_OPEN_CMD = config.getString("gui-open-cmd");
		
		MAX_DEFAULT = config.getInt("max-default");
		MAX_PERMISSION = config.getString("max-permission");
		
		WORLDS = config.getStringList("worlds");
		
		LOOT_BONUS_BLOCKS = config.getStringList("option.loot-bonus-block");
		
		SELL_ENABLE = config.getBoolean("sell.enable");
		SELL_PRICES.clear();
		config.getConfigurationSection("sell.price").getKeys(false).forEach(ms -> {
			SELL_PRICES.put(Material.valueOf(ms), config.getDouble("sell.price." + ms));
		});
	}
	
}
