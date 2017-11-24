package com.example.orderplatform;

// �޸������dialog

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
		// �ڹ��캯���л�ȡ��ǰ�û����û���
		this.currUser = UserName;
		// ���ز��ֲ������ֵĿؼ����õ���¼�
		View view = getLayoutInflater().inflate(R.layout.dialog_alter, null);
		final EditText EditOldPassword = (EditText) view
				.findViewById(R.id.alter_oldpassword);
		final EditText EditNewPassword1 = (EditText) view
				.findViewById(R.id.alter_newpassword1);
		final EditText EditNewPassword2 = (EditText) view
				.findViewById(R.id.alter_newpassword2);
		Button confirm = (Button) view.findViewById(R.id.alter_confirm);
		Button cancel = (Button) view.findViewById(R.id.alter_cancel);

		// ȷ�ϰ�ť�ļ���
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String oldPassword = EditOldPassword.getText().toString();
				String newPassword1 = EditNewPassword1.getText().toString();
				String newPassword2 = EditNewPassword2.getText().toString();
				// �����ݿ��в�ѯ��ǰ�û�������
				SelectPassword(currUser);
				// ���������Ϊ��
				if (oldPassword.equals("") || newPassword1.equals("")
						|| newPassword2.equals("")) {
					Toast.makeText(getContext(), "������...", Toast.LENGTH_SHORT)
							.show();
				} else {
					// �����������д����
					if (!oldPassword.equals(SelectedPassword)) {
						Toast.makeText(getContext(), "������󣬲����޸�",
								Toast.LENGTH_SHORT).show();
					} else {
						//�����������������벻һ��
						if (!newPassword1.equals(newPassword2)) {
							Toast.makeText(getContext(), "�����������벻һ��",
									Toast.LENGTH_SHORT).show();
						} else {
							// ��������벻���Ϲ淶
							if (!DataValid.isPasswordValid(newPassword1)) {
								Toast.makeText(getContext(),
										"�����벻�Ϸ�������Ϊ6λ�����ַ��������ұ����������һ���ַ�",
										Toast.LENGTH_SHORT).show();
							} else {
								// ���������;�������ͬ
								if (newPassword1.equals(SelectedPassword)) {
									Toast.makeText(getContext(), "�����벻�ܺ;�������ͬ",
											Toast.LENGTH_SHORT).show();
								} else {
									AlterPassword(currUser, newPassword1);
									// �޸��������ת����¼����
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

	// �����ݿ��в�ѯ��ǰ�û�������
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

	// �����ݿ��б���������Ϊ������
	public void AlterPassword(String user, String newPassword) {
		dbhelper = new DBHelper(getContext(), "master.db", null, 1);
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("password", newPassword);
		db.update("user", values, "username = ?", new String[] { user });
		Toast.makeText(getContext(), "�����޸ĳɹ�", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Ԥ������Dialog��һЩ����
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
