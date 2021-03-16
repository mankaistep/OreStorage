package me.manaki.plugin.orestorage.main;

import me.manaki.plugin.orestorage.OreStorage;
import me.manaki.plugin.orestorage.object.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;


public class EventHandling implements Listener {
	
	@EventHandler
	public void onQuitt(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Utils.saveMaxStorage(p.getName());
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Block b = e.getBlock();
		b.setMetadata("placed", new FixedMetadataValue(OreStorage.main, ""));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (e.isCancelled()) return;
		if (!isInRightPlace(player)) return;
		
		Block block = e.getBlock();
		Material m = block.getType();
		if (!ConfigManager.ALLOWED_BLOCKS.contains(m)) return;
		e.setDropItems(false);
		
		if (!isInOwnIsland(player)) {
			player.sendMessage("§cĐây không phải Island của bạn!");
			return;
		}
		
		// Replace
		Material material = ConfigManager.REPLACED_BLOCKS.containsKey(m) ? ConfigManager.REPLACED_BLOCKS.get(m) : m;
		
		Bukkit.getScheduler().runTaskAsynchronously(OreStorage.main, () -> {
			// Check full
			if (!SBDManager.canAddMore(player, material)) {
				player.sendTitle("§c§lĐẦY KHO", "§7Kho đồ cho " + SBDManager.getOreName(material) + " đã đầy, hãy lấy bớt ra", 10, 40, 10);
				player.sendMessage("§aĐể gia tăng giới hạn, hãy lên rank cao hơn");
				return;
			}
			
			PlayerBlockData data = SBDManager.getData(player.getName());
			int amount = getOreAmount(player, material);
			data.addBlock(material, amount);
			SBDManager.checkMatch(player.getName());
			
			player.sendTitle("", "§f+" + amount + " §a" + Utils.getTrans(material) + " §fvào /kho" , 0, 15, 0);
			
		});
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(OreStorage.main, () -> {
			SBDManager.load(player.getName());
		});
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(OreStorage.main, () -> {
			SBDManager.save(player.getName());
		});
	}
	
//	@EventHandler
//	public void onCommand(PlayerCommandPreprocessEvent e) {
//		String cmd = e.getMessage();
//		if (cmd.replace("/", "").equalsIgnoreCase(ConfigManager.GUI_OPEN_CMD)) {
//			e.setCancelled(true);
//			Player player = e.getPlayer();
//			player.openInventory(SBDManager.getGUI(player));
//		}
//	}
	
	@EventHandler
	public void onClick2(InventoryClickEvent e) {
		if (ConfigManager.SELL_ENABLE) SellGUI.eventClick(e);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!e.getView().getTitle().equals(ConfigManager.GUI_TITLE)) return;
		e.setCancelled(true);
		if (e.getClickedInventory() != e.getWhoClicked().getOpenInventory().getTopInventory()) return;
		
		int slot = e.getSlot();
		Player viewer = (Player) e.getWhoClicked();
		
		if (slot == e.getInventory().getSize() - 1 && e.getInventory().getItem(slot) != null && ConfigManager.SELL_ENABLE) {
			SellGUI.open(viewer);
			return;
		}
		
		boolean isRightClick = e.getClick() == ClickType.RIGHT;
		boolean isLeftClick = e.getClick() == ClickType.LEFT;
		boolean isShiftRightClick = e.getClick() == ClickType.SHIFT_RIGHT;
		boolean isShiftLeftClick = e.getClick() == ClickType.SHIFT_LEFT;
		
		String player = ((DitMeMayHolder) e.getInventory().getHolder()).getOwner();
		
		if (ConfigManager.GUIITEMS.containsKey(slot)) {
			PlayerBlockData data = SBDManager.getData(player);
			SBDGUIItem item = ConfigManager.GUIITEMS.get(slot);
			int amount = data.getAmount(item.material);
			
			// If Inventory is full
			if (viewer.getInventory().firstEmpty() == -1) {
				viewer.sendMessage("§cKho đồ đầy, hãy để ra ô trống đủ để nhận");
				return;
			}
			
			if (isShiftLeftClick) {
				if (amount < item.shiftLeftClick) {
					viewer.sendMessage("§cTrong kho ít hơn số lượng lấy ra!");
					return;
				}
				data.addBlock(item.material, -1 * item.shiftLeftClick);
				viewer.getInventory().addItem(Utils.getItem(item.material, item.shiftLeftClick));
			}
			if (isShiftRightClick) {
				if (amount < item.shiftRightClick) {
					viewer.sendMessage("§cTrong kho ít hơn số lượng lấy ra!");
					return;
				}
				data.addBlock(item.material, -1 * item.shiftRightClick);
				viewer.getInventory().addItem(Utils.getItem(item.material, item.shiftRightClick));
			}
			if (isLeftClick) {
				if (amount < item.leftClick) {
					viewer.sendMessage("§cTrong kho ít hơn số lượng lấy ra!");
					return;
				}
				data.addBlock(item.material, -1 * item.leftClick);
				viewer.getInventory().addItem(Utils.getItem(item.material, item.leftClick));
			}
			if (isRightClick) {
				if (amount < item.rightClick) {
					viewer.sendMessage("§cTrong kho ít hơn số lượng lấy ra!");
					return;
				}
				data.addBlock(item.material, -1 * item.rightClick);
				viewer.getInventory().addItem(Utils.getItem(item.material, item.rightClick));
			}
			if (e.getClick() == ClickType.MIDDLE) {
				// Check full
				int remain = SBDManager.getRemain(player, item.material);
				int a = getAmount(viewer, new ItemStack(item.material));
				
				if (remain > a) {
					// Có thể thêm hết
					removeItems(viewer, new ItemStack(item.material));
					data.addBlock(item.material, a);
				} else {
					// Không thể thêm hết
					removeItems(viewer, new ItemStack(item.material), remain);
					data.addBlock(item.material, remain);
					
					// Thông báo
					viewer.sendMessage("§cĐầy kho");
				}
			}
			e.getInventory().setItem(slot, SBDManager.getIcon(item, data, player));
			SBDManager.save(player);
		
		}
	}
	
	public int removeItems(Player player, ItemStack item) {
		if (item.getType() == Material.INK_SACK) item.setDurability((short) 4);
		item = item.clone();
		PlayerInventory inv = player.getInventory();
		ItemStack[] items = inv.getContents();
		int c = 0;
		for (int i = 0 ; i < items.length ; i++) {
			ItemStack is = items[i];
			if (is == null) continue;
			if (is.isSimilar(item)) {
				c += is.getAmount();
				items[i] = null;
			}
		}
		inv.setContents(items);
		player.updateInventory();
		return c;
	}
	
	public int removeItems(Player player, ItemStack item, int amount) {
		if (item.getType() == Material.INK_SACK) item.setDurability((short) 4);
		item = item.clone();
		PlayerInventory inv = player.getInventory();
		ItemStack[] items = inv.getContents();
		int c = 0;
		for (int i = 0 ; i < items.length ; i++) {
			ItemStack is = items[i];
			if (is == null) continue;
			if (is.isSimilar(item)) {
				if (c + is.getAmount() <= amount) {
					c += is.getAmount();
					items[i] = null;
				}
				else {
					int canDelete = amount - c;
					c = amount;
					is.setAmount(is.getAmount() - canDelete);
					items[i] = is;
					break;
				}
			}
		}
		inv.setContents(items);
		player.updateInventory();
		return c;
	}
	
	public static int getAmount(Player player, ItemStack item) {
		ItemStack clone = item.clone();
		if (clone.getType() == Material.INK_SACK) clone.setDurability((short) 4);;
		PlayerInventory inv = player.getInventory();
		ItemStack[] items = inv.getContents();
		int c = 0;
		for (int i = 0 ; i < items.length ; i++) {
			ItemStack is = items[i];
			if (is == null) continue;
			if (is.isSimilar(clone)) {
				c += is.getAmount();
			}
		}
		return c;
	}
	
	public static boolean isInRightPlace(Player player) {
		return ConfigManager.WORLDS.contains(player.getWorld().getName());
	}
	
	public static boolean isInOwnIsland(Player player) {
		return OreStorage.main.islandManager.isInOwnIsland(player);
	}
	
	public static int getLevelGiaTai(Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
				return meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
			}
		}
		return 0;
	}
	
	public static int getOreAmount(Player player, Material ore) {
		if (!ConfigManager.LOOT_BONUS_BLOCKS.contains(ore.name())) return 1;
		int giatai = getLevelGiaTai(player);
		if (giatai == 0) return 1;
		return Utils.randomInt(1, giatai + 1);
	}
	
}
