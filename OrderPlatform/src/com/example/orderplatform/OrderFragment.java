package com.example.orderplatform;

// 显示订单信息的Frament

import java.util.ArrayList;
import java.util.List;

import db.DBHelper;
import adapter.Menu;
import adapter.MenuAdapter;
import adapter.Order;
import adapter.OrderAdapter;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class OrderFragment extends Fragment {

	private List<Order> list_orders = new ArrayList<Order>();
	private OrderAdapter adapter;
	private DBHelper dbhelper;
	private String CurUser = null;

	private View view;
	private ListView lv_order;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.order, container, false);

		lv_order = (ListView) view.findViewById(R.id.order_lv);
		list_orders.clear();
		// 从数据库中获取当前用户的订单
		getOrder();
		Log.d("lalala--ordersize", String.valueOf(list_orders.size()));
		adapter = new OrderAdapter(view.getContext(), R.layout.item_order,
				list_orders);
		lv_order.setAdapter(adapter);

		return view;
	}

	// 从数据库中获取当前用户的订单
	public void getOrder() {
		// 从MainActivity获取当前用户的用户名
		MainActivity activity = (MainActivity) getActivity();
		CurUser = activity.CurrentUserName;
		dbhelper = new DBHelper(view.getContext(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query("deal", null, "orderuser = '" + CurUser + "'",
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String makeDate = cursor.getString(cursor
						.getColumnIndex("makedate"));
				String reserveDate = cursor.getString(cursor
						.getColumnIndex("reservedate"));
				String menus = cursor.getString(cursor.getColumnIndex("menus"));
				int numberOfDesk = cursor.getInt(cursor
						.getColumnIndex("tablenumber"));
				int numberOfPeople = cursor.getInt(cursor
						.getColumnIndex("peoplenumber"));
				String orderStatic = cursor.getString(cursor
						.getColumnIndex("static"));
				double oederPrice = cursor.getDouble(cursor
						.getColumnIndex("orderprice"));
				Log.d("lalala--order", menus);
				Order order = new Order(id, makeDate, reserveDate, menus,
						numberOfDesk, numberOfPeople, orderStatic, oederPrice);
				list_orders.add(order);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

}
