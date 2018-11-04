package com.example.wogus.calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by wogus on 2018-11-01.
 */

public class FragmentMonthCalendar extends Fragment {
	int year,month;
	GridView gvMonthCalendar;
	AdapterGridMonthCalendar monthlyGridAdapter;
	LayoutInflater inflater;
	public FragmentMonthCalendar(){	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_month_calendar, container, false);
		this.inflater = inflater;

		year = getArguments().getInt("year");
		month = getArguments().getInt("month");
		gvMonthCalendar = view.findViewById(R.id.gvMonthCalendar);
		setAdpaterGridMonthCal();

		return view;
	}

	public void setAdpaterGridMonthCal(){
		int length;
		Calendar firstDayMonth = Calendar.getInstance();
		Calendar lastDayMonth = Calendar.getInstance();

		firstDayMonth.set(year,month,1);
		lastDayMonth.set(year,month,firstDayMonth.getActualMaximum(Calendar.DATE));

		length = firstDayMonth.get(Calendar.DAY_OF_WEEK)+6-lastDayMonth.get(Calendar.DAY_OF_WEEK)+lastDayMonth.get(Calendar.DATE);
		firstDayMonth.set(Calendar.DATE,2-firstDayMonth.get(Calendar.DAY_OF_WEEK));

		monthlyGridAdapter = new AdapterGridMonthCalendar(inflater.getContext(),length,firstDayMonth,gvMonthCalendar,month);
		gvMonthCalendar.setAdapter(monthlyGridAdapter);
	}
}

class AdapterGridMonthCalendar extends BaseAdapter{
	int length,month;
	Context context;
	LayoutInflater inf;
	Calendar startCal;
	GridView gvMonthCalendar;
	DBHelper dbHelper;
	Holiday holiday;
	public AdapterGridMonthCalendar(Context context, int length, Calendar startCal, GridView gvMonthCalendar, int month) {
		this.context = context;
		this.length = length;
		inf = (LayoutInflater) context.getSystemService	(Context.LAYOUT_INFLATER_SERVICE);
		this.startCal = startCal;
		this.gvMonthCalendar=gvMonthCalendar;
		this.month=month;
		dbHelper = 	dbHelper = new DBHelper(context, "Schedule.db", null, 1);
		holiday = new Holiday();
	}

	@Override
	public int getCount() {
		return length;
	}

	@Override
	public Calendar getItem(int position) {
		Calendar cal = Calendar.getInstance();
		cal.set(startCal.get(Calendar.YEAR),startCal.get(Calendar.MONTH),startCal.get(Calendar.DATE)+position);
		return cal;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		String[] holidayInfo = new String[2];
		if (convertView==null)
			convertView = inf.inflate(R.layout.item_gridview_month_calendar, null);

		final Calendar cal = getItem(position);
		TextView tvDate = convertView.findViewById(R.id.tvDate);
		ImageView ivSchedule = convertView.findViewById(R.id.ivSchedule);
		TextView tvHoliday = convertView.findViewById(R.id.tvHoliday);

		String schedule=dbHelper.select(cal.get(Calendar.YEAR)*10000+cal.get(Calendar.MONTH)*100+cal.get(Calendar.DATE));
		if(schedule!=null)
			ivSchedule.setVisibility(View.VISIBLE);
		else
			ivSchedule.setVisibility(View.INVISIBLE);
		tvDate.setText(""+cal.get(Calendar.DATE));
		try {
			holidayInfo = holiday.isHoliday(cal);
		}catch (Exception e){}

		if(holidayInfo[0].equals("true"))
			tvDate.setTextColor(Color.RED);
		else
			tvDate.setTextColor(Color.BLACK);
		if(holidayInfo[1].equals("false"))
			tvHoliday.setVisibility(View.INVISIBLE);
		else {
			tvHoliday.setVisibility(View.VISIBLE);
			tvHoliday.setText(holidayInfo[1]);
		}
		if(month!=cal.get(Calendar.MONTH))
			convertView.setAlpha((float)0.3);

		convertView.setMinimumHeight(gvMonthCalendar.getHeight()/length*7);
		if(schedule!=null) {
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(inf.getContext(), ScheduleActivity.class);
					intent.putExtra("cal", cal);
					inf.getContext().startActivity(intent);
				}
			});
		}
		return convertView;
	}
}


