package com.example.orderplatform;

// 修改密码的dialog

import specification.DataValid;
import db.DBHelper;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlterPasswordDialog extends Dialog {

	private String currUser = null;
	private String SelectedPassword = null;

	private DBHelper dbhelper;

	public AlterPasswordDialog(Context context, String UserName) {
		super(context, R.style.dialog);
		// 在构造函数中获取当前用户的用户名
		this.currUser = UserName;
		// 加载布局并给布局的控件设置点击事件
		View view = getLayoutInflater().inflate(R.layout.dialog_alter, null);
		final EditText EditOldPassword = (EditText) view
				.findViewById(R.id.alter_oldpassword);
		final EditText EditNewPassword1 = (EditText) view
				.findViewById(R.id.alter_newpassword1);
		final EditText EditNewPassword2 = (EditText) view
				.findViewById(R.id.alter_newpassword2);
		Button confirm = (Button) view.findViewById(R.id.alter_confirm);
		Button cancel = (Button) view.findViewById(R.id.alter_cancel);

		// 确认按钮的监听
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String oldPassword = EditOldPassword.getText().toString();
				String newPassword1 = EditNewPassword1.getText().toString();
				String newPassword2 = EditNewPassword2.getText().toString();
				// 从数据库中查询当前用户的密码
				SelectPassword(currUser);
				// 如果有数据为空
				if (oldPassword.equals("") || newPassword1.equals("")
						|| newPassword2.equals("")) {
					Toast.makeText(getContext(), "请输入...", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 如果旧密码填写错误
					if (!oldPassword.equals(SelectedPassword)) {
						Toast.makeText(getContext(), "密码错误，不能修改",
								Toast.LENGTH_SHORT).show();
					} else {
						//　如果两次输入的密码不一致
						if (!newPassword1.equals(newPassword2)) {
							Toast.makeText(getContext(), "两次输入密码不一致",
									Toast.LENGTH_SHORT).show();
						} else {
							// 如果新密码不符合规范
							if (!DataValid.isPasswordValid(newPassword1)) {
								Toast.makeText(getContext(),
										"新密码不合法，必须为6位以上字符或数字且必须包含至少一个字符",
										Toast.LENGTH_SHORT).show();
							} else {
								// 如果新密码和旧密码相同
								if (newPassword1.equals(SelectedPassword)) {
									Toast.makeText(getContext(), "新密码不能和旧密码相同",
											Toast.LENGTH_SHORT).show();
								} else {
									AlterPassword(currUser, newPassword1);
									// 修改密码后跳转至登录界面
									Intent intent = new Intent();
									intent.setClass(getContext(),
											LoginActivity.class);
									getContext().startActivity(intent);
								}
							}
						}
					}
				}
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}

		});
		super.setContentView(view);
	}

	// 从数据库中查询当前用户的密码
	public void SelectPassword(String UserName) {
		dbhelper = new DBHelper(getContext(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query("user", new String[] { "password" },
				"username = '" + UserName + "'", null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				if (cursor != null)
					SelectedPassword = cursor.getString(cursor
							.getColumnIndex("password"));
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	// 将数据库中保存的密码改为新密码
	public void AlterPassword(String user, String newPassword) {
		dbhelper = new DBHelper(getContext(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("password", newPassword);
		db.update("user", values, "username = ?", new String[] { user });
		Toast.makeText(getContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 预先设置Dialog的一些属性
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		WindowManager m = getWindow().getWindowManager();
		Display d = m.getDefaultDisplay();
		getWindow().setAttributes(p);
		p.height = (int) (d.getHeight() * 0.6);
		p.width = d.getWidth();
		p.gravity = Gravity.LEFT | Gravity.BOTTOM;
		dialogWindow.setAttributes(p);
	}

}
