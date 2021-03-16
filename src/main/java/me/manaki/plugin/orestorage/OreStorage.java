package me.manaki.plugin.orestorage;

import java.io.File;
import java.io.IOException;

import me.manaki.plugin.orestorage.island.ASkyblockManager;
import me.manaki.plugin.orestorage.island.IslandManager;
import me.manaki.plugin.orestorage.island.SuperiorSkyblock2Manager;
import me.manaki.plugin.orestorage.main.Commands;
import me.manaki.plugin.orestorage.main.EventHandling;
import me.manaki.plugin.orestorage.main.KhoCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.manaki.plugin.orestorage.object.ConfigManager;
import me.manaki.plugin.orestorage.object.SBDManager;
import me.manaki.plugin.orestorage.storage.SBDStorage;

public class OreStorage extends JavaPlugin {
	
	public static OreStorage main;
	public IslandManager islandManager;
	public FileConfiguration config;
	
	@Override
	public void onEnable() {
		main = this;
		this.saveDefaultConfig();
		this.reloadCF();
		
		Bukkit.getPluginManager().registerEvents(new EventHandling(), this);
		this.getCommand("orestorage").setExecutor(new Commands());
		this.getCommand("kho").setExecutor(new KhoCommand());
		
		Bukkit.getOnlinePlayers().forEach(p -> {
			SBDManager.load(p.getName());
		});

		if (Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) {
			this.islandManager = new SuperiorSkyblock2Manager();
		}
		else if (Bukkit.getPluginManager().isPluginEnabled("ASkyBlock")) {
			this.islandManager = new ASkyblockManager();
		}
	}
	
	@Override
	public void onDisable() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			SBDManager.save(player.getName());
		});
	}
	
	public void reloadCF() {
		File file = new File(this.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(file);
		ConfigManager.load(config);
		SBDStorage.init(config);
	}
	
	public void saveCF() {
		File file = new File(this.getDataFolder(), "config.yml");
		try {
			config.save(file);
		} catch (IOException e) {
		}
	}
}
