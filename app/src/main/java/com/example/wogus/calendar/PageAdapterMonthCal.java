package com.example.wogus.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;

/**
 * Created by wogus on 2018-11-01.
 */

public class PageAdapterMonthCal extends FragmentStatePagerAdapter	{
	Calendar cal;
        public PageAdapterMonthCal(FragmentManager fm , Calendar cal)
		{
			super(fm);
			this.cal = cal;
		}

		@Override
		public Fragment getItem(int position) {
        	Calendar calBuf=Calendar.getInstance();
        	calBuf.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1);
			calBuf.set(Calendar.MONTH,position-601);

			FragmentMonthCalendar fragment = new FragmentMonthCalendar( );
			Bundle bundle = new Bundle(2); 						// 파라미터는 전달할 데이터 개수

			bundle.putInt("year", calBuf.get(Calendar.YEAR)); 				// key , value
			bundle.putInt("month",calBuf.get(Calendar.MONTH));
			fragment.setArguments(bundle);

			return fragment;
		}

		@Override
		public int getCount() {
		// total page count
		return 12*101;
	}

}
