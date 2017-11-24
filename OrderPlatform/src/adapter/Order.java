package adapter;

// Order类，用户订单

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
		this.id = "订单号：" + String.valueOf(id);
		this.makeDate = "订单生成时间：" + makeDate;
		this.reserveDate = "预定用餐时间：" + reserveDate;
		String[] menuItem = menus.split(" ");
		String name = "";
		String amount = "";
		if (2 < menuItem.length) {
			for (int i = 0; i < (menuItem.length - 2); i++) {
				if (0 == (i % 2)) {
					name = name + menuItem[i] + "\n";

				} else {
					amount = amount + menuItem[i] + "份" + "\n";
				}
			}
			name = name + menuItem[menuItem.length - 2];
			amount = amount + menuItem[menuItem.length - 1] + "份";
		} else {
			name = menuItem[0];
			amount = menuItem[1] + "份";
			Log.d("lalala--name", name);
			Log.d("lalala--amount", amount);
		}
		this.nameOfMenus = name;
		this.amountOfMenus = amount;
		this.numberOfDesk = "预定桌号：" + String.valueOf(numberOfDesk);
		this.numberOfPeople = "预定用餐人数：" + String.valueOf(numberOfPeople);
		this.orderStatic = orderStatic;
		this.orderPrice = "订单总价：" + String.valueOf(orderPrice);
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
