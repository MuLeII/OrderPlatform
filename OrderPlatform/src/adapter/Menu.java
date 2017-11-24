package adapter;

// Menu类，各种菜

public class Menu {

	private String name;
	private int pic;
	private String summary;
	private String price;
	private String amount;

	// 默认已点份数为0的构造方法
	public Menu(String name, int pic, String summary, String price) {
		this.name = name;
		this.pic = pic;
		this.summary = summary;
		this.price = price;
		this.amount = "0";
	}
	
	// 知道已点份数的构造方法
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

	// 设置已点份数
	public void setAmount(String amount) {
		this.amount = amount;
	}

}
