package com.invengo.bedmg.entity;

import android.webkit.JavascriptInterface;

import com.invengo.bedmg.action.MainActivity;
import com.invengo.bedmg.dao.DbCsv;
import com.invengo.bedmg.enums.EmUh;
import com.invengo.bedmg.enums.EmUrl;
import com.invengo.rfid.EmCb;
import com.invengo.rfid.EmPushMod;
import com.invengo.rfid.InfTagListener;
import com.invengo.rfid.xc2910.Rd;

/**
 * Created by LZR on 2017/9/1.
 */

public class Web {
	private Rd rfd = new Rd();
	private MainActivity ma;
	private String user = null;
	private DbCsv db = new DbCsv();

	// 初始化
	public void init (MainActivity m) {
		ma = m;

		// 读写器设置
//		rfd.setBank("epc");
		rfd.setHex(true);
		rfd.setPm(EmPushMod.Catch);
		rfd.setTagListenter(new InfTagListener() {
			@Override
			public void onReadTag(com.invengo.rfid.tag.Base bt, InfTagListener itl) {}

			@Override
			public void onWrtTag(com.invengo.rfid.tag.Base bt, InfTagListener itl) {
				ma.sendUrl(EmUrl.WrtOk);
			}

			@Override
			public void cb(EmCb e, String[] args) {
//Log.i("-c-", e.name());
				switch (e) {
					case Scanning:
						ma.sendUrl(EmUrl.Scan);
						break;
					case Stopped:
						ma.sendUrl(EmUrl.Stop);
						break;
					case ErrWrt:
						ma.sendUrl(EmUrl.WrtErr);
						break;
					case ErrConnect:
						ma.sendUrl(EmUrl.Err);
						break;
					case Connected:
						ma.sendUh(EmUh.Connected);
						break;
				}
			}
		});
		rfd.init();
	}

	public void open() {
		rfd.open();
		db.open();
	}

	public void close() {
		rfd.close();
		db.close();
	}

	public boolean isBusy() {
		return rfd.isBusy();
	}

/*----------------------------------------*/

	@JavascriptInterface
	public void scan() {
		rfd.scan();
	}

	@JavascriptInterface
	public void stop() {
		rfd.stop();
	}

	@JavascriptInterface
	public String catchScanning() {
		return rfd.catchScanning(true);
	}

	@JavascriptInterface
	public void wrt (String bankNam, String dat, String tid) {
		rfd.wrt(bankNam, dat, tid);
	}

/*----------------------------------------*/

	// 获取单位
	@JavascriptInterface
	public String getUnit() {
		return "XXXXX";
	}

	// 获取车次
	@JavascriptInterface
	public String getTrip() {
		return "XXXXX";
	}

	// 保存记录
	@JavascriptInterface
	public void save(String msg) {
		db.insert("total", msg, false);
	}

	// 保存中文记录
	@JavascriptInterface
	public void save2(String msg) {
		db.insert("清点记录", msg, false);
	}

	// 保存明细
	@JavascriptInterface
	public void saveDetails(String filNam, String msg) {
		db.insert(filNam, "tim,typ,sn,ct,num\n", true);
		db.insert(filNam, msg, false);
	}

	// 提示音
	@JavascriptInterface
	public void sound() {
		ma.sendUh(EmUh.Sound);
//		ma.mkNtf("30");
	}

	// 通过车号查询结果
	@JavascriptInterface
	public String qry (String num, long min, long max) {
		return db.qry(num, min, max);
	}

	// 查询车号
	@JavascriptInterface
	public String findNum (String num) {
		return db.findNum(num);
	}

	// 查询明细
	@JavascriptInterface
	public String findDetails (String tim, String typ, String filNam) {
		return db.findDetails(tim, typ, filNam);
	}

	// 设置日期
	@JavascriptInterface
	public void showDatePicker (int y, int m, int d, boolean min) {
		ma.sendDate(y, m, d, min);
	}

}
