package com.example.orderplatform;

// 选择用餐时间的Activity

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DateActivity extends Activity {

	private DatePicker datepicker;
	private TimePicker timepicker;
	private Button btn_confirm;

	private String ReserveDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date);

		datepicker = (DatePicker) findViewById(R.id.DatePicker);
		timepicker = (TimePicker) findViewById(R.id.TimePicker);
		btn_confirm = (Button) findViewById(R.id.confirm_date);
		// 确认按钮的监听
		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 获取用户选择的用餐时间
				int year = datepicker.getYear();
				int month = datepicker.getMonth() + 1;
				int day = datepicker.getDayOfMonth();
				int hour = timepicker.getCurrentHour();
				int minute = timepicker.getCurrentMinute();
				ReserveDate = String.valueOf(year) + "年"
						+ String.valueOf(month) + "月" + String.valueOf(day)
						+ "日" + String.valueOf(hour) + "时"
						+ String.valueOf(minute) + "分";
				// 将用户设置的用餐时间传给MainActivity
				Intent intent = new Intent();
				intent.putExtra("ReserveDate", ReserveDate);
				setResult(RESULT_OK, intent);
				finish();
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.date, menu);
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
