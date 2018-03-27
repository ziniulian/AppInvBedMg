package com.invengo.bedmg.enums;

/**
 * Created by LZR on 2017/9/6.
 */

public enum EmSql {
	GetAll("select * from total"),	// 获取所有数据
	ByTim("select * from total where tim = ?"),	// 通过时间获取数据
	ByNum("select * from total where num = ?"),	// 通过车号获取数据
	GetAllNum("select distinct num from total"),	// 获取所有车号
	FindNum("select distinct num from total where num like ?"),	// 通过车号获取数据
	FindDetails("select sn, ct, num from <0> where tim = ? and typ = ?");	// 查询明细

	private final String sql;
	EmSql(String s) {
		sql = s;
	}
	@Override
	public String toString() {
		return sql;
	}
}
