package com.invengo.rfid.tag;

/**
 * 6C型标签（小方块）
 * Created by LZR on 2017/8/10.
 */

public class T6Cblock extends Base {
	@Override
	protected Typ getTyp() {
		return Typ.get("6Cblock");
	}
}
