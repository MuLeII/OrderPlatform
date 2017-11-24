package adapter;

// // 对Order的adapter，在显示订单的ListView处使用

import java.util.List;

import com.example.orderplatform.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderAdapter extends ArrayAdapter<Order> {

	private int resourceId;

	public OrderAdapter(Context context, int resource, List<Order> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Order order = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView orderId = (TextView) view.findViewById(R.id.order_id);
		TextView makeDate = (TextView) view.findViewById(R.id.order_makedate);
		TextView reserveDate = (TextView) view.findViewById(R.id.order_reservedate);
		TextView menuName = (TextView) view.findViewById(R.id.order_menus_name);
		TextView menuAmount = (TextView) view.findViewById(R.id.order_menus_amount);
		TextView deskId = (TextView) view.findViewById(R.id.order_deskid);
		TextView peopleNumber = (TextView) view.findViewById(R.id.order_peoplenumber);
		TextView orderPrice = (TextView) view.findViewById(R.id.order_money);
		TextView orderStatic = (TextView) view.findViewById(R.id.order_static);

		orderId.setText(order.getId());
		makeDate.setText(order.getMakeDate());
		reserveDate.setText(order.getReserveDate());
		menuName.setText(order.getNameOfMenus());
		menuAmount.setText(order.getAmountOfMenus());
		deskId.setText(order.getNumberOfDesk());
		peopleNumber.setText(order.getNumberOfPeople());
		orderPrice.setText(order.getOrderPrice());
		orderStatic.setText(order.getOrderStatic());
		
		return view;
	}

}
