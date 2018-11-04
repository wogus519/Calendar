package com.example.wogus.calendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by wogus on 2018-11-02.
 */

public class ScheduleActivity extends AppCompatActivity{
	TextView tvDate;
	ImageView ivBack,ivDone,ivDelete;
	EditText etSchedule;
	Calendar cal;
	DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		initVar();
		setListener();
		setEtSchedule();
		tvDate.setText(dateFormat());
	}
	public void setEtSchedule(){
		String schedule = dbHelper.select(cal.get(Calendar.YEAR) * 10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE));
		if(schedule==null) {
			ivDelete.setVisibility(View.GONE);
			etSchedule.setText("");
		}else {
			ivDelete.setVisibility(View.VISIBLE);
			etSchedule.setText(schedule);
		}
	}
	public void  initVar(){
		cal = (Calendar) getIntent().getExtras().get("cal");
		tvDate = findViewById(R.id.tvDate);
		ivBack = findViewById(R.id.ivBack);
		ivDone = findViewById(R.id.ivDone);
		ivDelete = findViewById(R.id.ivDelete);
		etSchedule = findViewById(R.id.etSchedule);

		dbHelper = new DBHelper(getApplicationContext(), "Schedule.db", null, 1);
	}
	public String dateFormat(){
		switch (cal.get(Calendar.DAY_OF_WEEK)){
			case 1:
				return (cal.get(Calendar.YEAR)+"년 "+(cal.get(Calendar.MONTH)+1)+"월 "+cal.get(Calendar.DATE)+"일 ("+"일)");
			case 2:
				return (cal.get(Calendar.YEAR)+"년 "+(cal.get(Calendar.MONTH)+1)+"월 "+cal.get(Calendar.DATE)+"일 ("+"월)");
			case 3:
				return (cal.get(Calendar.YEAR)+"년 "+(cal.get(Calendar.MONTH)+1)+"월 "+cal.get(Calendar.DATE)+"일 ("+"화)");
			case 4:
				return (cal.get(Calendar.YEAR)+"년 "+(cal.get(Calendar.MONTH)+1)+"월 "+cal.get(Calendar.DATE)+"일 ("+"수)");
			case 5:
				return (cal.get(Calendar.YEAR)+"년 "+(cal.get(Calendar.MONTH)+1)+"월 "+cal.get(Calendar.DATE)+"일 ("+"목)");
			case 6:
				return (cal.get(Calendar.YEAR)+"년 "+(cal.get(Calendar.MONTH)+1)+"월 "+cal.get(Calendar.DATE)+"일 ("+"금)");
			default:
				return (cal.get(Calendar.YEAR)+"년 "+(cal.get(Calendar.MONTH)+1)+"월 "+cal.get(Calendar.DATE)+"일 ("+"토)");
		}
	}
	public void setListener() {
		ivDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String schedule = etSchedule.getText().toString();
				if(!schedule.equals("")) {
					if(dbHelper.select(cal.get(Calendar.YEAR) * 10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE))==null)
						dbHelper.insert(cal.get(Calendar.YEAR) * 10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE), schedule);
					else
						dbHelper.update(cal.get(Calendar.YEAR) * 10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE),schedule);
					Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}else
					Toast.makeText(getApplicationContext(), "빈 일정은 만들 수 없습니다", Toast.LENGTH_LONG).show();

			}
		});
		ivDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new AlertDialog.Builder(view.getContext())
						.setMessage("일정을 삭제하시겠습니까?")
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dbHelper.delete(cal.get(Calendar.YEAR) * 10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE));
								Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}})
						.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
							}})
						.show();
			}
		});
		ivBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		tvDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				DatePickerDialog dialog = new DatePickerDialog(ScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int year, int month, int date) {
						cal.set(year,month,date);
						tvDate.setText(dateFormat());
						setEtSchedule();
					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
				dialog.show();
			}
		});
		etSchedule.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>=100)
					Toast.makeText(getApplicationContext(), "입력할 수 있는 최대글자수는 100입니다.", Toast.LENGTH_LONG).show();
			}

			@Override
			public void afterTextChanged(Editable arg0) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {	}

		});
	}
}
