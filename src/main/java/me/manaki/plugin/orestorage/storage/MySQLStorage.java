package me.manaki.plugin.orestorage.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import me.manaki.plugin.orestorage.object.PlayerBlockData;

public class MySQLStorage {
	
	public static Connection connection;
	
	public static void init(String host, String port, String name, String user, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + host + ":" + port + "/" + name + "?autoReconnect=true";
			connection = DriverManager.getConnection(url, user, password);
			createTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean ifTableExist(String name) {
		try {
			ResultSet tables = connection.getMetaData().getTables(null, null, name, null);
			if (tables.next()) return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static Map<String, String> toStringStringMap(Map<Material, Integer> input) {
		Map<String, String> map = new HashMap<String, String> ();
		input.forEach((r, s) -> {
			map.put(r.name(), s + "");
		});
		return map;
	}
	
	public static String toString(Map<String, String> map) {
		String s = "";
		for (String s1 : map.keySet()) {
			String s2 = map.get(s1);
			s += s1 + "," + s2 + ";";
		}
		try {
			s = s.substring(0, s.length() - 1);
		}
		catch (StringIndexOutOfBoundsException  e) {
			return s;
		}

		return s;
	}
	
	public static Map<String, String> fromString(String s) {
		Map<String, String> map = new HashMap<String, String> ();
		try {
			String[] a = s.split(";");
			for (String element : a) {
				String[] value = element.split(",");
				try {
					map.put(value[0], value[1]);
				}
				catch (Exception e) {
					return map;
				}
			}
			return map;
		}
		catch (Exception e) {
			return map;
		}
	}
	
	public static Map<Material, Integer> fromStringMap(Map<String, String> input) {
		Map<Material, Integer> map = new HashMap<Material, Integer> ();
		
		input.forEach((s1, s2) -> {
			map.put(Material.valueOf(s1), Integer.parseInt(s2));
		});
		
		return map;
	}
	
	//
	
	public static void createTable() {
		Map<String, String> queries = new HashMap<String, String> ();
		queries.put("blockdata", "create table blockdata(PLAYER varchar(50) not null, DATA varchar(10000), primary key (PLAYER))");
		queries.forEach((table, cmd) -> {
			try {
				if (ifTableExist(table)) return;
				PreparedStatement ps = connection.prepareStatement(cmd);
				ps.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static boolean hasData(String player) {
		player = player.toLowerCase();
		String query = "select * from blockdata where PLAYER=?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, player);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void initData(String player) {
		player = player.toLowerCase();
		List<String> queries = new ArrayList<String> ();
		queries.add("insert into blockdata(PLAYER, DATA) values('" + player + "', '')");		
		queries.forEach(cmd -> {
			try {
				PreparedStatement ps = connection.prepareStatement(cmd);
				ps.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static PlayerBlockData getData(String player) {
		player = player.toLowerCase();
		String query = "select * from blockdata where PLAYER=?";
		PlayerBlockData data = new PlayerBlockData();
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, player);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				data = new PlayerBlockData(fromStringMap(fromString(rs.getString("DATA"))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static void saveData(String player, PlayerBlockData data) {
		if (!hasData(player)) initData(player);
		player = player.toLowerCase();
		String query = "update blockdata set DATA=? where PLAYER=?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, toString(toStringStringMap(data.getMap())));
			ps.setString(2, player);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void importToFile() {
		String query = "select * from blockdata";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PlayerBlockData data = new PlayerBlockData(fromStringMap(fromString(rs.getString("DATA"))));
				FileStorage.saveData(rs.getString("PLAYER"), data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
