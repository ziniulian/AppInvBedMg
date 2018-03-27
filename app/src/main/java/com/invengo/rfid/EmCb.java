package com.invengo.rfid;

/**
 * 回调信息
 * Created by LZR on 2017/8/9.
 */

public enum EmCb {
	ShowProgress,	// 显示进度条
	HidProgress,	// 隐藏进度条
	ShowToast,		// 信息提示
	Scanning,		// 开始扫描
	Stopped,		// 扫描已停止
	ErrWrt,		// 写入失败
	Connected,		// 与RFID设备已建立连接
	DisConnected,	// 与RFID设备已断开连接
	ErrConnect,		// 与RFID设备连接失败
	ErrRead			// 读取失败
}
