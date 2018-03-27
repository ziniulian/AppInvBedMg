package com.invengo.rfid.tag;

/**
 * 6C型标签（通用）
 * Created by LZR on 2017/8/9.
 */

public class T6C extends Base {
	@Override
	protected Typ getTyp() {
		return Typ.get("6C");
	}
}
