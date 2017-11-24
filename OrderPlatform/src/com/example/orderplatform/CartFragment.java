package com.example.orderplatform;

// 显示已点菜的Fragment

import java.util.ArrayList;
import java.util.List;

import adapter.Menu;
import adapter.MenuAdapter;
import adapter.MenuHasOrderAdapter;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import java.util.Calendar;

import db.DBHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class CartFragment extends Fragment {

	private List<Menu> list_menus = new ArrayList<Menu>();
	private MenuAdapter adapter;
	private DBHelper dbhelper;
	private int ID_DESK1;
	private int[] desk_static = new int[24];
	private int selected_desk = -1; // 记录用户选的桌号
	private int people_number = -1; // 记录用餐人数
	private int year = 0;
	private int month = 0;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private Calendar MyCalender;
	private String CurUser = null; // 记录当前用户
	private String makeDate = null; // 记录下单时间
	private String ReserveDate = null; // 记录预定用餐时间
	private double orderPrice = 0.0; // 记录订单总价

	private View view;
	private ListView lv_hasOrdered;
	private TextView title;
	private TextView clear;
	private Button selectDesk;
	private LinearLayout legend;
	private LinearLayout desks;
	private RadioButton[] desk;
	private EditText et_people_number;
	private Button setReserveDate;
	private Button btn_commit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.cart, container, false);

		lv_hasOrdered = (ListView) view.findViewById(R.id.cart_lv);
		title = (TextView) view.findViewById(R.id.cart_tv);
		clear = (TextView) view.findViewById(R.id.cart_tv_clear);
		selectDesk = (Button) view.findViewById(R.id.cart_button_desk);
		legend = (LinearLayout) view.findViewById(R.id.cort_desks_legend);
		desks = (LinearLayout) view.findViewById(R.id.cort_desks);
		et_people_number = (EditText) view
				.findViewById(R.id.desk_number_people);
		setReserveDate = (Button) view.findViewById(R.id.desk_reserve_date);
		btn_commit = (Button) view.findViewById(R.id.desk_commit);
		// 餐桌1的Id
		ID_DESK1 = R.id.desk1;
		int id = ID_DESK1;
		// 得到所有的餐桌对应的RadioButton对象并设置监听
		desk = new RadioButton[24];
		for (int i = 0; i < 24; i++) {
			desk[i] = (RadioButton) view.findViewById(id);
			desk[i].setOnClickListener(new deskOnClickListener());
			id = id + 1;
		}
		// 从数据库查询餐桌可不可选static = 0，不可选
		getDeskStatic();
		Drawable drawableTop = getResources().getDrawable(R.drawable.desk_lock);
		for (int i = 0; i < 24; i++) {
			if (0 == desk_static[i]) {
				// 不可选的餐桌设置不可点击 更换图片
				desk[i].setEnabled(false);
				desk[i].setCompoundDrawablesWithIntrinsicBounds(null,
						drawableTop, null, null);
			}
		}

		setReserveDate.setOnClickListener(new btnOnClickListener());
		btn_commit.setOnClickListener(new btnOnClickListener());

		// 从MainActivity获取记录已点菜的list
		MainActivity activity = (MainActivity) getActivity();
		list_menus = activity.getHasOrdered();
		Log.d("lalala--main->cart--hasOrder", String.valueOf(list_menus.size()));
		adapter = new MenuAdapter(view.getContext(), R.layout.item_menu,
				list_menus);

		// adapter = new MenuHasOrderAdapter(view.getContext(),
		// R.layout.item_menu, list_menus);
		adapter.hasOrdered = list_menus;
		lv_hasOrdered.setAdapter(adapter);

		// 清空已点菜
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != list_menus) {
					list_menus.clear();
					adapter.notifyDataSetChanged();
				}
			}

		});
		
		// 选桌按钮的监听
		selectDesk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (0 < list_menus.size()) {
					// 显示选桌的组件
					title.setText("—————————选桌—————————");
					clear.setVisibility(View.GONE);
					lv_hasOrdered.setVisibility(View.GONE);
					selectDesk.setVisibility(View.GONE);
					legend.setVisibility(View.VISIBLE);
					desks.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(getActivity(), "别急，你还没点菜",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		return view;
	}

	// 餐桌对应的RadioButton的监听
	public class deskOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (0 < selected_desk) {
				desk[selected_desk - 1].setChecked(false);
			}
			selected_desk = arg0.getId() - ID_DESK1 + 1;
			Toast.makeText(getActivity(),
					"desk" + String.valueOf(selected_desk), Toast.LENGTH_SHORT)
					.show();
			Log.d("lalala--selected_desk", String.valueOf(selected_desk));
		}

	}

	public class btnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.desk_reserve_date:
				// 设置用餐时间按钮的监听
				Intent intent = new Intent();
				intent.setClass(getActivity(), DateActivity.class);
				// 将用餐时间传回MainActivity
				getActivity().startActivityForResult(intent, 1);
				break;
			case R.id.desk_commit:
				// 提交订单按钮的监听
				MainActivity activity = (MainActivity) getActivity();
				if (et_people_number.getText().toString().equals("")) {
					Toast.makeText(getActivity(), "你还没写用餐人数",
							Toast.LENGTH_SHORT).show();
				} else {
					if (null == activity.ReserveDate) {
						Toast.makeText(getActivity(), "你还没设用餐时间",
								Toast.LENGTH_SHORT).show();
					} else {
						if (0 > selected_desk) {
							Toast.makeText(getActivity(), "你还没选桌",
									Toast.LENGTH_SHORT).show();
						} else {
							Log.d("lalala--peoplenumber", et_people_number
									.getText().toString());
							people_number = Integer.valueOf(et_people_number
									.getText().toString());
							commitOrder();
						}
					}
				}
				break;
			default:
				break;
			}
		}

	}
	
	// 从数据库获取餐桌状态
	public void getDeskStatic() {
		dbhelper = new DBHelper(getActivity(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		// try {
		Cursor cursor = db.query("desk", new String[] { "static" }, null, null,
				null, null, null);
		cursor.moveToFirst();
		for (int i = 0; i < 24; i++) {
			if (cursor != null)
				desk_static[i] = cursor.getInt(0);
			cursor.moveToNext();
		}
		cursor.close();
	}

	//提交订单的操作，将订单传至数据库中
	public void commitOrder() {
		makeDate = "";
		orderPrice = 0.0;
		// 获取当前系统时间，下单时间
		long time = System.currentTimeMillis();
		MyCalender = Calendar.getInstance();
		MyCalender.setTimeInMillis(time);
		year = MyCalender.get(Calendar.YEAR);
		month = MyCalender.get(Calendar.MONTH);
		day = MyCalender.get(Calendar.DAY_OF_MONTH);
		hour = MyCalender.get(Calendar.HOUR_OF_DAY);// 24小时制
		minute = MyCalender.get(Calendar.MINUTE);
		makeDate = String.valueOf(year) + "年" + String.valueOf(month) + "月"
				+ String.valueOf(day) + "日" + String.valueOf(hour) + "时"
				+ String.valueOf(minute) + "分";
		String price = "";
		String menus = "";
		// 计算订单总价
		for (int i = 0; i < list_menus.size() - 1; i++) {
			price = list_menus.get(i).getPrice();
			price = price.substring(1, price.length() - 2);
			orderPrice = orderPrice + Double.valueOf(price)
					* Integer.valueOf(list_menus.get(i).getAmount());
			menus = menus + list_menus.get(i).getName() + " "
					+ list_menus.get(i).getAmount() + " ";
		}
		price = list_menus.get(list_menus.size() - 1).getPrice();
		price = price.substring(1, price.length() - 2);
		orderPrice = orderPrice
				+ Double.valueOf(price)
				* Integer.valueOf(list_menus.get(list_menus.size() - 1)
						.getAmount());
		menus = menus + list_menus.get(list_menus.size() - 1).getName() + " "
				+ list_menus.get(list_menus.size() - 1).getAmount();
		String orderStatic = "订单已提交";
		// 获取当前用户用户名、预定用餐时间
		MainActivity activity = (MainActivity) getActivity();
		CurUser = activity.CurrentUserName;
		ReserveDate = activity.ReserveDate;

		dbhelper = new DBHelper(getActivity(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("orderuser", CurUser);
		values.put("makedate", makeDate);
		values.put("reservedate", ReserveDate);
		values.put("menus", menus);
		values.put("tablenumber", selected_desk);
		values.put("peoplenumber", people_number);
		values.put("static", orderStatic);
		values.put("orderprice", orderPrice);
		db.insert("deal", null, values);
		values.clear();
		Toast.makeText(getActivity(), "订单已提交", Toast.LENGTH_SHORT).show();
		list_menus.clear();
		MainActivity main = (MainActivity) getActivity();
		RadioButton tab_order = (RadioButton) main
				.findViewById(R.id.main_menu_order);
		tab_order.setChecked(true);
	}

	public List<Menu> getList_menus() {
		return list_menus;
	}

}
