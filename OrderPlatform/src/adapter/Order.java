package adapter;

// Order�࣬�û�����

import android.util.Log;

public class Order {

	private String id;
	private String makeDate;
	private String reserveDate;
	private String nameOfMenus;
	private String amountOfMenus;
	private String numberOfDesk;
	private String numberOfPeople;
	private String orderStatic;
	private String orderPrice;

	public Order(int id, String makeDate, String reserveDate, String menus,
			int numberOfDesk, int numberOfPeople, String orderStatic,
			double orderPrice) {
		this.id = "�����ţ�" + String.valueOf(id);
		this.makeDate = "��������ʱ�䣺" + makeDate;
		this.reserveDate = "Ԥ���ò�ʱ�䣺" + reserveDate;
		String[] menuItem = menus.split(" ");
		String name = "";
		String amount = "";
		if (2 < menuItem.length) {
			for (int i = 0; i < (menuItem.length - 2); i++) {
				if (0 == (i % 2)) {
					name = name + menuItem[i] + "\n";

				} else {
					amount = amount + menuItem[i] + "��" + "\n";
				}
			}
			name = name + menuItem[menuItem.length - 2];
			amount = amount + menuItem[menuItem.length - 1] + "��";
		} else {
			name = menuItem[0];
			amount = menuItem[1] + "��";
			Log.d("lalala--name", name);
			Log.d("lalala--amount", amount);
		}
		this.nameOfMenus = name;
		this.amountOfMenus = amount;
		this.numberOfDesk = "Ԥ�����ţ�" + String.valueOf(numberOfDesk);
		this.numberOfPeople = "Ԥ���ò�������" + String.valueOf(numberOfPeople);
		this.orderStatic = orderStatic;
		this.orderPrice = "�����ܼۣ�" + String.valueOf(orderPrice);
	}

	public String getId() {
		return id;
	}

	public String getMakeDate() {
		return makeDate;
	}

	public String getReserveDate() {
		return reserveDate;
	}

	public String getNameOfMenus() {
		return nameOfMenus;
	}

	public String getAmountOfMenus() {
		return amountOfMenus;
	}

	public String getNumberOfDesk() {
		return numberOfDesk;
	}

	public String getNumberOfPeople() {
		return numberOfPeople;
	}

	public String getOrderStatic() {
		return orderStatic;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

}
