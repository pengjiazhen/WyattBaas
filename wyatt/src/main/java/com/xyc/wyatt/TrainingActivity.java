package com.xyc.wyatt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xyc.wyatt.domain.RunRecord;
import com.xyc.wyatt.domain.WTimePattern;
import com.xyc.wyatt.manager.RunRecordManager;
import com.xyc.wyatt.util.DensityUtil;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.util.WTUtil;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class TrainingActivity extends BaseActivity {

	protected static final int NONET = 0;
	protected static final int GET_FAIL = 1;
	private View mRightView;
	private Context mContext;
	private RunRecordAdapter adapter;
	private ListView training_list;
	private ImageView training_iv_caidan;
	private LinkedList<RunRecord> list;

	Handler handler = new Handler() {

		public void dispatchMessage(android.os.Message msg) {

			switch (msg.what) {
			case NONET:
				Toast.makeText(mContext, "网络异常", 0).show();
				break;
			case GET_FAIL:
				Toast.makeText(mContext, "获取数据失败", 0).show();
				break;
			default:
				break;
			}
		};
	};
	private PopupWindow popupWindow;
	private TextView tv_tip;
	private View popu_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);// sherlockactionbar
																	// 转圈
		super.onCreate(savedInstanceState);
		mRightView = View.inflate(this, R.layout.activity_training, null);
		// 设置右边的布局
		addContentView(mRightView);
		mContext = this;
		GloableValue.currentContent = 1;
		initView();
		init();
		initEvent();
		// mRightView.setBackgroundResource(R.drawable.shadow);

	}

	protected void initView() {
		// 设置标题栏的样式
		initActionBar("训练日", true);

	}

	protected void init() {
		training_list = (ListView) findViewById(R.id.training_list);
		list = new LinkedList<RunRecord>();
		adapter = new RunRecordAdapter();
		training_list.setAdapter(adapter);
		if (GloableValue.user.getStatus() == WTContant.LOGIN) {
			getData();
		} else {
			// 没有登陆的界面
			getDateLoginOut();

		}
		

	}

	private void getDateLoginOut() {
		list.clear();
		for (int i = 1; i < 31; i++) {
			RunRecord record = new RunRecord();
			record.setIsCompleted("n");
			record.setDate("");
			list.addLast(record);
		}

	}

	private void getData() {
		showProgress();
		RunRecordManager.getRunRrcord(mContext,
				GloableValue.user.getUserName(), new FindListener<RunRecord>() {

					@Override
					public void onSuccess(List<RunRecord> clist) {
						for (RunRecord record : clist) {
							list.addLast(record);
							// dao.saveOrUpdata(record, "id=?", new
							// String[]{String.valueOf(record.getId())}, "id=?",
							// new String[]{String.valueOf(record.getId())});
						}
								adapter.notifyDataSetChanged();
								dismissProgress();
						

					}

					@Override
					public void onError(int arg0, String arg1) {
						dismissProgress();
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private class RunRecordAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				// TODO Auto-generated method stub
				convertView = View.inflate(mContext, R.layout.training_item,
						null);

				holder.tian = (TextView) convertView
						.findViewById(R.id.training_tv_tian);
				holder.date = (TextView) convertView
						.findViewById(R.id.training_tv_date);
				holder.isComplete = (ImageView) convertView
						.findViewById(R.id.training_iv_isComplete);
				holder.distance = (TextView) convertView
						.findViewById(R.id.training_tv_distance);
				holder.runkaluli = (TextView) convertView
						.findViewById(R.id.training_tv_runkaluli);
				holder.caidan = (ImageView) convertView
						.findViewById(R.id.training_iv_caidan);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tian.setText("第" + (position + 1) + "天");
			if (list.get(position).getDate()!=null && !"".endsWith(list.get(position).getDate())) {
				holder.date.setText(WTUtil.getSKTimePattern(WTimePattern.YMD,
						Long.parseLong(list.get(position).getDate())));
			} else {
				holder.date.setText("");
			}
			if (list.get(position).getIsCompleted().equals("y")) {
				holder.isComplete
						.setBackgroundResource(R.drawable.training_yes);
			} else {
				holder.isComplete.setBackgroundResource(R.drawable.training_no);
			}
			if(list.get(position)
					.getDistance()>0&&list.get(position).getIsCompleted().equals("n")){
				holder.distance.setText("建议距离"+String.valueOf(list.get(position)
						.getDistance()) + "m");
				holder.distance.setTextColor(Color.RED);
			}else{
				holder.distance.setText(String.valueOf(list.get(position)
						.getDistance()) + "m");
				holder.distance.setTextColor(Color.BLACK);
			}
			holder.runkaluli.setText(String.valueOf(list.get(position)
					.getRunkaluli()) + "卡路里");
			//vfinal View nContentView = convertView;
			holder.caidan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					showPopuWindow(list.get(position).getTip(),v,v);
					//PopupWindow popupWindow = new PopupWindow(mContext);
					//makeShortToast(list.get(position).getTip());
				}

				
			});
			final ViewHolder nholder = holder;
			return convertView;
		}

		class ViewHolder {
			TextView tian;// 第几天
			TextView date;// 日期
			ImageView isComplete;// 是否完成

			TextView distance;// 运动距离
			TextView runkaluli;// 消耗卡路里
			ImageView caidan;

		}

	}
	private void showPopuWindow(String tip,View v,View anchor) {
		if(popupWindow!=null){
			popupWindow.dismiss();
		}else{
			popu_view = View.inflate(mContext, R.layout.popu_tip, null);
			popupWindow = new PopupWindow(popu_view,-2,-2);
			tv_tip = (TextView) popu_view.findViewById(R.id.tv_tip);
			popupWindow.setTouchable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setTouchInterceptor(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
					{
						popupWindow.dismiss();
						return true;
					}
					return false;
				}
			});
		}
		if(tip!=null&&!"".equals(tip)){
			int[] location = new int[2];
			v.getLocationInWindow(location);
			int dip = 40;
			// 在代码里面设置的宽高值 都是像素。---》dip
			int px = DensityUtil.dip2px(getApplicationContext(), dip);
			popupWindow.showAtLocation(anchor, Gravity.TOP|Gravity.RIGHT,px, location[1]);
			tv_tip.setText(tip);
		}
		
	}

	protected void initEvent() {
		training_list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				disMissPopupWindow();
				
			}
		});
		mRightView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		training_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				if (list.get(position).getIsCompleted().equals("y")) {
					Intent intent = new Intent();

					// intent传递对象
					// intent.putExtra("runRecord", list.get(position));
					intent.putExtra("objectId", list.get(position).getObjectId());
					intent.putExtra("distanceRecom", list.get(position).getDistance());
					if((position+1)<list.size()){
						intent.putExtra("nextObjectId", list.get(position+1).getObjectId());
					}
					intent.setClass(getApplicationContext(), DayActivity.class);

					startActivity(intent);

				} else {
					if (GloableValue.user.getStatus() == WTContant.LOGIN) {
						Intent intent = new Intent();
						int p = (position + 1);
						intent.putExtra("day", String.valueOf(p));
						// intent传递对象
						intent.putExtra("distanceRecom", list.get(position).getDistance());
						intent.putExtra("objectId", list.get(position).getObjectId());
						if((position+1)<list.size()){
							intent.putExtra("nextObjectId", list.get(position+1).getObjectId());
						}
						intent.setClass(getApplicationContext(),
								RuningActivity.class);
						// intent.setClass(mContext, RuningActivity.class);
						startActivity(intent);
						finish();
					} else {
						// 跳转到登陆界面
						Intent intent = new Intent();
						intent.setClass(mContext, LoginActivity.class);
						intent.putExtra("where", "trainingActivity");
						startActivityForResult(intent,
								WTContant.REQUE_CODE_LOGIN);
					}

				}

			}

		});

	}
	/**
	 * 让popupwindow消失
	 */
	private void disMissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}

	}

}
