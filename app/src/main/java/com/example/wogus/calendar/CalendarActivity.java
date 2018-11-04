package com.example.wogus.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	PageAdapterMonthCal pageAdapterMonthCal;
	PageAdapterWeekCal pageAdapterWeekCal;
	PageAdapterDayCal pageAdapterDayCal;
	int fragment_no;
	ViewPager viewPager;
	ImageView ivSchedule;
	TextView tvTitle;
	Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		readDate();
		initVar();
		setListener();
		callFragment();
		pageAdapterMonthCal.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		saveDate();
		super.onPause();
	}

	public void saveDate() {
		SharedPreferences sp = getSharedPreferences("calender", MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("mode", fragment_no);
		editor.commit();
	}

	public void readDate() {
		SharedPreferences sp = getSharedPreferences("calender", MODE_PRIVATE);
		fragment_no = sp.getInt("mode", 1);
	}
	public void initVar() {
		cal = Calendar.getInstance();

		pageAdapterMonthCal = new PageAdapterMonthCal(getSupportFragmentManager(), cal);
		pageAdapterWeekCal = new PageAdapterWeekCal(getSupportFragmentManager(), cal);
		pageAdapterDayCal = new PageAdapterDayCal(getSupportFragmentManager(), cal);

		viewPager = findViewById(R.id.pager);
		ivSchedule = findViewById(R.id.ivSchedule);
		tvTitle = findViewById(R.id.tvTitle);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		switch (fragment_no){
			case 1:
				navigationView.setCheckedItem(R.id.nav_month);
				break;
			case 2:
				navigationView.setCheckedItem(R.id.nav_week);
				break;
			case 3:
				navigationView.setCheckedItem(R.id.nav_day);
				break;
		}
	}
	public void setListener() {
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				Calendar calBuf = Calendar.getInstance();
				switch (fragment_no) {
					case 1:
						calBuf.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
						calBuf.set(Calendar.MONTH, position - 601);
						tvTitle.setText(calBuf.get(Calendar.YEAR) + "년 " + (calBuf.get(Calendar.MONTH) + 1) + "월");
						break;
					case 2:
						calBuf.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
						calBuf.set(Calendar.WEEK_OF_YEAR, (position - 48 * 50));
						tvTitle.setText(calBuf.get(Calendar.YEAR) + "년 " + (calBuf.get(Calendar.MONTH) + 1) + "월");
						break;
					case 3:
						calBuf.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
						calBuf.set(Calendar.DAY_OF_YEAR, position - 365 * 50);
						switch (calBuf.get(Calendar.DAY_OF_WEEK)) {
							case 1:
								tvTitle.setText(calBuf.get(Calendar.YEAR) + "년 " + (calBuf.get(Calendar.MONTH) + 1) + "월 " + calBuf.get(Calendar.DATE) + "일 (" + "일)");
								break;
							case 2:
								tvTitle.setText(calBuf.get(Calendar.YEAR) + "년 " + (calBuf.get(Calendar.MONTH) + 1) + "월 " + calBuf.get(Calendar.DATE) + "일 (" + "월)");
								break;
							case 3:
								tvTitle.setText(calBuf.get(Calendar.YEAR) + "년 " + (calBuf.get(Calendar.MONTH) + 1) + "월 " + calBuf.get(Calendar.DATE) + "일 (" + "화)");
								break;
							case 4:
								tvTitle.setText(calBuf.get(Calendar.YEAR) + "년 " + (calBuf.get(Calendar.MONTH) + 1) + "월 " + calBuf.get(Calendar.DATE) + "일 (" + "수)");
								break;
							case 5:
								tvTitle.setText(calBuf.get(Calendar.YEAR) + "년 " + (calBuf.get(Calendar.MONTH) + 1) + "월 " + calBuf.get(Calendar.DATE) + "일 (" + "목)");
								break;
							case 6:
								tvTitle.setText(calBuf.get(Calendar.YEAR) + "년 " + (calBuf.get(Calendar.MONTH) + 1) + "월 " + calBuf.get(Calendar.DATE) + "일 (" + "금)");
								break;
							default:
								tvTitle.setText(calBuf.get(Calendar.YEAR) + "년 " + (calBuf.get(Calendar.MONTH) + 1) + "월 " + calBuf.get(Calendar.DATE) + "일 (" + "토)");
						}
						break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		ivSchedule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(CalendarActivity.this, ScheduleActivity.class);
				intent.putExtra("cal", cal);
				startActivity(intent);
			}
		});
	}
	public void callFragment() {
		switch (fragment_no) {
			case 1:
				viewPager.setAdapter(pageAdapterMonthCal);
				viewPager.setCurrentItem(601 + cal.get(Calendar.MONTH));
				break;
			case 2:
				viewPager.setAdapter(pageAdapterWeekCal);
				viewPager.setCurrentItem(48 * 50 + cal.get(Calendar.WEEK_OF_YEAR));
				break;
			case 3:
				viewPager.setAdapter(pageAdapterDayCal);
				viewPager.setCurrentItem(365 * 50 + cal.get(Calendar.DAY_OF_YEAR));
				break;
		}

	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		switch (id) {
			case R.id.nav_month:
				fragment_no = 1;
				break;
			case R.id.nav_week:
				fragment_no = 2;
				break;
			case R.id.nav_day:
				fragment_no = 3;
				break;
		}

		callFragment();

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
}