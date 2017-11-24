package com.example.orderplatform;

// 主界面——登陆成功后的界面

import java.util.ArrayList;
import java.util.List;

import db.DBHelper;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {

	private static final int HOME = 1;
	private static final int CART = 2;
	private static final int ORDER = 3;
	private static final int USER = 4;
	public String ReserveDate = null;
	public String CurrentUserName = null;

	private List<adapter.Menu> hasOrdered = new ArrayList<adapter.Menu>();
	private HomeFragment home;
	private CartFragment cart;
	private OrderFragment order;
	private UserFragment user;
	private RadioGroup tab;

	private int current_tab = -1;
	private DBHelper dbhelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 默认不弹出软键盘
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		Intent intent = getIntent();
		// 得到当前登录的用户用户名
		CurrentUserName = intent.getStringExtra("current_username");
		dbhelper = new DBHelper(MainActivity.this, "master.db", null, 1);
		// 初始化RadioGroup
		initTab();

	}

	// 接收数据从DateActivity传回的数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (1 == requestCode) {
			// 记录预定用餐时间
			ReserveDate = data.getStringExtra("ReserveDate");
			Log.d("lalala--date->main--ReserveDate", ReserveDate);
		}
		
	}

	private void initTab() {
		current_tab = HOME;
		if (null == home) {
			home = new HomeFragment();
		}
		getFragmentManager().beginTransaction()
				.replace(R.id.main_content, home).commit();
		tab = (RadioGroup) findViewById(R.id.main_tab_menu);
		tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.main_menu_home:
					if (null == home) {
						home = new HomeFragment();
					}
					if (CART == current_tab) {
						CartFragment cart = (CartFragment) getFragmentManager()
								.findFragmentById(R.id.main_content);
						hasOrdered = cart.getList_menus();
						Log.d("lalala--cart->main--hasOrdered",
								String.valueOf(hasOrdered.size()));
					}
					getFragmentManager().beginTransaction()
							.replace(R.id.main_content, home).commit();
					current_tab = HOME;
					break;
				case R.id.main_menu_cart:
					if (null == cart) {
						cart = new CartFragment();
					}
					if (HOME == current_tab) {
						HomeFragment home = (HomeFragment) getFragmentManager()
								.findFragmentById(R.id.main_content);
						hasOrdered = home.getHasOrderedToCart();
						Log.d("lalala--home->main--hasOrdered",
								String.valueOf(hasOrdered.size()));
					}
					getFragmentManager().beginTransaction()
							.replace(R.id.main_content, cart).commit();
					current_tab = CART;
					break;
				case R.id.main_menu_order:
					if (null == order) {
						order = new OrderFragment();
					}
					getFragmentManager().beginTransaction()
							.replace(R.id.main_content, order).commit();
					current_tab = ORDER;
					break;
				case R.id.main_menu_user:
					if (null == user) {
						user = new UserFragment();
					}
					getFragmentManager().beginTransaction()
							.replace(R.id.main_content, user).commit();
					current_tab = USER;
					break;
				default:
					break;
				}

			}
		});
	}

	public List<adapter.Menu> getHasOrdered() {
		return hasOrdered;
	}

	public void setCurrent_tab(int current_tab) {
		this.current_tab = current_tab;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		// switch(current_tab)
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
