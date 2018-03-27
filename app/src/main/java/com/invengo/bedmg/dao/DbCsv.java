package com.invengo.bedmg.dao;

import android.os.Environment;

import com.invengo.bedmg.enums.EmSql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import static com.invengo.bedmg.enums.EmSql.ByNum;
import static com.invengo.bedmg.enums.EmSql.ByTim;
import static com.invengo.bedmg.enums.EmSql.GetAll;

/**
 * CSV数据库
 * Created by LZR on 2017/9/5.
 */

public class DbCsv {
	/**
	 * CSV JDBC驱动
	 */
	private static final String CSV_JDBC_DRIVER = "org.relique.jdbc.csv.CsvDriver";
	/**
	 * jdbc连接csv的 Header
	 */
	private static final String CSV_JDBC_HEADER = "jdbc:relique:csv:";
	/**
	 * separator 参数设置: CSV 文件中数据分割符
	 */
	private static final String CSV_PROP_SEPARATOR = "separator";
	/**
	 * separator 参数设置: 首行包含数据否
	 */
	private static final String CSV_PROP_SUPHEADER = "suppressHeaders";
	/**
	 * fileExtension 参数设置: 文件类型
	 */
	private static final String CSV_PROP_FILEEXTEN = "fileExtension";
	/**
	 * charset 参数设置: 字符集
	 */
	private static final String CSV_PROP_CHARSET = "charset";

	private Connection con;
	private Properties props;
	private String sdDir = "Invengo/BadMg/DB/CSV/";		// CSV文件所在目录
	private String url;	// 连接字

	public DbCsv () {
		// 解析CSV前的一些准备工作：解析参数设置
		props = new java.util.Properties();
		// 该CSV的数据是由','分隔
		props.put(CSV_PROP_SEPARATOR, ",");
		// 首行(去掉上面头行后的第一行)包含数据
		props.put(CSV_PROP_SUPHEADER, "false");
//		// 首行不包含数据
//		props.put(CSV_PROP_SUPHEADER, "true");
		// 要解析的文件类型
		props.put(CSV_PROP_FILEEXTEN, ".csv");
		// 字符集
		props.put(CSV_PROP_CHARSET, "UTF-8");

		// 创建数据库路径
		File d = new File(Environment.getExternalStorageDirectory(), sdDir);
		if (!d.exists()) {
			d.mkdirs();
		}
		url = CSV_JDBC_HEADER + d.getAbsolutePath();

		// 创建数据表
		crtTab();
	}

	// 连接数据库
	public boolean open () {
		boolean r = false;
		try {
			// 加载CSV-JDBC驱动
			Class.forName(CSV_JDBC_DRIVER);
			con = DriverManager.getConnection(url, props);
			r = !con.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	// 关闭数据库
	public void close () {
		try {
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 创建数据表
	private void crtTab () {
		insert("total", "tim,num,cnt01,cnt02,cnt03\n", true);
		if (bom("清点记录")) {
			insert("清点记录", "时间,车号,小单,被套,枕套,总计\n", false);
		}
	}

	// 写入数据
	public void insert(String tnam, String msg, boolean crt) {
		File f = new File(Environment.getExternalStorageDirectory(), sdDir + tnam + ".csv");
		boolean b = f.exists();
		if ((!crt && b) || (crt && !b)) {
			try {
				FileWriter w = new FileWriter(f, true);
				w.write(msg);
				w.flush();
				w.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 加BOM
	private boolean bom(String tnam) {
		File f = new File(Environment.getExternalStorageDirectory(), sdDir + tnam + ".csv");
		if (!f.exists()) {
			try {
				FileOutputStream os = new FileOutputStream(f);
				os.write(new byte[] {(byte)239, (byte)187, (byte)191});	// EF BB BF
				os.flush();
				os.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private String getStr (ResultSet rs, String field) {
		String s = null;
		try {
			s = rs.getString(field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (s != null) {
			return '\"' + s + '\"';
		} else {
			return null;
		}
	}

	// 通过车号查询结果
	public String qry (String num, long min, long max) {
		StringBuilder r = new StringBuilder();
		r.append('[');
		try {
			PreparedStatement sql;
			if (num == null) {
				if (min == max) {
					sql = con.prepareStatement(ByTim.toString());
					sql.setString(1, min + "");
				} else {
					sql = con.prepareStatement(GetAll.toString());
				}
			} else {
				sql = con.prepareStatement(ByNum.toString());
				sql.setString(1, num);
			}
			ResultSet rs = sql.executeQuery();
			long t;
			while (rs.next()) {
				t = rs.getLong("tim");
				if (t >= min && t <= max) {
					r.append('[');
					r.append(getStr(rs, "tim"));
					r.append(',');
					r.append(getStr(rs, "num"));
					r.append(',');
					r.append(rs.getInt("cnt01"));
					r.append(',');
					r.append(rs.getInt("cnt02"));
					r.append(',');
					r.append(rs.getInt("cnt03"));
					r.append(']');
					r.append(',');
				}
			}
			int n = r.length();
			if (n > 1) {
				r.deleteCharAt(n - 1);
			}
			rs.close();
			sql.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		r.append(']');
		return r.toString();
	}

	// 查询车号
	public String findNum (String num) {
		StringBuilder r = new StringBuilder();
		r.append('[');
		try {
			PreparedStatement sql;
			if (num == null) {
				sql = con.prepareStatement(EmSql.GetAllNum.toString());
			} else {
				sql = con.prepareStatement(EmSql.FindNum.toString());
				sql.setString(1, "%" + num + "%");
			}
			ResultSet rs = sql.executeQuery();
			while (rs.next()) {
				r.append(getStr(rs, "num"));
				r.append(',');
			}
			int n = r.length();
			if (n > 1) {
				r.deleteCharAt(n - 1);
			}
			rs.close();
			sql.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		r.append(']');
		return r.toString();
	}

	// 查询明细
	public String findDetails (String tim, String typ, String filNam) {
		StringBuilder r = new StringBuilder();
		r.append('[');
		try {
			PreparedStatement sql= con.prepareStatement(EmSql.FindDetails.toString().replace("<0>", filNam));
			sql.setString(1, tim);
			sql.setString(2, typ);
			ResultSet rs = sql.executeQuery();
			while (rs.next()) {
				r.append('[');
				r.append(rs.getLong("sn"));
				r.append(',');
				r.append(rs.getLong("ct"));
				r.append(',');
				r.append(rs.getString("num"));
				r.append(']');
				r.append(',');
			}
			int n = r.length();
			if (n > 1) {
				r.deleteCharAt(n - 1);
			}
			rs.close();
			sql.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		r.append(']');
		return r.toString();
	}

}
