package com.example.orderplatform;

// 显示用户的Fragment

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

public class UserFragment extends Fragment {

	private MainActivity main;
	private View view;
	private TextView welcome;
	private TextView userOrder;
	private TextView alterPassword;
	private TextView quit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.user, container, false);

		welcome = (TextView) view.findViewById(R.id.user_username);
		userOrder = (TextView) view.findViewById(R.id.user_order);
		alterPassword = (TextView) view.findViewById(R.id.user_alterpassword);
		quit = (TextView) view.findViewById(R.id.user_quit);
		
		main = (MainActivity) getActivity();
		welcome.setText("欢迎您，" + main.CurrentUserName);
		userOrder.setOnClickListener(new userOnClickListener() );
		alterPassword.setOnClickListener(new userOnClickListener() );
		quit.setOnClickListener(new userOnClickListener() );
		
		return view;
	}

	public class userOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.user_order:
				// 历史订单按钮的监听
				RadioButton tab_order = (RadioButton) main.findViewById(R.id.main_menu_order);
				tab_order.setChecked(true);
				break;
			case R.id.user_alterpassword:
				// 修改密码按钮的监听
				AlterPasswordDialog dialog = new AlterPasswordDialog(main, main.CurrentUserName);
		        dialog.show();
				break;
			case R.id.user_quit:
				// 注销登录按钮的监听
				Intent intent = new Intent();
				intent.setClass(main, LoginActivity.class);
				main.startActivity(intent);
				main.finish();
				break;
			default:
				break;
			}
		}

	}

}
