package init;

// 用户第一次启动app时的操作
// 由于没有后台，所有要从数据库中查询的数据需要事先在这里存入数据库中

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.orderplatform.R;

import db.DBHelper;

public class initApp {

	public initApp(Context context) {
		dbhelper = new DBHelper(context, "master.db", null, 1);
	}

	private DBHelper dbhelper;

	private String[] names = { "洋葱牛肉锅贴", "番茄牛肉", "熘松肉", "毛豆炒牛肉", "黑啤牛肉锅",
			"红酒炖牛肉", "罗宋汤", "剁椒蒸紫山药", "麻辣粉蒸肉", "尖椒土豆丝", "手撕包菜", "牙签带鱼",
			"牛油果炒玉米粒", "鱼肉燕麦", "上汤皇帝菜", "酱拌莴笋叶", "蒜蓉茼蒿", "莲藕彩椒炒豆干", "葱香豉油花肾豆",
			"阿胶红豆滋补汤", "清炖鱼圆汤", "清炖羊蝎子汤" };
	private int[] pics = { R.drawable.menu_ycnrgt, R.drawable.menu_fqnr,
			R.drawable.menu_lsr, R.drawable.menu_mdcnr, R.drawable.menu_hpnrg,
			R.drawable.menu_hjdnr, R.drawable.menu_lst, R.drawable.menu_djzzsy,
			R.drawable.menu_mlfzr, R.drawable.menu_jjtds, R.drawable.menu_ssbc,
			R.drawable.menu_yqdy, R.drawable.menu_nygcyml,
			R.drawable.menu_yrym, R.drawable.menu_stwdc, R.drawable.menu_jbwsy,
			R.drawable.menu_srth, R.drawable.menu_locjcdg,
			R.drawable.menu_cxgyhsd, R.drawable.menu_ejhdzbt,
			R.drawable.menu_qdyyt, R.drawable.menu_qdyxzz };
	private double[] prices = { 38.00, 48.00, 58.00, 38.00, 58.00, 58.00,
			38.00, 28.00, 48.0, 18.00, 18.00, 68.00, 18.00, 28.00, 18.00,
			18.00, 18.00, 18.00, 18.00, 28.00, 38.00, 38.00 };
	private String[] summarys = {
			"内馅鲜嫩多汁，外皮焦香酥脆，这样的锅贴谁能不爱呢？",
			"炖牛肉时，加上些番茄，能让牛肉更快变烂发挥自身优势，更重要的是能增强补血功效。",
			"松肉一般的吃法就是炸出来直接吃，香酥软嫩，或者是溜一下吃，也非常棒。",
			"将毛豆焯至7、8成熟后同牛肉、红椒、橄榄菜快炒，牛肉鲜香滑嫩，毛豆君清雅水嫩，美滋滋的咬了一回春。",
			"黑啤单喝可能略带苦味，但是搭配牛肉反而可以使口味回甘，激发出肉的香气，让汤汁更加浓郁。",
			"强身健体抗衰老",
			"酸香开胃",
			"紫山药，含有大量的蛋白质，多糖及淀粉等多种营养物质，有滋肺益肾，健脾等功效，内含大量紫色花青素，有利于治疗心血管疾病，并且起到抗氧化,美容养颜的作用。",
			"美味难忘",
			"精致美味",
			"看似普通的卷心菜实际有着很好的功效。中医认为经常食用卷心菜能性甘平，无毒，有补髓，利关节，壮筋骨，利五脏，调六腑，清热止痛等功效。",
			"带鱼的肉因为没有鱼刺，老人和孩子可以放心的吃鱼肉了。这种做法使带鱼一点都不会浪费掉，也是一种省钱的吃法呢。下面就跟着我一起学学吧。",
			"牛油果果肉为黄绿色，味如牛油，被称为“森林的牛油”，果肉含有多种不饱和脂肪酸，所以有降低胆固醇的功效，另外牛油果所含的维生素对美容保健也很有效果。",
			"燕麦配着鱼肉更好入口，更营养的，清清淡淡。",
			"皇帝菜其实是茼蒿的一种",
			"莴笋叶营养丰富，还具有清热安神、清肝利胆、养胃的功效。莴笋叶子经沸水烫后，不但口感清香，而且营养丰富，值得一试哦！",
			"绿叶菜比起其它颜色的蔬菜营养价值更高。茼蒿菜营养价值也很丰富。分享一下蒜蓉茼蒿的做法。",
			"白色的莲藕，黄色、红色、绿色的三种彩椒，还有豆干，食材色泽搭配的比较丰富，因为加了豆腐干，所以口感味道都不错。",
			"花肾豆，也叫：荷包豆，花豆，因其外形极像动物肾脏，所以名其为肾豆，颜色也同肾脏一般。有健脾，壮阳壮腰，减肥，利尿消肿，祛脂降压等功效。",
			"阿胶是地道的滋补药材，因源于山东东阿县，故名“阿胶”。东阿阿胶自古以来就是皇家贡品。阿胶为补血止血、滋阴润燥之良药。阿胶具有生血作用，可用于失血贫血、缺铁贫血、再生障碍贫血及年老体弱、儿童、妇女的滋补。",
			"生鱼，又名黑名，为淡水名贵鱼类，有“鱼中珍品”之称，是一种营养全面，肉味鲜美的高级食品，它脂肪含量少，蛋白质含量高，含钙量高于一般鱼的含量，还含有多种维生素和无机盐，一直被视为病后康复和老幼体虚者的滋补珍品。",
			"羊蝎子就是羊大梁，因其形跟蝎子相似，故而俗称羊蝎子。羊蝎子低脂肪;低胆固醇;高蛋白;富含钙质。易于吸收，有滋阴补肾，美颜壮阳功效。" };
	private int[] counters = { 56, 43, 66, 50, 34, 45, 6, 26, 45, 61, 47, 55,
			65, 46, 45, 47, 12, 47, 42, 36, 59, 34 };
	private String[] types = { "meat", "meat", "meat", "meat", "meat", "meat",
			"soap", "vege", "meat", "vege", "vege", "meat", "vege", "meat",
			"vege", "vege", "vege", "vege", "vege", "soap", "soap", "soap" };

	private int[] desk_static = new int[24];

	public void init() {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		// 初始化菜单
		for (int i = 0; i < pics.length; i++) {
			values.put("name", names[i]);
			values.put("pic", pics[i]);
			values.put("price", prices[i]);
			values.put("summary", summarys[i]);
			values.put("counter", counters[i]);
			values.put("type", types[i]);
			db.insert("menu", null, values);
			values.clear();
		}
		// 初始化餐桌
		for (int i = 0; i < 24; i++) {
			desk_static[i] = 1;
		}
		// 初始化几个不可选的餐桌
		desk_static[6] = 0;
		desk_static[23] = 0;
		desk_static[11] = 0;
		desk_static[9] = 0;
		desk_static[20] = 0;
		for (int i = 0; i < 24; i++) {
			values.put("static", desk_static[i]);
			db.insert("desk", null, values);
			values.clear();
		}
	}

}
