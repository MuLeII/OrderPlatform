package com.example.orderplatform;

// 显示菜单的Fragment

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import db.DBHelper;

import adapter.Menu;
import adapter.MenuAdapter;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomeFragment extends Fragment {

	// 要传到购物车的已点菜数据
	private List<Menu> hasOrderedToCart = new ArrayList<Menu>();
	// 从Activity获得的已点菜数据
	private List<Menu> hasOrderedFromMain = new ArrayList<Menu>();
	// ListView要显示的数据
	private List<Menu> list_menus = new ArrayList<Menu>();
	private MenuAdapter adapter;
	private DBHelper dbhelper;

	private View view;
	private RadioGroup radiogrowp_menu;
	private ListView lv_hot;
	private TextView menuTitle;
	private SearchView search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.home, container, false);
		
		// 默认不弹出软键盘
		getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		// 从MainActivity中获取已点菜数据
		MainActivity activity = (MainActivity) getActivity();
		
		hasOrderedFromMain = activity.getHasOrdered();
		Log.d("lalala--main->home--hasOrder",
				String.valueOf(hasOrderedFromMain.size()));
		// 获得要操作的组件
		search = (SearchView) view.findViewById(R.id.home_searchview);
		search.setOnQueryTextListener(new QueryTextListener());
		menuTitle = (TextView) view.findViewById(R.id.home_tv);
		menuTitle.setText("———特色菜———");
		lv_hot = (ListView) view.findViewById(R.id.home_hot_lv);
		list_menus.clear();
		// 从数据库中查询销量前十的特色菜
		getHotMenu();
		adapter = new MenuAdapter(view.getContext(), R.layout.item_menu,
				list_menus);
		adapter.hasOrdered = hasOrderedFromMain;
		lv_hot.setAdapter(adapter);
		hasOrderedToCart = adapter.getHasOrdered();
		radiogrowp_menu = (RadioGroup) view.findViewById(R.id.home_buttongroup);
		// 选择菜类的RadioGroup的监听
		radiogrowp_menu
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						switch (arg1) {
						case R.id.home_button_hot:
							menuTitle.setText("———特色菜———");
							list_menus.clear();
							// 从数据库中查询销量前十的特色菜
							getHotMenu();
							adapter.notifyDataSetChanged();
							break;
						case R.id.home_button_meat:
							menuTitle.setText("———荤菜———");
							list_menus.clear();
							// 从数据库中查询所有荤菜
							getMeatMenu();
							adapter.notifyDataSetChanged();
							break;
						case R.id.home_button_vege:
							menuTitle.setText("———素菜———");
							list_menus.clear();
							// 从数据库中查询所有素菜
							getVegeMenu();
							adapter.notifyDataSetChanged();
							break;
						case R.id.home_button_soup:
							menuTitle.setText("———汤———");
							list_menus.clear();
							// 从数据库中查询所有汤菜
							getSoapMenu();
							adapter.notifyDataSetChanged();
							break;
						default:
							break;
						}
					}
				});

		lv_hot.setOnItemClickListener(new LVOnItemClickListener());

		return view;
	}

	// listview的监听
	public class LVOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Menu menu = (Menu) lv_hot.getItemAtPosition(arg2);
			// 启动dialog查看食物详情
			MenuItemDialog dialog = new MenuItemDialog(view.getContext(), menu);
			dialog.show();
		}

	}

	// SearchView的监听
	public class QueryTextListener implements OnQueryTextListener {

		@Override
		public boolean onQueryTextChange(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String arg0) {
			// TODO Auto-generated method stub
			// 从数据库中模糊查询用户输入的菜品
			dbhelper = new DBHelper(view.getContext(), "master.db", null, 1);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor cursor = db.query("menu", null,
					"name like '%" + arg0 + "%'", null, null, null,
					"counter DESC");
			// 如果有查询结果，显示在listview中
			if (cursor.moveToFirst()) {
				list_menus.clear();
				do {
					String item_name = cursor.getString(cursor
							.getColumnIndex("name"));
					int item_pic = cursor.getInt(cursor.getColumnIndex("pic"));
					String item_summary = cursor.getString(cursor
							.getColumnIndex("summary"));
					String item_price = cursor.getString(cursor
							.getColumnIndex("price"));
					Menu menu = null;
					boolean hasAdd = false;
					// 判断是否有已经点的菜，有的话设置已点份数
					if (0 < hasOrderedFromMain.size()) {
						for (int i = 0; i < hasOrderedFromMain.size(); i++) {
							if (hasOrderedFromMain.get(i).getName()
									.equals(item_name)) {
								menu = new Menu(item_name, item_pic,
										item_summary, "¥" + item_price + "/份",
										hasOrderedFromMain.get(i).getAmount());
								hasAdd = true;
								break;
							}
						}
						if (!hasAdd) {
							hasAdd = true;
							menu = new Menu(item_name, item_pic, item_summary,
									"¥" + item_price + "/份");
						}
					} else {
						menu = new Menu(item_name, item_pic, item_summary, "¥"
								+ item_price + "/份");
						hasAdd = true;
					}
					list_menus.add(menu);
				} while (cursor.moveToNext());
				menuTitle.setText("———搜索结果———");
				adapter.notifyDataSetChanged();
			} else {
				// 没有查询到结果
				Toast.makeText(getActivity(), "没有查询到结果", Toast.LENGTH_SHORT)
						.show();
			}
			return false;
		}
	}

	// 从数据库查询特色菜
	public void getHotMenu() {
		dbhelper = new DBHelper(view.getContext(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query("menu", null, null, null, null, null,
				"counter DESC");
		if (cursor.moveToFirst()) {
			int flag = 0;
			do {
				if (10 == flag) {
					break;
				}
				String item_name = cursor.getString(cursor
						.getColumnIndex("name"));
				int item_pic = cursor.getInt(cursor.getColumnIndex("pic"));
				String item_summary = cursor.getString(cursor
						.getColumnIndex("summary"));
				String item_price = cursor.getString(cursor
						.getColumnIndex("price"));
				Menu menu = null;
				boolean hasAdd = false;
				if (0 < hasOrderedFromMain.size()) {
					for (int i = 0; i < hasOrderedFromMain.size(); i++) {
						if (hasOrderedFromMain.get(i).getName()
								.equals(item_name)) {
							menu = new Menu(item_name, item_pic, item_summary,
									"¥" + item_price + "/份", hasOrderedFromMain
											.get(i).getAmount());
							hasAdd = true;
							break;
						}
					}
					if (!hasAdd) {
						hasAdd = true;
						menu = new Menu(item_name, item_pic, item_summary, "¥"
								+ item_price + "/份");
					}
				} else {
					menu = new Menu(item_name, item_pic, item_summary, "¥"
							+ item_price + "/份");
					hasAdd = true;
				}
				flag = flag + 1;
				list_menus.add(menu);
			} while (cursor.moveToNext());
		}
	}

	// 从数据库查询荤菜
	public void getMeatMenu() {
		dbhelper = new DBHelper(view.getContext(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query("menu", null, "type = 'meat'", null, null,
				null, "counter DESC");
		if (cursor.moveToFirst()) {
			do {
				String item_name = cursor.getString(cursor
						.getColumnIndex("name"));
				int item_pic = cursor.getInt(cursor.getColumnIndex("pic"));
				String item_summary = cursor.getString(cursor
						.getColumnIndex("summary"));
				String item_price = cursor.getString(cursor
						.getColumnIndex("price"));
				Menu menu = null;
				boolean hasAdd = false;
				if (0 < hasOrderedFromMain.size()) {
					for (int i = 0; i < hasOrderedFromMain.size(); i++) {
						if (hasOrderedFromMain.get(i).getName()
								.equals(item_name)) {
							menu = new Menu(item_name, item_pic, item_summary,
									"¥" + item_price + "/份", hasOrderedFromMain
											.get(i).getAmount());
							hasAdd = true;
							break;
						}
					}
					if (!hasAdd) {
						hasAdd = true;
						menu = new Menu(item_name, item_pic, item_summary, "¥"
								+ item_price + "/份");
					}
				} else {
					menu = new Menu(item_name, item_pic, item_summary, "¥"
							+ item_price + "/份");
					hasAdd = true;
				}
				list_menus.add(menu);
			} while (cursor.moveToNext());
		}
	}

	// 从数据库查询素菜
	public void getVegeMenu() {
		dbhelper = new DBHelper(view.getContext(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query("menu", null, "type = 'vege'", null, null,
				null, "counter DESC");
		if (cursor.moveToFirst()) {
			do {
				String item_name = cursor.getString(cursor
						.getColumnIndex("name"));
				int item_pic = cursor.getInt(cursor.getColumnIndex("pic"));
				String item_summary = cursor.getString(cursor
						.getColumnIndex("summary"));
				String item_price = cursor.getString(cursor
						.getColumnIndex("price"));
				Menu menu = null;
				boolean hasAdd = false;
				if (0 < hasOrderedFromMain.size()) {
					for (int i = 0; i < hasOrderedFromMain.size(); i++) {
						if (hasOrderedFromMain.get(i).getName()
								.equals(item_name)) {
							menu = new Menu(item_name, item_pic, item_summary,
									"¥" + item_price + "/份", hasOrderedFromMain
											.get(i).getAmount());
							hasAdd = true;
							break;
						}
					}
					if (!hasAdd) {
						hasAdd = true;
						menu = new Menu(item_name, item_pic, item_summary, "¥"
								+ item_price + "/份");
					}
				} else {
					menu = new Menu(item_name, item_pic, item_summary, "¥"
							+ item_price + "/份");
					hasAdd = true;
				}
				list_menus.add(menu);
			} while (cursor.moveToNext());
		}
	}

	// 从数据库查询汤菜
	public void getSoapMenu() {
		dbhelper = new DBHelper(view.getContext(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query("menu", null, "type = 'soap'", null, null,
				null, "counter DESC");
		if (cursor.moveToFirst()) {
			do {
				String item_name = cursor.getString(cursor
						.getColumnIndex("name"));
				int item_pic = cursor.getInt(cursor.getColumnIndex("pic"));
				String item_summary = cursor.getString(cursor
						.getColumnIndex("summary"));
				String item_price = cursor.getString(cursor
						.getColumnIndex("price"));
				Menu menu = null;
				boolean hasAdd = false;
				if (0 < hasOrderedFromMain.size()) {
					for (int i = 0; i < hasOrderedFromMain.size(); i++) {
						if (hasOrderedFromMain.get(i).getName()
								.equals(item_name)) {
							menu = new Menu(item_name, item_pic, item_summary,
									"¥" + item_price + "/份", hasOrderedFromMain
											.get(i).getAmount());
							hasAdd = true;
							break;
						}
					}
					if (!hasAdd) {
						hasAdd = true;
						menu = new Menu(item_name, item_pic, item_summary, "¥"
								+ item_price + "/份");
					}
				} else {
					menu = new Menu(item_name, item_pic, item_summary, "¥"
							+ item_price + "/份");
					hasAdd = true;
				}
				list_menus.add(menu);
			} while (cursor.moveToNext());
		}
	}

	public List<Menu> getHasOrderedToCart() {
		return hasOrderedToCart;
	}

}
