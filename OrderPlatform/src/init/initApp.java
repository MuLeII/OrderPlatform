package init;

// �û���һ������appʱ�Ĳ���
// ����û�к�̨������Ҫ�����ݿ��в�ѯ��������Ҫ����������������ݿ���

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

	private String[] names = { "���ţ�����", "����ţ��", "������", "ë����ţ��", "��ơţ���",
			"�����ţ��", "������", "�罷����ɽҩ", "����������", "�⽷����˿", "��˺����", "��ǩ����",
			"ţ�͹���������", "��������", "�����ʵ۲�", "����ݫ��Ҷ", "��������", "��ź�ʽ�������", "�������ͻ�����",
			"�����춹�̲���", "������Բ��", "������Ы����" };
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
			"�������۶�֭����Ƥ�����ִ࣬�����Ĺ���˭�ܲ����أ�",
			"��ţ��ʱ������Щ���ѣ�����ţ�������÷����������ƣ�����Ҫ��������ǿ��Ѫ��Ч��",
			"����һ��ĳԷ�����ը����ֱ�ӳԣ��������ۣ���������һ�³ԣ�Ҳ�ǳ�����",
			"��ë������7��8�����ͬţ�⡢�콷����魲˿쳴��ţ�����㻬�ۣ�ë��������ˮ�ۣ������̵�ҧ��һ�ش���",
			"��ơ���ȿ����Դ���ζ�����Ǵ���ţ�ⷴ������ʹ��ζ�ظʣ��������������������֭����Ũ����",
			"ǿ���忹˥��",
			"���㿪θ",
			"��ɽҩ�����д����ĵ����ʣ����Ǽ���۵ȶ���Ӫ�����ʣ����̷���������Ƣ�ȹ�Ч���ں�������ɫ�����أ�������������Ѫ�ܼ����������𵽿�����,�������յ����á�",
			"��ζ����",
			"������ζ",
			"������ͨ�ľ��Ĳ�ʵ�����źܺõĹ�Ч����ҽ��Ϊ����ʳ�þ��Ĳ����Ը�ƽ���޶����в��裬���ؽڣ�׳��ǣ������࣬������������ֹʹ�ȹ�Ч��",
			"���������Ϊû����̣����˺ͺ��ӿ��Է��ĵĳ������ˡ���������ʹ����һ�㶼�����˷ѵ���Ҳ��һ��ʡǮ�ĳԷ��ء�����͸�����һ��ѧѧ�ɡ�",
			"ţ�͹�����Ϊ����ɫ��ζ��ţ�ͣ�����Ϊ��ɭ�ֵ�ţ�͡������⺬�ж��ֲ�����֬���ᣬ�����н��͵��̴��Ĺ�Ч������ţ�͹�������ά���ض����ݱ���Ҳ����Ч����",
			"�����������������ڣ���Ӫ���ģ����嵭����",
			"�ʵ۲���ʵ�������һ��",
			"ݫ��ҶӪ���ḻ�����������Ȱ��������������θ�Ĺ�Ч��ݫ��Ҷ�Ӿ���ˮ�̺󣬲����ڸ����㣬����Ӫ���ḻ��ֵ��һ��Ŷ��",
			"��Ҷ�˱���������ɫ���߲�Ӫ����ֵ���ߡ������Ӫ����ֵҲ�ܷḻ������һ�����������������",
			"��ɫ����ź����ɫ����ɫ����ɫ�����ֲʽ������ж��ɣ�ʳ��ɫ�����ıȽϷḻ����Ϊ���˶����ɣ����Կڸ�ζ��������",
			"��������Ҳ�У��ɰ������������������μ��������࣬��������Ϊ��������ɫҲͬ����һ�㡣�н�Ƣ��׳��׳�������ʣ��������ף���֬��ѹ�ȹ�Ч��",
			"�����ǵص����̲�ҩ�ģ���Դ��ɽ�������أ������������������������Թ��������ǻʼҹ�Ʒ������Ϊ��ѪֹѪ����������֮��ҩ������������Ѫ���ã�������ʧѪƶѪ��ȱ��ƶѪ�������ϰ�ƶѪ��������������ͯ����Ů���̲���",
			"���㣬����������Ϊ��ˮ�������࣬�С�������Ʒ��֮�ƣ���һ��Ӫ��ȫ�棬��ζ�����ĸ߼�ʳƷ����֬�������٣������ʺ����ߣ�����������һ����ĺ����������ж���ά���غ��޻��Σ�һֱ����Ϊ���󿵸������������ߵ��̲���Ʒ��",
			"��Ы�Ӿ���������������θ�Ы�����ƣ��ʶ��׳���Ы�ӡ���Ы�ӵ�֬��;�͵��̴�;�ߵ���;�������ʡ��������գ�����������������׳����Ч��" };
	private int[] counters = { 56, 43, 66, 50, 34, 45, 6, 26, 45, 61, 47, 55,
			65, 46, 45, 47, 12, 47, 42, 36, 59, 34 };
	private String[] types = { "meat", "meat", "meat", "meat", "meat", "meat",
			"soap", "vege", "meat", "vege", "vege", "meat", "vege", "meat",
			"vege", "vege", "vege", "vege", "vege", "soap", "soap", "soap" };

	private int[] desk_static = new int[24];

	public void init() {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		// ��ʼ���˵�
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
		// ��ʼ������
		for (int i = 0; i < 24; i++) {
			desk_static[i] = 1;
		}
		// ��ʼ����������ѡ�Ĳ���
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
