package me.manaki.plugin.orestorage.storage;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import me.manaki.plugin.orestorage.OreStorage;
import me.manaki.plugin.orestorage.object.ConfigManager;
import me.manaki.plugin.orestorage.object.PlayerBlockData;

public class SBDStorage {

	public static void init(FileConfiguration config) {
		if (!ConfigManager.FILE_STORAGE) {
			String host = config.getString("storage.mysql.database.host");
			String port = config.getString("storage.mysql.database.port");
			String name = config.getString("storage.mysql.database.name");
			String user = config.getString("storage.mysql.database.user");
			String password = config.getString("storage.mysql.database.password");
			MySQLStorage.init(host, port, name, user, password);
		} else {
			File file = new File(OreStorage.main.getDataFolder() + "//playerdata//");
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}
	
	public static PlayerBlockData getData(String player) {
		if (ConfigManager.FILE_STORAGE) {
			return FileStorage.getData(player);
		}
		else return MySQLStorage.getData(player);
	}
	
	public static void saveData(String player, PlayerBlockData data) {
		if (ConfigManager.FILE_STORAGE) {
			FileStorage.saveData(player, data);
		}
		else MySQLStorage.saveData(player, data);
	}
	
	public static void importData(boolean fromFile) {
		if (fromFile) {
			File folder = new File(OreStorage.main.getDataFolder() + "//playerdata");
			for (File file : folder.listFiles()) {
				PlayerBlockData data = FileStorage.fromFile(file);
				MySQLStorage.saveData(file.getName().replace(".yml", ""), data);
			}

		} else {
			MySQLStorage.importToFile();
		}
	}
	
}
