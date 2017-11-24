package com.example.orderplatform;

// 找回密码的dialog

import db.DBHelper;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

public class RetrievePasswordDialog extends Dialog {

	private String currUser = null;
	private String SelectedPassword = null;
	private String SelectedAnswer1 = null;
	private String SelectedAnswer2 = null;
	private String SelectedAnswer3 = null;
	private int question1Index = 0;
	private int question2Index = 0;
	private int question3Index = 0;
	private String[] question1s;
	private String[] question2s;
	private String[] question3s;

	private DBHelper dbhelper;

	public RetrievePasswordDialog(Context context, String UserName) {
		super(context, R.style.dialog);
		this.currUser = UserName;
		// 加载布局并给布局的控件设置点击事件
		// 得到预先设置的所有密保问题
		question1s = getContext().getResources().getStringArray(
				R.array.question_password1);
		question2s = getContext().getResources().getStringArray(
				R.array.question_password2);
		question3s = getContext().getResources().getStringArray(
				R.array.question_password3);

		View view = getLayoutInflater().inflate(R.layout.dialog_retrieve, null);
		TextView question1 = (TextView) view
				.findViewById(R.id.retrieve_question1);
		TextView question2 = (TextView) view
				.findViewById(R.id.retrieve_question2);
		TextView question3 = (TextView) view
				.findViewById(R.id.retrieve_question3);
		final EditText EditAnswer1 = (EditText) view
				.findViewById(R.id.retrieve_answer1);
		final EditText EditAnswer2 = (EditText) view
				.findViewById(R.id.retrieve_answer2);
		final EditText EditAnswer3 = (EditText) view
				.findViewById(R.id.retrieve_answer3);
		Button confirm = (Button) view.findViewById(R.id.retrieve_confirm);
		Button cancel = (Button) view.findViewById(R.id.retrieve_cancel);

		// 从数据库中查询用户注册时预留的信息
		SelectUserInfo(currUser);
		// 用三个TextView显示用户注册时选择的密保问题
		question1.setText("问题1：" + question1s[question1Index]);
		question2.setText("问题2：" + question2s[question2Index]);
		question3.setText("问题3：" + question3s[question3Index]);

		// 确认按钮的监听
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 获取用户回答的答案
				String answer1 = EditAnswer1.getText().toString();
				String answer2 = EditAnswer2.getText().toString();
				String answer3 = EditAnswer3.getText().toString();
				// 如果有问题没回答
				if (answer1.equals("") || answer2.equals("")
						|| answer3.equals("")) {
					Toast.makeText(getContext(), "请先回答完以上3个问题",
							Toast.LENGTH_SHORT).show();
				} else {
					// 如果问题一答对
					if (answer1.equals(SelectedAnswer1)) {
						// 如果问题二答对
						if (answer2.equals(SelectedAnswer2)) {
							// 如果问题三答对
							if (answer3.equals(SelectedAnswer3)) {
								// 问题回答正确
								Toast.makeText(getContext(),
										"你的密码为：" + SelectedPassword,
										Toast.LENGTH_LONG).show();
								dismiss();
							} else {
								Toast.makeText(getContext(), "问题3回答错误",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getContext(), "问题2回答错误",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getContext(), "问题1回答错误",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

		});
		
		//　取消按钮的监听
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 关闭dialog
				dismiss();
			}

		});
		super.setContentView(view);
	}

	// 从数据库中查询用户注册时预留的信息
	public void SelectUserInfo(String UserName) {
		dbhelper = new DBHelper(getContext(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query("user", null, "username = '" + UserName + "'",
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				if (cursor != null) {
					//　记录用户密码
					SelectedPassword = cursor.getString(cursor
							.getColumnIndex("password"));
					// 记录问题1-3的下标
					question1Index = cursor.getInt(cursor
							.getColumnIndex("question1"));
					question2Index = cursor.getInt(cursor
							.getColumnIndex("question2"));
					question3Index = cursor.getInt(cursor
							.getColumnIndex("question3"));
					// 记录三个答案
					SelectedAnswer1 = cursor.getString(cursor
							.getColumnIndex("answer1"));
					SelectedAnswer2 = cursor.getString(cursor
							.getColumnIndex("answer2"));
					SelectedAnswer3 = cursor.getString(cursor
							.getColumnIndex("answer3"));
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
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
