package me.manaki.plugin.orestorage.storage;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.manaki.plugin.orestorage.OreStorage;
import me.manaki.plugin.orestorage.object.PlayerBlockData;

public class FileStorage {
	
	public static boolean hasData(String player) {
		player = player.toLowerCase();
		File file = new File(OreStorage.main.getDataFolder() + "//playerdata//" + player + ".yml");
		return file.exists();
	}
	
	public static File getFile(String player) {
		player = player.toLowerCase();
		File file = new File(OreStorage.main.getDataFolder() + "//playerdata//" + player + ".yml");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return file;
	}
	
	public static PlayerBlockData fromFile(File file) {
		FileConfiguration storage = YamlConfiguration.loadConfiguration(file);
		PlayerBlockData data = new PlayerBlockData();
		if (!storage.contains("blocks")) return data;
		storage.getConfigurationSection("blocks").getKeys(false).forEach(s -> {
			data.setBlock(Material.valueOf(s), storage.getInt("blocks." + s));
		});
		return data;
	}
	
	public static void saveData(String player, PlayerBlockData data) {
		player = player.toLowerCase();
		File file = getFile(player);
		FileConfiguration storage = YamlConfiguration.loadConfiguration(file);
		data.getMap().forEach((m,i) -> {
			storage.set("blocks." + m.name(), i);
		});
		try {
			storage.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PlayerBlockData getData(String player) {
		player = player.toLowerCase();
		File file = getFile(player);
		return fromFile(file);
	}
	
}
