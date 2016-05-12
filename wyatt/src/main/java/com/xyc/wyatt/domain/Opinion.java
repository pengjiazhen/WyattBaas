package com.xyc.wyatt.domain;

import cn.bmob.v3.BmobObject;

public class Opinion extends BmobObject{

	private String content;
	private String phone;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
