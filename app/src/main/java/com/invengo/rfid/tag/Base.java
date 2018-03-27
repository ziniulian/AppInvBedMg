package com.invengo.rfid.tag;

import com.invengo.rfid.util.Str;

/**
 * 标签基类
 * Created by LZR on 2017/8/9.
 */

public abstract class Base {
	private byte[] epc = null;
	private byte[] tid = null;
	private byte[] use = null;
	private byte[] bck = null;
	private int tim = 1;	// 标签扫描到的次数

	public Base setEpc(byte[] epc) {
		this.epc = epc;
		return this;
	}

	public Base setEpc(String hex) {
//		return setEpc(Str.Hexstr2Bytes(hex, getTyp().getEwl()));
		return setEpc(Str.Hexstr2Bytes(hex));
	}

	public Base setEpcDat(String dat) {
		return setEpc(Str.getLimitBytes(dat, getTyp().getEwl()));
	}

	public Base setTid(byte[] tid) {
		this.tid = tid;
		return this;
	}

	public Base setTid(String hex) {
		return setTid(Str.Hexstr2Bytes(hex));
	}

	public Base setUse(byte[] use) {
//		this.use = use;
//		return this;
		return setUse(0, use);
	}

	public Base setUse(int offset, byte[] bs) {
		if (bs == null) {
			use = null;
		} else {
			int n = getTyp().getUwl();
			int s = bs.length;
			if (use == null) {
				use = new byte[n];
			}
			if ((s + offset) > n) {
				s = n - offset;
			}
			System.arraycopy(bs, 0, use, offset, s);
		}
		return this;
	}

	public Base setUse(String hex) {
		return setUse(Str.Hexstr2Bytes(hex));
	}

	public Base setUse(int offset, String hex) {
		return setUse(offset, Str.Hexstr2Bytes(hex));
	}

	public Base setUseDat(String dat) {
		return setUse(Str.getLimitBytes(dat, getTyp().getUwl()));
	}

	public Base setUseDat(int offset, String dat) {
		return setUse(Str.getLimitBytes(dat, (getTyp().getUwl() - offset)));
	}

	public Base setBck(byte[] bck) {
		this.bck = bck;
		return this;
	}

	public Base setBck(String hex) {
		return setBck(Str.Hexstr2Bytes(hex));
	}

	public byte[] getEpc() {
		return epc;
	}

	public String getEpcDat() {
		return Str.Bytes2Dat(epc);
	}

	public String getEpcHexstr() {
		return Str.Bytes2Hexstr(epc);
	}

	public byte[] getTid() {
		return tid;
	}

	public String getTidHexstr() {
		return Str.Bytes2Hexstr(tid);
	}

	public byte[] getUse() {
		return use;
	}

	public String getUseDat() {
		return Str.Bytes2Dat(use);
	}

	public String getUseHexstr() {
		return Str.Bytes2Hexstr(use);
	}

	public byte[] getBck() {
		return bck;
	}

	public String getBckDat() {
		return Str.Bytes2Dat(bck);
	}

	public String getBckHexstr() {
		return Str.Bytes2Hexstr(bck);
	}

	public String toJson(boolean isHex) {
		StringBuilder sb = new StringBuilder("{\"tim\":");
		sb.append(tim);
		sb.append(',');
		if (epc != null) {
			sb.append("\"epc\":");
			sb.append('\"');
			if (isHex) {
				sb.append(getEpcHexstr());
			} else {
				sb.append(getEpcDat());
			}
			sb.append('\"');
			sb.append(',');
		}
		if (tid != null) {
			sb.append("\"tid\":");
			sb.append('\"');
			sb.append(getTidHexstr());
			sb.append('\"');
			sb.append(',');
		}
		if (use != null) {
			sb.append("\"use\":");
			sb.append('\"');
			if (isHex) {
				sb.append(getUseHexstr());
			} else {
				sb.append(getUseDat());
			}
			sb.append('\"');
			sb.append(',');
		}
		if (bck != null) {
			sb.append("\"bck\":");
			sb.append('\"');
			if (isHex) {
				sb.append(getBckHexstr());
			} else {
				sb.append(getBckDat());
			}
			sb.append('\"');
			sb.append(',');
		}

		sb.deleteCharAt(sb.length()-1);
		sb.append('}');
		return sb.toString();
	}

	public int getTim() {
		return tim;
	}

	public void addOneTim () {
		tim ++;
	}

	protected abstract Typ getTyp ();

}
