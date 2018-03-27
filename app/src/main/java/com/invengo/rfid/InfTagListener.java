package com.invengo.rfid;

/**
 * 事件
 * Created by LZR on 2017/8/7.
 */

public interface InfTagListener {
	public void onReadTag (com.invengo.rfid.tag.Base bt, InfTagListener itl);
	public void onWrtTag (com.invengo.rfid.tag.Base bt, InfTagListener itl);
	public void cb (EmCb e, String[] args);
}
