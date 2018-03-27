package com.invengo.bedmg.enums;

/**
 * Created by LZR on 2017/9/1.
 */

public enum EmUrl {
	Scan("javascript: rfid.scan()"),
	Stop("javascript: rfid.stop()"),
	Back("javascript: dat.back();"),
	WrtOk("javascript: rfid.hdWrt(true);"),
	WrtErr("javascript: rfid.hdWrt(false);"),
	SetDate("javascript: dat.chgDate("),
	Home("file:///android_asset/web/home.html"),
	Ascan("file:///android_asset/web/aScan.html"),
	Qry("file:///android_asset/web/qry.html"),
	Exit("file:///android_asset/web/home.html"),
	Err("file:///android_asset/web/err.html");

	private final String url;
	EmUrl(String u) {
		url = u;
	}
	public String url() {
		return url;
	}
}
