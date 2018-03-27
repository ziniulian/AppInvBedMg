package com.invengo.bedmg.action;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.invengo.bedmg.entity.Web;
import com.invengo.bedmg.enums.EmUh;
import com.invengo.bedmg.enums.EmUrl;

public class MainActivity extends AppCompatActivity {
	private Web w = new Web();	// 读写器
	private WebView wv;
	private Handler uh = new UiHandler();

	// 声音
	private SoundPool sp = null;
	private int music;

	// 日期选择器
	private DateSeter ds = new DateSeter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); // 全屏、不锁屏
		setContentView(R.layout.activity_main);

		// 声音
		sp = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		music = sp.load(this, R.raw.biu, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

		// 读写器设置
		w.init(this);

		// 页面设置
		wv = (WebView)findViewById(R.id.wv);
		WebSettings ws = wv.getSettings();
		ws.setDefaultTextEncodingName("UTF-8");
		ws.setJavaScriptEnabled(true);
		wv.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		wv.addJavascriptInterface(w, "rfdo");

		sendUrl(EmUrl.Home);
	}

	@Override
	protected void onResume() {
		w.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		w.close();
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_SOFT_RIGHT:
				if (event.getRepeatCount() == 0) {
					EmUrl e = getCurUi();
					if (e != null) {
						switch (getCurUi()) {
							case Ascan:
								if (w.isBusy()) {
									w.stop();
								} else {
									w.scan();
								}
								break;
						}
					}
				}
				return true;
			case KeyEvent.KEYCODE_BACK:
				EmUrl e = getCurUi();
				if (e != null) {
					switch (e) {
						case Ascan:
						case Home:
							sendUrl(EmUrl.Back);
							break;
						case Qry:
							sendUrl(EmUrl.Home);
							break;
						case Exit:
						case Err:
							return super.onKeyDown(keyCode, event);
					}
				} else {
					wv.goBack();
				}
				return true;
			case 84:
				// 测试用
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 显示对话框
	public void showDialogFragment(DialogFragment df){
		FragmentTransaction mFragTransaction = getFragmentManager().beginTransaction();
		Fragment fragment =  getFragmentManager().findFragmentByTag("dialogFragment");
		if(fragment!=null){
			//为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
			mFragTransaction.remove(fragment);
		}
		df.show(mFragTransaction, "dialogFragment"); //显示一个Fragment并且给该Fragment添加一个Tag，可通过findFragmentByTag找到该Fragment
	}

	// 获取当前页面信息
	private EmUrl getCurUi () {
		try {
			return EmUrl.valueOf(wv.getTitle());
		} catch (Exception e) {
			return null;
		}
	}

	// 页面跳转
	public void sendUrl (EmUrl e) {
		uh.sendMessage(uh.obtainMessage(EmUh.Url.ordinal(), 0, 0, e.url()));
	}

	// 页面跳转
	public void sendUrl (EmUrl e, String obj) {
		uh.sendMessage(uh.obtainMessage(EmUh.Url.ordinal(), 0, 0, e.url() + obj));
	}

	// 发送页面处理消息
	public void sendUh (EmUh e) {
		uh.sendMessage(uh.obtainMessage(e.ordinal()));
	}

	// 日期选择
	public void sendDate (int y, int m, int d, boolean min) {
		Message msg = uh.obtainMessage(EmUh.Date.ordinal());
		Bundle b = msg.getData();
		b.putInt("y", y);
		b.putInt("m", m);
		b.putInt("d", d);
		b.putBoolean("min", min);
		uh.sendMessage(msg);
	}

	// 页面处理器
	private class UiHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			EmUh e = EmUh.values()[msg.what];
			switch (e) {
				case Url:
					wv.loadUrl((String)msg.obj);
					break;
				case Connected:
					if (getCurUi() == EmUrl.Err) {
						wv.goBack();
					}
					break;
				case Sound:
					sp.play(music, 1, 1, 0, 0, 1);
					break;
				case Date:
					ds.setArg(msg.getData());
					showDialogFragment(ds);
					break;
				default:
					break;
			}
		}
	}

}
