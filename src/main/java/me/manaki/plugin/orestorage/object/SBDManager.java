package me.manaki.plugin.orestorage.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.manaki.plugin.orestorage.OreStorage;
import me.manaki.plugin.orestorage.main.Utils;
import me.manaki.plugin.orestorage.storage.SBDStorage;

public class SBDManager {
	
	private static Map<String, PlayerBlockData> data = new HashMap<String, PlayerBlockData> ();
	
	public static PlayerBlockData get(String name) {
		return getData(name);
	}

//	public static int getMax(String player) {
//		return Utils.getMaxStorage(player);
//	}
	
//	public static int getRemain(String player, Material material) {
//		if (!canAddMore(player, material)) return 0;
//		PlayerBlockData data = getData(player);
//		return getMax(player) - data.getAmount(material);
//	}
	
//	public static int getRemain(Player player, Material material) {
//		if (!canAddMore(player, material)) return 0;
//		PlayerBlockData data = getData(player.getName());
//		return getMax(player) - data.getAmount(material);
//	}
	
//	public static boolean canAddMore(Player player, Material material) {
//		PlayerBlockData data = getData(player.getName());
//		return (data.getAmount(material) < getMax(player));
//	}
	
//	public static boolean canAddMore(String player, Material material) {
//		PlayerBlockData data = getData(player);
//		return (data.getAmount(material) < getMax(player));
//	}
	
	public static PlayerBlockData getData(Player player) {
		return getData(player.getName());
	}
	
	public static PlayerBlockData getData(String name) {
		if (!data.containsKey(name)) load(name);
		return data.get(name);
	}
	
	public static void checkMatch(String player) {
		if (!ConfigManager.AUTO_MATCH) return;
		PlayerBlockData pData = data.get(player);
		for (Material m : ConfigManager.MATCH.keySet()) {
			int amount = pData.getAmount(m);
			if (amount < 9) continue;
			int thuong = amount / 9;
			int du = amount % 9;
			pData.addBlock(m, -1 * amount + du);
			pData.addBlock(ConfigManager.MATCH.get(m), thuong);
		}
	}
	
	public static void load(String player) {
		PlayerBlockData pData = SBDStorage.getData(player);
		SBDManager.data.put(player, pData);
	}
	
	public static void save(String player) {
		SBDStorage.saveData(player, SBDManager.get(player));
		SBDManager.data.remove(player);
	}
	
	public static void open(String player, Player viewer) {
		Inventory inv = Bukkit.createInventory(new DitMeMayHolder(player), ConfigManager.GUI_SIZE, ConfigManager.GUI_TITLE);
		Bukkit.getScheduler().runTaskAsynchronously(OreStorage.main, () -> {
			PlayerBlockData data = SBDManager.get(player);
			ConfigManager.GUIITEMS.forEach((slot, gi) -> {
				inv.setItem(slot, getIcon(gi, data, player));
			});
			if (ConfigManager.SELL_ENABLE && (player.equals(viewer.getName()))) inv.setItem(inv.getSize() - 1, getSellButton());
		});
		viewer.openInventory(inv);
	}
	
	public static Inventory getGUI(Player player) {
		Inventory inv = Bukkit.createInventory(new DitMeMayHolder(player.getName()), ConfigManager.GUI_SIZE, ConfigManager.GUI_TITLE);
		
		PlayerBlockData data = SBDManager.get(player.getName());
		Bukkit.getScheduler().runTaskAsynchronously(OreStorage.main, () -> {
			ConfigManager.GUIITEMS.forEach((slot, gi) -> {
				inv.setItem(slot, getIcon(gi, data, player.getName()));
			});
			if (ConfigManager.SELL_ENABLE) inv.setItem(inv.getSize() - 1, getSellButton());
		});
		
		return inv;
	}
	
	public static ItemStack getIcon(SBDGUIItem gi, PlayerBlockData data, String player) {
		ItemStack item = new ItemStack(gi.material);
		if (gi.material == Material.INK_SACK) item.setDurability((short) 4);
		ItemMeta meta = item.getItemMeta();
		
		List<String> lore = new ArrayList<String> ();
//		lore.add("§aSố lượng: §7" + data.getAmount(gi.material) + "/" + getMax(player));
		lore.add("§aSố lượng: §7" + data.getAmount(gi.material));
		lore.add("§cChuột trái: §fLấy " + gi.leftClick + "");
		lore.add("§cChuột phải: §fLấy " + gi.rightClick + "");
		lore.add("§cShift + C.trái: §fLấy " + gi.shiftLeftClick + "");
		lore.add("§cShift + C.phải: §fLấy " + gi.shiftRightClick + "");
		lore.add("§cChuột giữa: §fBỏ hết vào kho");
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack getSellButton() {
		ItemStack is = new ItemStack(Material.CHEST);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("§6§lBán");
		List<String> lore = new ArrayList<String> ();
		lore.add("§7Click để mở menu bán");
		meta.setLore(lore);
		is.setItemMeta(meta);
		return is;
	}
	
	public static String getOreName(Material material) {
		if (material == Material.INK_SACK) return "Lapis lazuli";
		return Utils.firstCharUppercase(material.name().toLowerCase()).replace("_", " ");
	}
}
