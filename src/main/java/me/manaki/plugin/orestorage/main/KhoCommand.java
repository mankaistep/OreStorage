package me.manaki.plugin.orestorage.main;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.manaki.plugin.orestorage.object.SBDManager;
import me.manaki.plugin.orestorage.storage.AllowedUsers;

public class KhoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		try {
			if (!sender.hasPermission("orestorage.kho")) {
				sender.sendMessage("§cThiếu permission: orestorage.kho");
				return false;
			}
		
			Player player = (Player) sender;
			
			if (args.length == 0) {
				SBDManager.open(player.getName(), player);
				return false;
			}
			
			else if (args[0].equalsIgnoreCase("open")) {
				if (args.length == 1) {
					SBDManager.open(player.getName(), player);
					return false;
				}
//				Player target = Bukkit.getPlayer(args[1]);
				String target = args[1];
				if (!AllowedUsers.get(target).contains(player.getName().toLowerCase()))  {
					player.sendMessage("§cBạn không thể dùng kho của người chơi này");
					return false;
				}
				SBDManager.open(target, player);
			}
			
			else if (args[0].equalsIgnoreCase("help")) {
				sendHelp(sender);
			}
			
			else if (args[0].equalsIgnoreCase("allow")) {
				String target = args[1];
				List<String> list = AllowedUsers.get(player.getName());
				list.add(target);
				AllowedUsers.save(player.getName(), list);
				sender.sendMessage("§aĐã cho phép người chơi " + target + " dùng kho của bạn");
			}
			
			else if (args[0].equalsIgnoreCase("block")) {
				String target = args[1];
				List<String> list = AllowedUsers.get(player.getName());
				if (list.contains(target.toLowerCase())) {
					list.remove(target.toLowerCase());
					sender.sendMessage("§aĐã chặn người chơi " + target + " dùng kho của bạn");
					AllowedUsers.save(player.getName(), list);
				} else {
					sender.sendMessage("§aNgười chơi này hiện tại không thể dùng kho của bạn!");
				}
			}
			
		}
		catch (ArrayIndexOutOfBoundsException e) {
			sendHelp(sender);
		}
		
		return false;
	}
	
	public void sendHelp(CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage("§6/kho: §eMở kho của bạn");
		sender.sendMessage("§6/kho open <người chơi>: §eMở kho của ai đó");
		sender.sendMessage("§6/kho allow <người chơi>: §eCho phép người chơi khác dùng kho của bạn");
		sender.sendMessage("§6/kho block <người chơi>: §eChặn người chơi khác dùng kho của bạn");
		sender.sendMessage("");
	}

}
