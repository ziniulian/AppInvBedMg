package com.invengo.rfid.tag;

/**
 * 6C型标签（小纸条）
 * Created by LZR on 2017/8/10.
 */

public class T6Cnote extends Base {
	@Override
	protected Typ getTyp() {
		return Typ.get("6Cnote");
	}
}
