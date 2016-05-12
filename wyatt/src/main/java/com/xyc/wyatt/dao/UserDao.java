package com.xyc.wyatt.dao;

import java.util.List;

import com.xyc.wyatt.domain.User;

import android.content.Context;

public class UserDao extends WTDaoSupport<User> {

	public UserDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return super.findAll();
	}
	
}
