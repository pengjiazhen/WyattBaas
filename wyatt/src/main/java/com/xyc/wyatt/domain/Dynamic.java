package com.xyc.wyatt.domain;

import cn.bmob.v3.BmobObject;

import com.xyc.wyatt.annotation.Column;
import com.xyc.wyatt.annotation.ID;
import com.xyc.wyatt.annotation.TableName;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@TableName("wt_dynamic")
public class Dynamic extends BmobObject implements Parcelable {
	/*
	 * id uid runFeel runImage points picture dtime
	 */
	@Column("id")
	//@ID(autoincrement = true)
	private int id;
	@Column("uid")
	private int uid;
	@Column("runFeel")
	private String runFeel;
	@Column("runImage")
	private String runImage;
	@Column("points")
	private int points;
	@Column("picture")
	private String picture;
	@Column("dtime")
	private String dtime;
	@Column("userName")
	private String userName;
	@Column("avatarPath")
	private String avatarPath;
	@Column("brand")
	private String brand;//手机型号

	
	

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public Dynamic() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getRunFeel() {
		return runFeel;
	}

	public void setRunFeel(String runFeel) {
		this.runFeel = runFeel;
	}

	public String getRunImage() {
		return runImage;
	}

	public void setRunImage(String runImage) {
		this.runImage = runImage;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDtime() {
		return dtime;
	}

	public void setDtime(String dtime) {
		this.dtime = dtime;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dynamic other = (Dynamic) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeInt(uid);
		dest.writeString(runFeel);
		dest.writeString(runImage);
		dest.writeInt(points);
		dest.writeString(picture);
		dest.writeString(dtime);

		dest.writeString(avatarPath);
		dest.writeString(userName);
	}

	public Dynamic(Parcel source) {
		id = source.readInt();

		uid = source.readInt();

		runFeel = source.readString();

		runImage = source.readString();

		points = source.readInt();

		picture = source.readString();

		dtime = source.readString();
		avatarPath = source.readString();

		userName = source.readString();

	}

	public static final Parcelable.Creator<Dynamic> CREATOR = new Creator<Dynamic>() {

		@Override
		public Dynamic createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Dynamic(source);
		}

		@Override
		public Dynamic[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Dynamic[size];
		}

	};
}
