package com.example.wogus.calendar;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by wogus on 2018-11-01.
 */

public class FragmentDayCalendar extends Fragment {
	int year,month,date;
	DBHelper dbHelper;
	Holiday holiday;
	public FragmentDayCalendar(){	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		dbHelper = new DBHelper(getContext(), "Schedule.db", null, 1);
		holiday = new Holiday();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_day_calendar, container, false);

		String[] holidayInfo = new String[2];
		final Calendar cal = Calendar.getInstance();

		year = getArguments().getInt("year");
		month = getArguments().getInt("month");
		date = getArguments().getInt("date");

		cal.set(year,month,date);
		String schedule=dbHelper.select(year*10000+month*100+date);

		TextView tvSchedule = view.findViewById(R.id.tvSchedule);
		TextView tvHoliday = view.findViewById(R.id.tvHoliday);
		try {
			holidayInfo = holiday.isHoliday(cal);
		}catch (Exception e){}
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

		if(schedule!=null) {
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(inflater.getContext(), ScheduleActivity.class);
					intent.putExtra("cal", cal);
					inflater.getContext().startActivity(intent);
				}
			});
		}
		return view;
	}


}
