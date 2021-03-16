package me.manaki.plugin.orestorage.main;

import me.manaki.plugin.orestorage.OreStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.manaki.plugin.orestorage.storage.SBDStorage;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if (!sender.hasPermission("dd.*")) {
			sender.sendMessage("§cDon't have enough permission!");
			return false;
		}
		
		try {
			if (args[0].equals("reload")) {
				OreStorage.main.reloadCF();
				sender.sendMessage("§aReloaded");
			}
			
			else if (args[0].equals("import")) {
				boolean fromFile = args[1].equalsIgnoreCase("file");
				Bukkit.getScheduler().runTaskAsynchronously(OreStorage.main, () -> {
					long start = System.currentTimeMillis();
					SBDStorage.importData(fromFile);
					long end = System.currentTimeMillis();
					sender.sendMessage("§aDone! Take "  + (end - start)  + " ms");
				});
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			sendTut(sender);
		}
		
		return false;
	}
	
	public void sendTut(CommandSender sender) {
		sender.sendMessage("/orestorage reload: Reload");
		sender.sendMessage("/orestorage <file/mysql>: Chuyển dữ liệu từ <..> đến nơi lưu trữ còn lại");
	}

}
