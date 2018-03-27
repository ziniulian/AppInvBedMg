package com.invengo.bedmg.action;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.invengo.bedmg.enums.EmUrl;

/**
 * 日期选择器
 * Created by LZR on 2017/9/18.
 */

public class DateSeter extends DialogFragment {
	private MainActivity ma;
	private DatePicker dp;
	private boolean min;
	private int y;
	private int m;
	private int d;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.date_seter, container);
//		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);	// 对话框隐藏Title

		ma = (MainActivity) getActivity();
		dp = (DatePicker) (view.findViewById(R.id.dpScd));
		Button btn = (Button) (view.findViewById(R.id.dpBtn));
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				String arg = min + ", \"" + dp.getYear() + '-' + (dp.getMonth() + 1) + '-' + dp.getDayOfMonth() + "\")";
				ma.sendUrl(EmUrl.SetDate, arg);
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		if (this.min) {
			getDialog().setTitle("设置起始日期");
		} else {
			getDialog().setTitle("设置结束日期");
		}
		dp.init(y, m, d, null);
		super.onResume();
	}

	// 参数设置
	public void setArg (Bundle b) {
		this.min = b.getBoolean("min");
		this.y = b.getInt("y");
		this.m = b.getInt("m");
		this.d = b.getInt("d");
	}
}
