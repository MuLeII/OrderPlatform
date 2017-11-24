package adapter;

// ��Menu��adapter������ʾ�˵���ListView��ʹ��

import java.util.ArrayList;
import java.util.List;

import com.example.orderplatform.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<Menu> {

	// �����Ѿ����˵Ĳ�
	public List<Menu> hasOrdered = new ArrayList<Menu>();
	private int index = -1;
	private int resourceId;

	public MenuAdapter(Context context, int resource, List<Menu> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Menu menu = getItem(position);
		final View view = LayoutInflater.from(getContext()).inflate(resourceId,
				null);
		final TextView menuName = (TextView) view
				.findViewById(R.id.menu_item_name);
		final ImageView menuPic = (ImageView) view
				.findViewById(R.id.menu_item_pic);
		final TextView menuSummary = (TextView) view
				.findViewById(R.id.menu_item_summary);
		final TextView menuPrice = (TextView) view
				.findViewById(R.id.menu_item_price);
		final TextView menu_plus = (TextView) view
				.findViewById(R.id.menu_item_plus);
		final TextView menu_minus = (TextView) view
				.findViewById(R.id.menu_item_minus);
		final TextView menu_amount = (TextView) view
				.findViewById(R.id.menu_item_amount);
		menuName.setText(menu.getName());
		menuPic.setBackgroundResource(menu.getPic());
		menuSummary.setText(menu.getSummary());
		menuPrice.setText(menu.getPrice());
		menu_amount.setText(menu.getAmount());
		// menu_amount.setVisibility(View.GONE);
		// menu_minus.setVisibility(View.GONE);

		// �Ӳ�
		menu_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// ����ѵ�˵���û�е�ǰ����Ĳ�
				if (0 == Integer.valueOf(menu_amount.getText().toString())) {
					Menu add_dish = new Menu(menu.getName(), menu.getPic(),
							menu.getSummary(), menu.getPrice(), menu
									.getAmount());
					// �ѵ�ǰ����Ĳ���ӵ��ѵ�˵���
					hasOrdered.add(add_dish);
					// menu_amount.setVisibility(View.VISIBLE);
					// menu_minus.setVisibility(View.VISIBLE);
				}
				// �����ѵ�˵��е�ǰ����Ĳ˵�����
				int number = Integer.valueOf(menu_amount.getText().toString()) + 1;
				// ��ȡ��ǰ����Ĳ����ѵ�˵��е��±�
				index = getDishIndex(menu.getName());
				hasOrdered.get(index).setAmount(String.valueOf(number));
				menu_amount.setText(String.valueOf(number));
				menu.setAmount(String.valueOf(number));
			}
		});

		// ����
		menu_minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (1 >= Integer.valueOf(menu_amount.getText().toString())) {
					if (1 == Integer.valueOf(menu_amount.getText().toString())) {
						// view.setVisibility(View.GONE);
						index = getDishIndex(menu.getName());
						// �ѵ�ǰ����Ĳ˴��ѵ�˵����Ƴ�
						hasOrdered.remove(index);
						menu_amount.setText("0");
						menu.setAmount("0");
						// menu_amount.setVisibility(View.GONE);
						// menu_minus.setVisibility(View.GONE);
					}
				} else {
					int number = Integer.valueOf(menu_amount.getText()
							.toString()) - 1;
					index = getDishIndex(menu.getName());
					hasOrdered.get(index).setAmount(String.valueOf(number));
					menu_amount.setText(String.valueOf(number));
					menu.setAmount(String.valueOf(number));
					Log.d("lalala", hasOrdered.get(index).getAmount());
				}
			}
		});
		return view;
	}

	// �õ�����Ϊname�Ĳ����ѵ�˵��е��±�
	private int getDishIndex(String name) {
		int rtn = -1;
		for (int i = 0; i < hasOrdered.size(); i++) {
			if (name.equals(hasOrdered.get(i).getName())) {
				rtn = i;
				break;
			}
		}
		return rtn;
	}

	public List<Menu> getHasOrdered() {
		return hasOrdered;
	}
}
