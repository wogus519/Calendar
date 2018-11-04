package com.example.wogus.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;

/**
 * Created by wogus on 2018-11-01.
 */

public class PageAdapterWeekCal extends FragmentStatePagerAdapter {
	Calendar cal;
	public PageAdapterWeekCal(FragmentManager fm , Calendar cal)
	{
		super(fm);
		this.cal = cal;
	}

	@Override
	public Fragment getItem(int position) {
		Calendar calBuf=Calendar.getInstance();
		calBuf.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
		calBuf.set(Calendar.WEEK_OF_YEAR,(position-48*50));

		FragmentWeekCalendar fragment = new FragmentWeekCalendar( );
		Bundle bundle = new Bundle(3); 						// 파라미터는 전달할 데이터 개수

		bundle.putInt("year", calBuf.get(Calendar.YEAR)); 				// key , value
		bundle.putInt("month",calBuf.get(Calendar.MONTH));
		bundle.putInt("date", calBuf.get(Calendar.DATE));
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public int getCount() {
		// total page count
		return 12*101*4;
	}

}
