package com.example.wogus.calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wogus on 2018-11-02.
 */

public class DBHelper extends SQLiteOpenHelper {

	// DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
	public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	// DB를 새로 생성할 때 호출되는 함수
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
		db.execSQL("CREATE TABLE SCHEDULE (date INTEGER PRIMARY KEY , schedule TEXT);");
	}

	// DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	public String select(int date) {
		// 읽기가 가능하게 DB 열기
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT schedule FROM SCHEDULE WHERE date="+date, null);
		if(cursor.moveToNext())
			return cursor.getString(0);
		return null;
	}

	public void insert(int date,String schedule) {
		// 읽고 쓰기가 가능하게 DB 열기
		SQLiteDatabase db = getWritableDatabase();
		// DB에 입력한 값으로 행 추가
		db.execSQL("INSERT INTO SCHEDULE VALUES("+date+", '" + schedule + "');");
		db.close();
	}

	public void update(int date,String schedule) {
		SQLiteDatabase db = getWritableDatabase();
		// 입력한 항목과 일치하는 행의 가격 정보 수정
		db.execSQL("UPDATE SCHEDULE SET schedule='" + schedule + "' WHERE date=" + date + ";");
		db.close();
	}

	public void delete(int date) {
		SQLiteDatabase db = getWritableDatabase();
		// 입력한 항목과 일치하는 행 삭제
		db.execSQL("DELETE FROM SCHEDULE WHERE date=" + date + ";");
		db.close();
	}
}

