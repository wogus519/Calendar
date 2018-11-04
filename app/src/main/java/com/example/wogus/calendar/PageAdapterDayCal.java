package com.example.wogus.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;

/**
 * Created by wogus on 2018-11-01.
 */

public class PageAdapterDayCal extends FragmentStatePagerAdapter {
	Calendar cal;
	public PageAdapterDayCal(FragmentManager fm , Calendar cal)
	{
		super(fm);
		this.cal = cal;
	}

	@Override
	public Fragment getItem(int position) {
		Calendar calBuf=Calendar.getInstance();
		calBuf.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1);
		calBuf.set(Calendar.DAY_OF_YEAR,position-365 * 50);

		FragmentDayCalendar fragment = new FragmentDayCalendar( );
		Bundle bundle = new Bundle(3); 						// 파라미터는 전달할 데이터 개수

		bundle.putInt("year", calBuf.get(Calendar.YEAR)); 				// key , value
		bundle.putInt("month",calBuf.get(Calendar.MONTH));
		bundle.putInt("date",calBuf.get(Calendar.DATE));
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public int getCount() {
		// total page count
		return 365*101;
	}

}
