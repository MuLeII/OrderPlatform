package adapter;

// Menu�࣬���ֲ�

public class Menu {

	private String name;
	private int pic;
	private String summary;
	private String price;
	private String amount;

	// Ĭ���ѵ����Ϊ0�Ĺ��췽��
	public Menu(String name, int pic, String summary, String price) {
		this.name = name;
		this.pic = pic;
		this.summary = summary;
		this.price = price;
		this.amount = "0";
	}
	
	// ֪���ѵ�����Ĺ��췽��
	public Menu(String name, int pic, String summary, String price, String amount) {
		this.name = name;
		this.pic = pic;
		this.summary = summary;
		this.price = price;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public int getPic() {
		return pic;
	}

	public String getSummary() {
		return summary;
	}

	public String getPrice() {
		return price;
	}

	public String getAmount() {
		return amount;
	}

	// �����ѵ����
	public void setAmount(String amount) {
		this.amount = amount;
	}

}
