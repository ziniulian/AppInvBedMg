package com.invengo.rfid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读写器基类
 * Created by LZR on 2017/8/8.
 */

public abstract class Base implements InfBaseRfid {
	private List<com.invengo.rfid.tag.Base> ts = new ArrayList<>();	// 标签集
	private InfTagListener itl = null;
	private boolean hex = false;	// 使用二进制数据
	private EmPushMod pm = EmPushMod.Event;

	@Override
	public void setTagListenter(InfTagListener l) {
		this.itl = l;
	}

	@Override
	public String catchScanning() {
		return catchScanning(false);
	}

	// 获取有扫描次数的标签集
	public String catchScanning(boolean epc) {
 		StringBuilder r = new StringBuilder();
		Map<String, com.invengo.rfid.tag.Base> m = new HashMap<>();
		String s;
		int i, n;
		n = ts.size();

		for (i = 0; i < n; i ++) {
			if (epc) {
				if (hex) {
					s = ts.get(i).getEpcHexstr();
				} else {
					s = ts.get(i).getEpcDat();
				}
			} else {
				s = ts.get(i).getTidHexstr();
			}
			if (m.containsKey(s)) {
				m.get(s).addOneTim();
			} else {
				m.put(s, ts.get(i));
			}
		}
		clearScanning();

		r.append('{');
		for (Map.Entry<String, com.invengo.rfid.tag.Base> entry : m.entrySet()) {
			r.append('\"');
			r.append(entry.getKey());
			r.append('\"');
			r.append(':');
			r.append(entry.getValue().toJson(hex));
			r.append(',');
		}

		n = r.length();
		if (n > 1) {
			r.deleteCharAt(n-1);
		}
		r.append('}');
		return r.toString();
	}

	public Base setPm(EmPushMod pm) {
		this.pm = pm;
		return this;
	}

	public Base setHex(boolean hex) {
		this.hex = hex;
		return this;
	}

	protected EmPushMod getPm() {
		return pm;
	}

	public boolean isHex() {
		return hex;
	}

	// 获取标签集
	public List<com.invengo.rfid.tag.Base> getScanning () {
		return ts;
	}

	// 清空标签集
	public synchronized void clearScanning() {
		ts.clear();
	}

	// 添加标签
	private synchronized void appendReadTag (com.invengo.rfid.tag.Base bt) {
		ts.add(bt);
	}

	// 回调
	protected void cb (EmCb e, String... args) {
		if (itl != null) {
			itl.cb(e, args);
		}
	}

	// 读到标签时的触发事件
	protected void onReadTag (com.invengo.rfid.tag.Base bt) {
		if (pm != EmPushMod.Event) {
			appendReadTag(bt);
		}
		if (pm != EmPushMod.Catch && itl != null) {
			itl.onReadTag(bt, itl);
		}
	}

	// 写完标签时的触发事件
	protected void onWrtTag (com.invengo.rfid.tag.Base bt) {
		if (itl != null) {
			itl.onWrtTag(bt, itl);
		}
	}
}
