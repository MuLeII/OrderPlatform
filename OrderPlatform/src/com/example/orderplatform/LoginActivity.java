package com.example.orderplatform;

// 登录和注册界面

import specification.DataValid;
import init.initApp;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import db.DBHelper;

public class LoginActivity extends Activity {

	private ImageView account;
	private EditText username;
	private EditText password;
	private EditText phone;
	private LinearLayout layout_question1;
	private Spinner question1;
	private LinearLayout layout_answer1;
	private EditText answer1;
	private LinearLayout layout_question2;
	private Spinner question2;
	private LinearLayout layout_answer2;
	private EditText answer2;
	private LinearLayout layout_question3;
	private Spinner question3;
	private LinearLayout layout_answer3;
	private EditText answer3;
	private LinearLayout layout_remember;
	private CheckBox remember_password;
	private TextView remember;
	private TextView forget;
	private Button register;
	private Button login;
	private TextView goto_register;
	private TextView goto_login;

	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private DBHelper dbhelper;
	private boolean isRememberPassword = false;
	private Boolean user_first = false;
	private String UserName = null;
	private String SelectedUserName = null;
	private String Password = null;
	private String SelectedPassword = null;
	private String PhoneNumber = null;
	private int q1 = -1;
	private String a1 = null;
	private int q2 = -1;
	private String a2 = null;
	private int q3 = -1;
	private String a3 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		pref = PreferenceManager.getDefaultSharedPreferences(this);
		dbhelper = new DBHelper(LoginActivity.this, "master.db", null, 1);

		account = (ImageView) findViewById(R.id.login_imageview_account);
		username = (EditText) findViewById(R.id.login_edittext_username);
		password = (EditText) findViewById(R.id.login_edittext_password);
		phone = (EditText) findViewById(R.id.login_edittext_phone);
		layout_question1 = (LinearLayout) findViewById(R.id.login_question1);
		question1 = (Spinner) findViewById(R.id.login_spinner_question1);
		layout_answer1 = (LinearLayout) findViewById(R.id.login_answer1);
		answer1 = (EditText) findViewById(R.id.login_edittext_answer1);
		layout_question2 = (LinearLayout) findViewById(R.id.login_question2);
		question2 = (Spinner) findViewById(R.id.login_spinner_question2);
		layout_answer2 = (LinearLayout) findViewById(R.id.login_answer2);
		answer2 = (EditText) findViewById(R.id.login_edittext_answer2);
		layout_question3 = (LinearLayout) findViewById(R.id.login_question3);
		question3 = (Spinner) findViewById(R.id.login_spinner_question3);
		layout_answer3 = (LinearLayout) findViewById(R.id.login_answer3);
		answer3 = (EditText) findViewById(R.id.login_edittext_answer3);
		layout_remember = (LinearLayout) findViewById(R.id.login_remember);
		remember_password = (CheckBox) findViewById(R.id.login_checkbox_remember);
		remember = (TextView) findViewById(R.id.login_textview_remember);
		forget = (TextView) findViewById(R.id.login_textview_forget);
		login = (Button) findViewById(R.id.login_button_login);
		register = (Button) findViewById(R.id.login_button_register);
		goto_register = (TextView) findViewById(R.id.login_textview_register);
		goto_login = (TextView) findViewById(R.id.login_textview_login);

		// 得到Perferences中的数据
		// 是否记住了密码
		isRememberPassword = pref.getBoolean("remember_password", false);
		// 是否是第一次启动App
		user_first = pref.getBoolean("first", true);
		if (isRememberPassword) {
			// 如果记住了密码，自动填充账号密码
			username.setText(pref.getString("username", ""));
			password.setText(pref.getString("password", ""));
			remember_password.setChecked(true);
		}
		if (user_first) {
			// 如果是第一次启动，进行数据初始化
			initApp init = new initApp(getApplicationContext());
			init.init();
			editor = pref.edit();
			editor.putBoolean("first", false);
			editor.commit();
		}

		goto_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 负责登录的组件GONE，负责注册的组件VISIBLE
				gotoRegister();
			}

		});
		goto_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gotoLogin();
			}

		});

		// 绑定监听
		remember_password.setOnClickListener(new ClickListener());
		remember.setOnClickListener(new ClickListener());
		forget.setOnClickListener(new ClickListener());
		login.setOnClickListener(new ClickListener());
		register.setOnClickListener(new ClickListener());
		question1.setOnItemSelectedListener(new ItemSelectedListener());
		question2.setOnItemSelectedListener(new ItemSelectedListener());
		question3.setOnItemSelectedListener(new ItemSelectedListener());

	}

	public class ClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.login_checkbox_remember:
				// 记住密码的checkbox的监听
				isRememberPassword = remember_password.isChecked();
				break;
			case R.id.login_textview_remember:
				// 记住密码的textview的监听
				remember_password.setChecked(!remember_password.isChecked());
				isRememberPassword = remember_password.isChecked();
				break;
			case R.id.login_textview_forget:
				// 忘记密码的的监听
				UserName = username.getText().toString();
				RetrievePasswordDialog dialog = new RetrievePasswordDialog(
						LoginActivity.this, UserName);
				dialog.show();
				break;
			case R.id.login_button_login:
				// 注册按钮的监听
				SelectedPassword = null;
				UserName = username.getText().toString();
				Password = password.getText().toString();
				// 查询密码的线程
				SelectPassword SelectPasswordThread = new SelectPassword(
						"select password");
				if (UserName.equals("")) {
					Toast.makeText(getApplicationContext(), "别急，还没填用户名",
							Toast.LENGTH_SHORT).show();
				} else {
					SelectPasswordThread.run();
					if (Password.equals(SelectedPassword)) {
						editor = pref.edit();
						if (isRememberPassword) {
							editor.putBoolean("remember_password", true);
							editor.putString("username", UserName);
							editor.putString("password", Password);
						} else {
							editor.clear();
						}
						editor.commit();
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						intent.putExtra("current_username", UserName);
						LoginActivity.this.startActivity(intent);
						Toast.makeText(getApplicationContext(),
								"欢迎观临，" + UserName, Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "密码错误",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case R.id.login_button_register:
				// 登录按钮的监听
				SelectedUserName = null;
				UserName = username.getText().toString();
				Password = password.getText().toString();
				PhoneNumber = phone.getText().toString();
				a1 = answer1.getText().toString();
				a2 = answer1.getText().toString();
				a3 = answer1.getText().toString();
				// 查询用户名的线程
				SelectUserName SelectUserNameThread = new SelectUserName(
						"select username");
				if (UserName.equals("")) {
					Toast.makeText(getApplicationContext(), "用户名不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					SelectUserNameThread.run();
					if (UserName.equals(SelectedUserName)) {
						Toast.makeText(getApplicationContext(), "用户名已存在,换一个吧",
								Toast.LENGTH_SHORT).show();
					} else {
						if (DataValid.isPasswordValid(Password)) {
							if (DataValid.isPhoneNumberValid(PhoneNumber)) {
								if (a1.equals("") || a2.equals("")
										|| a3.equals("")) {
									Toast.makeText(getApplicationContext(),
											"为了您的账号安全，请填写密保问题",
											Toast.LENGTH_SHORT).show();
								} else {
									SQLiteDatabase db = dbhelper
											.getWritableDatabase();
									ContentValues values = new ContentValues();
									values.put("username", UserName);
									values.put("password", Password);
									values.put("phone", PhoneNumber);
									values.put("question1", q1);
									values.put("answer1", a1);
									values.put("question2", q2);
									values.put("answer2", a2);
									values.put("question3", q3);
									values.put("answer3", a3);
									db.insert("user", null, values);
									values.clear();
									Toast.makeText(getApplicationContext(),
											"注册成功", Toast.LENGTH_SHORT).show();
									gotoLogin();
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"请输入正确的电话号码", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							Toast.makeText(getApplicationContext(),
									"密码不合法，必须为6位以上字符或数字且必须包含至少一个字符",
									Toast.LENGTH_SHORT).show();
						}

					}
				}
				break;
			default:
				break;
			}
		}
	}

	// 密保问题的Spinner的监听，用来记住用户选择了哪一个问题
	public class ItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			int position = arg2;
			switch (arg0.getId()) {
			case R.id.login_spinner_question1:
				q1 = position;
				break;
			case R.id.login_spinner_question2:
				q2 = position;
				break;
			case R.id.login_spinner_question3:
				q3 = position;
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	// 查询用户名的线程
	public class SelectUserName extends Thread {

		private DBHelper dbhelper;

		public SelectUserName(String str) {
			super(str);
		}

		public void run() {
			dbhelper = new DBHelper(LoginActivity.this, "master.db", null, 1);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor cursor = db.query("user", new String[] { "username" },
					"username = '" + UserName + "'", null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					if (cursor != null)
						SelectedUserName = cursor.getString(0);
					Log.d("lalala-SelectedUserName", SelectedUserName);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
	}

	public class SelectPassword extends Thread {

		private DBHelper dbhelper;

		public SelectPassword(String str) {
			super(str);
		}

		public void run() {
			dbhelper = new DBHelper(LoginActivity.this, "master.db", null, 1);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor cursor = db.query("user", new String[] { "password" },
					"username = '" + UserName + "'", null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					if (cursor != null)
						SelectedPassword = cursor.getString(0);
					Log.d("lalala-SelectedPassword", SelectedPassword);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
	}

	public void gotoRegister() {
		// 负责登录的组件GONE，负责注册的组件VISIBLE
		account.setVisibility(View.GONE);
		login.setVisibility(View.GONE);
		layout_remember.setVisibility(View.GONE);
		goto_register.setVisibility(View.GONE);

		phone.setVisibility(View.VISIBLE);
		layout_question1.setVisibility(View.VISIBLE);
		layout_answer1.setVisibility(View.VISIBLE);
		layout_question2.setVisibility(View.VISIBLE);
		layout_answer2.setVisibility(View.VISIBLE);
		layout_question3.setVisibility(View.VISIBLE);
		layout_answer3.setVisibility(View.VISIBLE);
		register.setVisibility(View.VISIBLE);
		goto_login.setVisibility(View.VISIBLE);
	}

	public void gotoLogin() {
		// 负责注册的组件GONE，负责登录的组件VISIBLE
		phone.setVisibility(View.GONE);
		layout_question1.setVisibility(View.GONE);
		layout_answer1.setVisibility(View.GONE);
		layout_question2.setVisibility(View.GONE);
		layout_answer2.setVisibility(View.GONE);
		layout_question3.setVisibility(View.GONE);
		layout_answer3.setVisibility(View.GONE);
		register.setVisibility(View.GONE);
		goto_login.setVisibility(View.GONE);

		account.setVisibility(View.VISIBLE);
		layout_remember.setVisibility(View.VISIBLE);
		login.setVisibility(View.VISIBLE);
		goto_register.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
