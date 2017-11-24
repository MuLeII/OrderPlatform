package db;

// 创建数据库，建表等对数据库的操作

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

	// 创建用户表的ＳＱＬ语句
	public static final String CREATE_TABLE_USER = "create table user ("
			+ "id integer primary key autoincrement,"
			+ "username text,"
			+ "password text,"
			+ "phone integer,"
			+ "question1 integer,"
			+ "answer1 text,"
			+ "question2 integer,"
			+ "answer2 text,"
			+ "question3 integer,"
			+ "answer3 text)";

	// 创建菜单表的ＳＱＬ语句
	public static final String CREATE_TABLE_MENU = "create table menu ("
			+ "id integer primary key autoincrement,"
			+ "name text,"
			+ "pic integer,"
			+ "price real,"
			+ "summary text,"
			+ "counter integer,"
			+ "type text)";

	// 创建订单表的ＳＱＬ语句
	public static final String CREATE_TABLE_DEAL = "create table deal(" +
			"id integer primary key autoincrement," +
			"orderuser text," +
			"makedate text," +
			"reservedate text," +
			"menus text," +
			"tablenumber integer," +
			"peoplenumber integer," +
			"orderprice real," +
			"static text)";
	
	// 创建餐桌表的ＳＱＬ语句
	public static final String CREATE_TABLE_DESK = "create table desk(" +
			"id integer primary key autoincrement," +
			"static integer)"; //1-可选, 0-不可选

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// 建表
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_MENU);
		db.execSQL(CREATE_TABLE_DEAL);
		db.execSQL(CREATE_TABLE_DESK);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
