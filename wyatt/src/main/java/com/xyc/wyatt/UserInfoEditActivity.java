package com.xyc.wyatt;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

import com.actionbarsherlock.view.MenuItem;
import com.alibaba.fastjson.JSONObject;
import com.xyc.wyatt.dao.UserDao;
import com.xyc.wyatt.manager.CallBack;
import com.xyc.wyatt.manager.UserManager;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoEditActivity extends BaseSherlockActivity {
	private ListView lv_user_info;
	private List<String> dataList;
	private int type;// 0表示是从修改age 1表示修改height 2表示修改weight
	private String value;

	private int selectPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_edit);
		type = getIntent().getIntExtra("type", 0);
		value = getIntent().getStringExtra("value");
		initView();
		init();
		initEvent();
	}

	@Override
	protected void initView() {
		lv_user_info = (ListView) findViewById(R.id.lv_user_info);
	}

	@Override
	protected void init() {
		dataList = new ArrayList<String>();
		if (type == 0) {
			initActionBar("修改年龄", true);
			for (int i = 10; i <= 60; i++) {
				dataList.add(i + "");
			}
		} else if (type == 1) {
			initActionBar("修改身高", true);
			for (int i = 60; i <= 240; i++) {
				dataList.add(i + "");
			}
		} else {
			initActionBar("修改体重", true);
			for (int i = 60; i <= 120; i++) {
				dataList.add(i + "");
			}
		}
		lv_user_info.setAdapter(new UserInfoAdapter());

	}

	@Override
	protected void initEvent() {
		lv_user_info.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent();
				intent.setClass(mContext, UserInfoActivity.class);
				intent.putExtra("value", dataList.get(position));
				if (type == 0) {//年龄
					setResult(RESULT_OK, intent);
					GloableValue.user.setAge(Integer.parseInt(dataList.get(position)));
					//访问网络
					String age = "age";
					///updateOnNet(age,Integer.parseInt(dataList.get(position))+"");
					updateUserInfo();
				} else if (type == 1) {//身高
					setResult(RESULT_OK, intent);
					GloableValue.user.setHeight(Double.parseDouble(dataList.get(position)));
					//访问网络
					String height = "height";
					//updateOnNet(height,Integer.parseInt(dataList.get(position))+"");
					updateUserInfo();
				} else if (type == 2) {//体重
					setResult(RESULT_OK, intent);
					GloableValue.user.setWeight(Double.parseDouble(dataList.get(position)));
					//访问网络
					String weight = "weight";
					//updateOnNet(weight,Integer.parseInt(dataList.get(position))+"");
					updateUserInfo();
				}
				finish();
			}

		});
	}
	private void updateUserInfo() {
		UserManager.updateUser(mContext, new UpdateListener() {
						@Override
						public void onSuccess() {
		                         makeShortToast("修改成功");										
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							makeShortToast("修改失败");
						}
					});
	}
	public class UserInfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext,
						R.layout.user_info_edit_item, null);
				holder.iv_selected = (ImageView) convertView
						.findViewById(R.id.iv_selected);
				holder.tv_value = (TextView) convertView
						.findViewById(R.id.tv_value);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_value.setText(dataList.get(position));
			holder.iv_selected.setVisibility(View.GONE);
			if (holder.tv_value.getText().equals(value)) {
				holder.iv_selected.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		class ViewHolder {
			TextView tv_value;
			ImageView iv_selected;
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==event.KEYCODE_BACK){
			back();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void back() {
		Intent intent = new Intent();
		intent.setClass(mContext, UserInfoActivity.class);
		intent.putExtra("value", value);
		if (type == 0) {
			setResult(RESULT_OK, intent);
		} else if (type == 1) {
			setResult(RESULT_OK, intent);
		} else if (type == 2) {
			setResult(RESULT_OK, intent);
		}
		finish();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		// 此处还可以做其他的bar响应，如果需要其他的，可以复写这个方法
		switch (id) {
		case android.R.id.home:
			back();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
