package com.xyc.wyatt.domain;

import cn.bmob.v3.BmobObject;

import com.xyc.wyatt.annotation.Column;
import com.xyc.wyatt.annotation.ID;
import com.xyc.wyatt.annotation.TableName;

import android.os.Parcel;
import android.os.Parcelable;
@TableName("wt_runrecord")
public class RunRecord extends BmobObject implements Parcelable {
/*	id 记录ID
	uid 用户ID
	isCompleted 是否完成
	distance 运动路程
	date 日期
	runTime 所耗时间
	runkaluli 所耗卡路里
	runFeel 跑后感
	points  点赞数
	runImage 轨迹图片
	picture 自拍
	runType 运动类型...
	runSpeed 平均速度
	runLevel 运动等级
	save1
	save2*/
	@Column("id")
	@ID(autoincrement = false)
	private int id;
	@Column("uid")
	private int uid;
	@Column("isCompleted")
	private String isCompleted;
	@Column("distance")
	private double distance;
	@Column("date")
	private String date;
	@Column("runTime")
	private double runTime;
	@Column("runkaluli")
	private double runkaluli;
	@Column("runImage")
    private String runImage;
	@Column("runType")
    private String runType;
	@Column("runSpeed")
	private double runSpeed;
	@Column("runLevel")
	private String runLevel;
	@Column("save1")
	private String save1;
	@Column("userName")
	private String userName;
	//@Column("sava2")
	private String sava2;
	private String tip;
	
	
	public String getTip() {
		return tip;
	}


	public void setTip(String tip) {
		this.tip = tip;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public RunRecord() {
	}
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getUid() {
		return uid;
	}


	public void setUid(int uid) {
		this.uid = uid;
	}


	public String getIsCompleted() {
		return isCompleted;
	}


	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}


	public double getDistance() {
		return distance;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public double getRunTime() {
		return runTime;
	}


	public void setRunTime(double runTime) {
		this.runTime = runTime;
	}


	public double getRunkaluli() {
		return runkaluli;
	}


	public void setRunkaluli(double runkaluli) {
		this.runkaluli = runkaluli;
	}


	public String getRunImage() {
		return runImage;
	}


	public void setRunImage(String runImage) {
		this.runImage = runImage;
	}


	public String getRunType() {
		return runType;
	}


	public void setRunType(String runType) {
		this.runType = runType;
	}


	public double getRunSpeed() {
		return runSpeed;
	}


	public void setRunSpeed(double runSpeed) {
		this.runSpeed = runSpeed;
	}


	public String getRunLevel() {
		return runLevel;
	}


	public void setRunLevel(String runLevel) {
		this.runLevel = runLevel;
	}


	public String getSave1() {
		return save1;
	}


	public void setSave1(String save1) {
		this.save1 = save1;
	}


	public String getSava2() {
		return sava2;
	}


	public void setSava2(String sava2) {
		this.sava2 = sava2;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	/*
	private int id;
	private int uid;
	private String isCompleted;
	private String distance;
	private String date;
	private String runTime;
	private String runkaluli;
    private String runImage;
    private String runType;
	private String runSpeed;
	private String runLevel;
	private String save1;
	private String sava2;*/
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(uid);
		dest.writeString(isCompleted);
		dest.writeDouble(distance);
		dest.writeString(date);
		dest.writeDouble(runTime);
		dest.writeDouble(runkaluli);
		dest.writeString(runImage);
		dest.writeString(runType);
		dest.writeDouble(runSpeed);
		dest.writeString(runLevel);
		dest.writeString(save1);
		dest.writeString(sava2);
		
		
	}
	
	public RunRecord(Parcel source) {

		id =source.readInt();

		uid =source.readInt();

		isCompleted = source.readString();

		distance = source.readDouble();

		date = source.readString();

		runTime = source.readDouble();

		runkaluli = source.readDouble();

		runImage = source.readString();

		runType = source.readString();

		runSpeed = source.readDouble();

		runLevel = source.readString();

		save1 = source.readString();

		sava2 = source.readString();
		
	}
	
	public static final Parcelable.Creator<RunRecord> CREATOR = new Creator<RunRecord>() {

		@Override
		public RunRecord[] newArray(int size) {
			return new RunRecord[size];
		}

		@Override
		public RunRecord createFromParcel(Parcel source) {
			return new RunRecord(source);
		}
	};
	
	
}
