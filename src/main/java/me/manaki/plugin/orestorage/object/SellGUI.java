package me.manaki.plugin.orestorage.object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.manaki.plugin.orestorage.OreStorage;
import me.manaki.plugin.orestorage.main.MoneyAPI;
import me.manaki.plugin.orestorage.main.Utils;

public class SellGUI {
	
	public static void open(Player player) {
		Inventory inv = Bukkit.createInventory(new SellHolder(), ConfigManager.GUI_SIZE, "§0§lBÁN");
		PlayerBlockData data = SBDManager.get(player.getName());
		Bukkit.getScheduler().runTaskAsynchronously(OreStorage.main, () -> {
			ConfigManager.GUIITEMS.forEach((slot, gi) -> {
				inv.setItem(slot, getIcon(gi, data, player));
			});
		});
		player.openInventory(inv);
	}
	
	public static void eventClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof SellHolder == false) return;
		e.setCancelled(true);
		if (e.getClickedInventory() != e.getWhoClicked().getOpenInventory().getTopInventory()) return;
		
		int slot = e.getSlot();
		boolean isRightClick = e.getClick() == ClickType.RIGHT;
		boolean isLeftClick = e.getClick() == ClickType.LEFT;
		boolean isShiftRightClick = e.getClick() == ClickType.SHIFT_RIGHT;
		boolean isShiftLeftClick = e.getClick() == ClickType.SHIFT_LEFT;
		
		if (ConfigManager.GUIITEMS.containsKey(slot)) {
			Player player = (Player) e.getWhoClicked();
			PlayerBlockData data = SBDManager.getData(player.getName());
			SBDGUIItem item = ConfigManager.GUIITEMS.get(slot);
			int amount = data.getAmount(item.material);
			
			Bukkit.getScheduler().runTaskAsynchronously(OreStorage.main, () -> {
				int takeAmount = -1;
				if (e.getClick() != ClickType.MIDDLE) {
					if (isShiftLeftClick) {
						if (amount < item.shiftLeftClick) {
							player.sendMessage("§cTrong kho ít hơn số lượng muốn bán");
							return;
						}
						takeAmount = 1 * item.shiftLeftClick;
					}
					if (isShiftRightClick) {
						if (amount < item.shiftRightClick) {
							player.sendMessage("§cTrong kho ít hơn số lượng muốn bán");
							return;
						}
						takeAmount = 1 * item.shiftRightClick;
					}
					if (isLeftClick) {
						if (amount < item.leftClick) {
							player.sendMessage("§cTrong kho ít hơn số lượng muốn bán");
							return;
						}
						takeAmount = 1 * item.leftClick;
					}
					if (isRightClick) {
						if (amount < item.rightClick) {
							player.sendMessage("§cTrong kho ít hơn số lượng muốn bán");
							return;
						}
						takeAmount = 1 * item.rightClick;
					}
				}
				
				if (e.getClick() == ClickType.MIDDLE) {
					// Check full
					takeAmount = amount;
				}
				
				// Take
				if (takeAmount != -1) {
					data.addBlock(item.material, -1 * takeAmount);
					double price = ConfigManager.SELL_PRICES.getOrDefault(item.material, 0d) * takeAmount;
					MoneyAPI.giveMoney(player, price);
					player.sendMessage("§aNhận §f" + price + "$ §akhi bán " + takeAmount + "x " + Utils.getTrans(item.material));
				}
				
				e.getInventory().setItem(slot, getIcon(item, data, player));
			});
			
		}
	}
	
	public static ItemStack getIcon(SBDGUIItem gi, PlayerBlockData data, Player player) {
		ItemStack item = new ItemStack(gi.material);
		if (gi.material == Material.INK_SACK) item.setDurability((short) 4);
		ItemMeta meta = item.getItemMeta();
		double price = ConfigManager.SELL_PRICES.getOrDefault(gi.material, 0d);
		meta.setDisplayName("§2" + Utils.getTrans(gi.material) + " §f(" + price + "$)");
		List<String> lore = new ArrayList<String> ();
		lore.add("§aSố lượng: §7" + data.getAmount(gi.material));
		lore.add("§cChuột trái: §fBán " + gi.leftClick + "");
		lore.add("§cChuột phải: §fBán " + gi.rightClick + "");
		lore.add("§cShift + C.trái: §fBán " + gi.shiftLeftClick + "");
		lore.add("§cShift + C.phải: §fBán " + gi.shiftRightClick + "");
		lore.add("§cChuột giữa: §fBán hết");
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		
		return item;
	}
}

class SellHolder implements InventoryHolder {

	@Override
	public Inventory getInventory() {
		return null;
	}
	
}
