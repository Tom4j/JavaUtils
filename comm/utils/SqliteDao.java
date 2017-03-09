package com.siweidg.comm.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sqlite工具类 使用jdbc直接操作数据库 完成 select/update/delete/insert操作
 * 
 * @author qzsun
 * 
 */
public class SqliteDao {

	/**
	 * 获取连接
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnection(String dbName) {
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:" + dbName);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				conn = null;
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取执行SQL的工具
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            SQL语句
	 * @return prepStmt
	 */
	public static PreparedStatement getPrepStatement(Connection conn, String sql) {
		PreparedStatement prepStmt = null;

		try {
			prepStmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prepStmt;
	}

	/**
	 * 关闭数据库资源
	 * 
	 * @param prepStmt
	 */
	public static void closePrepStatement(PreparedStatement prepStmt) {
		if (null != prepStmt) {
			try {
				prepStmt.close();
			} catch (SQLException e) {
				prepStmt = null;
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取结果集
	 * 
	 * @param stmt
	 *            执行SQL的工具
	 * @param sql
	 *            SQL语句
	 * @return 结果集
	 */
	public static ResultSet getResultSet(Statement stmt, String sql) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 关闭数据库资源
	 * 
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @return
	 */
	public static List<Map> executeQuery(String dbName, String sql) {
		Connection connection = getConnection(dbName);
		PreparedStatement pst = getPrepStatement(connection, sql);
		ResultSet rs = null;
		try {
			rs = pst.executeQuery();
			List<Map> result = resultSetToList(rs);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭数据库资源
			closeResultSet(rs);
			closePrepStatement(pst);
			closeConnection(connection);
		}
		return null;

	}

	/**
	 * 转化resultset为list
	 * 
	 * @param rs
	 * @return
	 * @throws java.sql.SQLException
	 */
	public static List<Map> resultSetToList(ResultSet rs)
			throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List<Map> list = new ArrayList<Map>();
		Map rowData = new HashMap();
		while (rs.next()) {
			rowData = new HashMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				// System.out.println(rs.getBlob("img"));
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
			System.out.println("list:" + list.toString());
		}
		return list;
	}

	/**
	 * 执行insert update delete
	 * 
	 * @param sql
	 */
	public static void executeUpdate(String dbName, String sql) {
		Connection connection = getConnection(dbName);
		PreparedStatement pst = getPrepStatement(connection, sql);
		try {
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭数据库资源
			closePrepStatement(pst);
			closeConnection(connection);
		}
	}

}
