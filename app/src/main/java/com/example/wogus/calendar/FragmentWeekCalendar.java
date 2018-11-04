package com.example.wogus.calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by wogus on 2018-11-01.
 */

public class FragmentWeekCalendar extends Fragment {

	int year,month,date;
	LayoutInflater inflater;
	AdapterGridWeekCalendar adapterGridWeekCalendar;
	GridView gvWeekCalendar;
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_week_calendar, container, false);
		this.inflater = inflater;

		year = getArguments().getInt("year");
		month = getArguments().getInt("month");
		date = getArguments().getInt("date");

		gvWeekCalendar = view.findViewById(R.id.gvWeekCalendar);
		setAdapterGridWeek();

		return view;
	}
	public void setAdapterGridWeek(){
		Calendar firstDayWeek = Calendar.getInstance();
		firstDayWeek.set(year,month,date);
		firstDayWeek.set(year,month,date+1-firstDayWeek.get(Calendar.DAY_OF_WEEK));
		adapterGridWeekCalendar = new AdapterGridWeekCalendar(inflater.getContext(),firstDayWeek,month,gvWeekCalendar);
		gvWeekCalendar.setAdapter(adapterGridWeekCalendar);
	}
}

class AdapterGridWeekCalendar extends BaseAdapter {
	Context context;
	LayoutInflater inf;
	int month;
	Calendar startCal;
	GridView gvWeekCalendar;
	DBHelper dbHelper;
	TextView tvSchedule, tvDate,tvHoliday;
	Holiday holiday;
	public AdapterGridWeekCalendar(Context context, Calendar startCal, int month,GridView gvWeekCalendar) {
		this.context = context;
		inf = (LayoutInflater) context.getSystemService	(Context.LAYOUT_INFLATER_SERVICE);
		this.startCal = startCal;
		this.month=month;
		this.gvWeekCalendar = gvWeekCalendar;
		dbHelper = new DBHelper(context, "Schedule.db", null, 1);
		holiday = new Holiday();
	}

	@Override
	public int getCount() {
		return 7;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null)
			convertView = inf.inflate(R.layout.item_gridview_week_calendar, null);
		String[] holidayInfo = new String[2];
		final Calendar cal = getItem(position);
		String schedule=dbHelper.select(cal.get(Calendar.YEAR)*10000+cal.get(Calendar.MONTH)*100+cal.get(Calendar.DATE));

		tvDate= convertView.findViewById(R.id.tvDate);
		tvSchedule = convertView.findViewById(R.id.tvSchedule);
		tvHoliday = convertView.findViewById(R.id.tvHoliday);
		try {
			holidayInfo = holiday.isHoliday(cal);
		}catch (Exception e){}

		tvDate.setText(""+cal.get(Calendar.DATE));
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
		if(schedule!=null)
			tvSchedule.setText(schedule);
		else
			tvSchedule.setText("");

		convertView.setMinimumHeight(gvWeekCalendar.getHeight());
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

