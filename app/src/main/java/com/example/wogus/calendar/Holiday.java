package com.example.wogus.calendar;

import android.icu.util.ChineseCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wogus on 2018-11-04.
 */

public class Holiday {

	public static String[] isHoliday(Calendar cal) throws Exception{
		String[] result = new String[2];
		long date = DateUtils.parseDate(""+(cal.get(Calendar.YEAR)*10000+(cal.get(Calendar.MONTH)+1)*100+cal.get(Calendar.DATE)), "yyyyMMdd").getTime();
		if(isLegalHoliday(date,result) || isWeekend(date) || isAlternative(date))
			result[0] = "true";
		else
			result[0] = "false";

		return result;
	}

	/**
	 * 음력날짜 구하기
	 * @param date
	 * @return
	 */
	public static String getLunarDate(long date) {
		ChineseCalendar cc = new ChineseCalendar(new java.util.Date(date));
		String m = String.valueOf(cc.get(ChineseCalendar.MONTH) + 1);
		m = StringUtils.leftPad(m, 2, "0");
		String d = String.valueOf(cc.get(ChineseCalendar.DAY_OF_MONTH));
		d = StringUtils.leftPad(d, 2, "0");

		return m + d;
	}

	/**
	 * 법정휴일
	 * @param date
	 * @return
	 */
	public static boolean isLegalHoliday(long date,String[] result) {
		String[] solar = {"0101", "0301", "0505", "0606", "0815", "1225"};
		String[] lunar = {"0101", "0102", "0408", "0814", "0815", "0816", "1230"};
		HashMap<String,String> hmSolar = new HashMap<>();
		HashMap<String,String> hmLunar = new HashMap<>();
		hmSolar.put("0101","신정");
		hmSolar.put("0301","삼일절");
		hmSolar.put("0505","어린이날");
		hmSolar.put("0606","현충일");
		hmSolar.put("0815","광복절");
		hmSolar.put("1225","크리스마스");
		hmLunar.put("0101","설날");
		hmLunar.put("0102","설날연휴");
		hmLunar.put("0408","부처님오신날");
		hmLunar.put("0814","추석연휴");
		hmLunar.put("0815","추석");
		hmLunar.put("0816","추석연휴");
		hmLunar.put("1230","설날연휴");
		List<String> solarList = Arrays.asList(solar);
		List<String> lunarList = Arrays.asList(lunar);

		String solarDate = DateFormatUtils.format(date, "MMdd");
		ChineseCalendar cc = new ChineseCalendar(new java.util.Date(date));

		String m = String.valueOf(cc.get(ChineseCalendar.MONTH) + 1);
		m = StringUtils.leftPad(m, 2, "0");
		String d = String.valueOf(cc.get(ChineseCalendar.DAY_OF_MONTH));
		d = StringUtils.leftPad(d, 2, "0");

		String lunarDate = m + d;

		if (solarList.indexOf(solarDate) >= 0) {
			result[1] = hmSolar.get(solarDate);
			return true;
		}
		if (lunarList.indexOf(lunarDate) >= 0) {
			result[1] = hmLunar.get(lunarDate);
			return true;
		}
		result[1]="false";
		return false;
	}

	/**
	 * 주말 (토,일)
	 * @param date
	 * @return
	 */
	public static boolean isWeekend(long date) {
		boolean result = false;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);

		//SUNDAY:1 SATURDAY:7
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if ( dayOfWeek == Calendar.SUNDAY) {
			result = true;
		}
		return result;
	}

	/**
	 * 대체공휴일
	 * @param date
	 * @return
	 */
	public static boolean isAlternative(long date) {
		boolean result = false;

		//설날 연휴와 추석 연휴가 다른 공휴일과 겹치는 경우 그 날 다음의 첫 번째 비공휴일을 공휴일로 하고, 어린이날이 토요일 또는 다른 공휴일과 겹치는 경우 그 날 다음의 첫 번째 비공휴일을 공휴일로 함
		//1. 어린이날
		String year = DateFormatUtils.format(date, "yyyy");
		java.util.Date d = null;
		try {
			d = DateUtils.parseDate(year+"0505", "yyyyMMdd");
		} catch (ParseException e) {}
		if (isWeekend(d.getTime()) == true) {
			d = DateUtils.addDays(d, 1);
		}
		if (isWeekend(d.getTime()) == true) {
			d = DateUtils.addDays(d, 1);
		}
		if (DateUtils.isSameDay(new java.util.Date(date), d) == true) {
			result = true;
		}

		//2. 설
		String lunarDate = getLunarDate(date);
		Calendar calendar = Calendar.getInstance();
		d = new java.util.Date(date);
		if (StringUtils.equals(lunarDate, "0103")) {

			d = DateUtils.addDays(d, -1);
			calendar.setTime(d);
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				return true;
			}

			d = DateUtils.addDays(d, -1);
			calendar.setTime(d);
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				return true;
			}

			d = DateUtils.addDays(d, -1);
			calendar.setTime(d);
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				return true;
			}
		}

		//3. 추석
		d = new java.util.Date(date);
		if (StringUtils.equals(lunarDate, "0817")) {
			d = DateUtils.addDays(d, -1);
			calendar.setTime(d);
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				return true;
			}

			d = DateUtils.addDays(d, -1);
			calendar.setTime(d);
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				return true;
			}

			d = DateUtils.addDays(d, -1);
			calendar.setTime(d);
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				return true;
			}
		}
		return result;
	}
}
