package com.example.orderplatform;

// 显示菜详细信息的dialog

import adapter.Menu;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuItemDialog extends Dialog {

	public MenuItemDialog(Context context, Menu menu) {
		super(context, R.style.dialog);
		// 加载布局并给布局的控件设置点击事件
		View view = getLayoutInflater().inflate(R.layout.dialog_menu, null);
		TextView name = (TextView) view.findViewById(R.id.dialog_menu_name);
		ImageView pic = (ImageView) view.findViewById(R.id.dialog_menu_pic);
		TextView summary = (TextView) view.findViewById(R.id.dialog_menu_summary);
		TextView price = (TextView) view.findViewById(R.id.dialog_menu_price);
		
		name.setText(menu.getName());
		pic.setBackgroundResource(menu.getPic());
		summary.setText(menu.getSummary());
		price.setText(menu.getPrice());
		
		super.setContentView(view);
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 预先设置Dialog的一些属性
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		WindowManager m = getWindow().getWindowManager();
		Display d = m.getDefaultDisplay();
		getWindow().setAttributes(p);
		p.height = (int) (d.getHeight() * 0.8);
		p.width = d.getWidth();
		p.gravity = Gravity.LEFT | Gravity.BOTTOM;
		dialogWindow.setAttributes(p);
	}

}
