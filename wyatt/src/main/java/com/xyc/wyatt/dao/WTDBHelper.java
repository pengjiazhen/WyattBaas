package com.xyc.wyatt.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**


 */
public class WTDBHelper extends SQLiteOpenHelper {

	private static final String NAME = "wt.db";
	private static final int START_VERSION = 1;
	private static final int CURRENT_VERSION = 4;
	/**
	 * 表的主键
	 */
	public static String TABLE_ID = "id";
	/**
	 * 表名
	 */
	public static final String USERTABLE = "wt_user";
	public static final String DYNAMICTABLE = "wt_dynamic";
	public static final String RECORDTABLE = "wt_runrecord";

	public WTDBHelper(Context context) {
		super(context, NAME, null, CURRENT_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String user = "CREATE TABLE  IF NOT EXISTS " + USERTABLE
				+ " (objectId varchar(50),id INTEGER ,"
				+ "userName varchar(20), passWord varchar(100),"
				+ "avatar varchar(255),weight double,"
				+ "height double,age INTEGER," + "sex varchar(10),"
				+ "save1 varchar(255)," + "save2 varchar(255),"+"status INTERGER(255))";
		
		String dynamic = "CREATE TABLE  IF NOT EXISTS " + DYNAMICTABLE
				+ " (objectId varchar(50), id INTEGER , "
				+ " uid INTEGER, runFeel varchar(255), "
				+ " runImage varchar(255),points INTEGER,dtime varchar(255), "
				+ " picture varchar(255), " + " userName varchar(255), "
				+ " avatarPath varchar(255)) ";

			
		String record = "CREATE TABLE  IF NOT EXISTS " + RECORDTABLE
				+ " ( objectId varchar(50),id INTEGER , " + "uid INTEGER,"
				+ " isCompleted varchar(10)," + "distance double,"
				+ "date varchar(20),userName varchar(20)," + "runTime double," + "runkaluli double,"
				+ "runSpeed double," + "runLevel varchar(255),"
				+ "runImage varchar(255)," + "runType varchar(255),"
				+ "save1 varchar(255)," + "save2 varchar(255))";
		String userRunRecord = "insert into wt_runrecord(id,uid,isCompleted,distance,date,runTime,runkaluli,runSpeed,runLevel,runImage,runType,save1)"
								+"values (1,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(2,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(3,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(4,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(5,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(6,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(7,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(8,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(9,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(10,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(11,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(12,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(13,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(14,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(15,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(16,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(17,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(18,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(19,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(20,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(21,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(22,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(23,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(24,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(25,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(26,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(27,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(28,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(29,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(30,0,'n',0.0,'',0.0,0.0,0.0,'','','','')"
								+",(31,0,'n',0.0,'',0.0,0.0,0.0,'','','','')";
		db.execSQL(user);
		db.execSQL(dynamic);
		db.execSQL(record);
		db.execSQL(userRunRecord);

		onUpgrade(db, START_VERSION, CURRENT_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case CURRENT_VERSION:
			break;
		case START_VERSION:// 下一个版本的表更新信息
			break;
		case VERSION_3:// 下一个版本的表更新信息
			break;
		}
	}

	private static final int VERSION_3 = 3;
}
