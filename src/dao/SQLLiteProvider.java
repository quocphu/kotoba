package dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SQLLiteProvider {
	private static Connection connection = null;
//	private String dbName = "Chinook_Sqlite.sqlite";
	private String dbName = "kotoba.sqlite";

	public SQLLiteProvider() {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int execute(String sql) {
		
		try {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			System.err.println(sql);
			e.printStackTrace();
		}
		return -1;
	}

	public int execute(String sql, HashMap<String, Object> params) {
		try {
			Statement statement = connection.createStatement();
			sql = createSql(params, sql);
//			System.err.println(sql);
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	public int insert(String sql) {
		int key = -1;
		try {
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			key = statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return key;
	}

	public <T> List<T> select(Class<T> cl, String sql) {
		List<T> result = new ArrayList<T>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				result.add(createObject(cl, rs));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private <T> T createObject(Class<T> cl, ResultSet rs) {
		T t = null;
		try {
			t = cl.newInstance();

			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
				String name = rsmd.getColumnName(i);
				Field f = cl.getDeclaredField(hyphenToCamel(name));
				f.setAccessible(true);
				if(f.getType() == Date.class) {
					f.set(t, rs.getDate(name));
				} else if (f.getType() == Integer.class){
					f.set(t, rs.getInt(name));
				} else {
					f.set(t, rs.getObject(name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	private String hyphenToCamel(String hyphen) {
		// lesson
		// update_date --> updateDate;
		String[] m = hyphen.split("_");
		
		String result = "";
		for (int i = 0; i < m.length; i++) {
//			System.out.println("i: " + i + " " + m[i]);
			result += m[i].substring(0, 1).toUpperCase() + m[i].substring(1);
		}

		result = result.substring(0, 1).toLowerCase() + result.substring(1);
//		System.out.println("hyphenToCamel " + result);
		return result;
	}

	public <T> T getById(Class<T> cl, int id) {
		String sql = "";
		sql += "select * from " + cl.getName() + "where id=" + id;
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				return createObject(cl, rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public <T> int insert(HashMap<String, Object> params, String sql) {
		sql = createSql(params, sql);
		return execute(sql);
	}
	private String createSql(HashMap<String, Object> params, String sql) {
		String rs = sql;
		for(String key : params.keySet()) {
			Object data = params.get(key);
			if (data instanceof String && data != null) {
				rs = rs.replace("{" + key + "}", "'" + data.toString() + "'");
			} else {
				rs = rs.replace("{" + key + "}", data == null ? "null":data.toString());
			}
//			System.out.println("rs " + rs);
//			System.out.println(" key " + key);
		}
//		System.out.println("createSql: " + rs);
		return rs;
	}
	public int delete(String tableName, int id) {
		String sql = "delete from %s where id=%d";
		sql = String.format(sql, tableName, id);
		return execute(sql);
	}
}
