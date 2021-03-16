package me.manaki.plugin.orestorage.storage;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import mk.plugin.playerdata.storage.PlayerData;
import mk.plugin.playerdata.storage.PlayerDataAPI;

public class AllowedUsers {
	
	public static List<String> get(String player) {
		PlayerData pd = PlayerDataAPI.get(player, "orestorage");
		if (!pd.hasData("allowed-users")) return Lists.newArrayList();
		return Lists.newArrayList(pd.getValue("allowed-users").split(";")).stream().map(s -> s.toLowerCase()).collect(Collectors.toList());
	}
	
	public static void save(String player, List<String> list) {
		String s = "";
		for (String p : list) s += p + ";";
		if (s.length() > 1) s = s.substring(0, s.length() - 1);
		PlayerData pd = PlayerDataAPI.get(player, "orestorage");
		pd.set("allowed-users", s);
		pd.save();
	}
	
}
