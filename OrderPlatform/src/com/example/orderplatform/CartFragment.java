package com.example.orderplatform;

// ��ʾ�ѵ�˵�Fragment

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
	private int selected_desk = -1; // ��¼�û�ѡ������
	private int people_number = -1; // ��¼�ò�����
	private int year = 0;
	private int month = 0;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private Calendar MyCalender;
	private String CurUser = null; // ��¼��ǰ�û�
	private String makeDate = null; // ��¼�µ�ʱ��
	private String ReserveDate = null; // ��¼Ԥ���ò�ʱ��
	private double orderPrice = 0.0; // ��¼�����ܼ�

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
		// ����1��Id
		ID_DESK1 = R.id.desk1;
		int id = ID_DESK1;
		// �õ����еĲ�����Ӧ��RadioButton�������ü���
		desk = new RadioButton[24];
		for (int i = 0; i < 24; i++) {
			desk[i] = (RadioButton) view.findViewById(id);
			desk[i].setOnClickListener(new deskOnClickListener());
			id = id + 1;
		}
		// �����ݿ��ѯ�����ɲ���ѡstatic = 0������ѡ
		getDeskStatic();
		Drawable drawableTop = getResources().getDrawable(R.drawable.desk_lock);
		for (int i = 0; i < 24; i++) {
			if (0 == desk_static[i]) {
				// ����ѡ�Ĳ������ò��ɵ�� ����ͼƬ
				desk[i].setEnabled(false);
				desk[i].setCompoundDrawablesWithIntrinsicBounds(null,
						drawableTop, null, null);
			}
		}

		setReserveDate.setOnClickListener(new btnOnClickListener());
		btn_commit.setOnClickListener(new btnOnClickListener());

		// ��MainActivity��ȡ��¼�ѵ�˵�list
		MainActivity activity = (MainActivity) getActivity();
		list_menus = activity.getHasOrdered();
		Log.d("lalala--main->cart--hasOrder", String.valueOf(list_menus.size()));
		adapter = new MenuAdapter(view.getContext(), R.layout.item_menu,
				list_menus);

		// adapter = new MenuHasOrderAdapter(view.getContext(),
		// R.layout.item_menu, list_menus);
		adapter.hasOrdered = list_menus;
		lv_hasOrdered.setAdapter(adapter);

		// ����ѵ��
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
		
		// ѡ����ť�ļ���
		selectDesk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (0 < list_menus.size()) {
					// ��ʾѡ�������
					title.setText("������������������ѡ��������������������");
					clear.setVisibility(View.GONE);
					lv_hasOrdered.setVisibility(View.GONE);
					selectDesk.setVisibility(View.GONE);
					legend.setVisibility(View.VISIBLE);
					desks.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(getActivity(), "�𼱣��㻹û���",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		return view;
	}

	// ������Ӧ��RadioButton�ļ���
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
				// �����ò�ʱ�䰴ť�ļ���
				Intent intent = new Intent();
				intent.setClass(getActivity(), DateActivity.class);
				// ���ò�ʱ�䴫��MainActivity
				getActivity().startActivityForResult(intent, 1);
				break;
			case R.id.desk_commit:
				// �ύ������ť�ļ���
				MainActivity activity = (MainActivity) getActivity();
				if (et_people_number.getText().toString().equals("")) {
					Toast.makeText(getActivity(), "�㻹ûд�ò�����",
							Toast.LENGTH_SHORT).show();
				} else {
					if (null == activity.ReserveDate) {
						Toast.makeText(getActivity(), "�㻹û���ò�ʱ��",
								Toast.LENGTH_SHORT).show();
					} else {
						if (0 > selected_desk) {
							Toast.makeText(getActivity(), "�㻹ûѡ��",
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
	
	// �����ݿ��ȡ����״̬
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

	//�ύ�����Ĳ������������������ݿ���
	public void commitOrder() {
		makeDate = "";
		orderPrice = 0.0;
		// ��ȡ��ǰϵͳʱ�䣬�µ�ʱ��
		long time = System.currentTimeMillis();
		MyCalender = Calendar.getInstance();
		MyCalender.setTimeInMillis(time);
		year = MyCalender.get(Calendar.YEAR);
		month = MyCalender.get(Calendar.MONTH);
		day = MyCalender.get(Calendar.DAY_OF_MONTH);
		hour = MyCalender.get(Calendar.HOUR_OF_DAY);// 24Сʱ��
		minute = MyCalender.get(Calendar.MINUTE);
		makeDate = String.valueOf(year) + "��" + String.valueOf(month) + "��"
				+ String.valueOf(day) + "��" + String.valueOf(hour) + "ʱ"
				+ String.valueOf(minute) + "��";
		String price = "";
		String menus = "";
		// ���㶩���ܼ�
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
		String orderStatic = "�������ύ";
		// ��ȡ��ǰ�û��û�����Ԥ���ò�ʱ��
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
		Toast.makeText(getActivity(), "�������ύ", Toast.LENGTH_SHORT).show();
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
