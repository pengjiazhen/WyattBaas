package com.xyc.wyatt;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;

import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer.InnerJavaBeanDeserializer;
import com.xyc.wyatt.dao.DynamicDao;
import com.xyc.wyatt.dao.UserDao;
import com.xyc.wyatt.domain.Comments;
import com.xyc.wyatt.domain.Dynamic;
import com.xyc.wyatt.domain.User;
import com.xyc.wyatt.manager.CallBack;
import com.xyc.wyatt.manager.NetMangaer;
import com.xyc.wyatt.manager.WyattcirleManager;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.JsonUtil;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.util.WTUtil;
import com.xyc.wyatt.view.RoundedImageView;
import com.xyc.wyatt.view.WTImageView;
import com.xyc.wyatt.view.XListView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WyattcirleActivity extends BaseActivity {
	private View mRightView;
	private XListView duimamic;
	private WyattcirleAdapte adapte;
	private LinkedList<Dynamic> list;
	private Context mContext;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mRightView = View.inflate(this, R.layout.wyattcirle_list, null);
		// 设置右边的布局
		addContentView(mRightView);
		GloableValue.currentContent = 3;
		mContext = this;
		initView();
		init();
		initEvent();
	}

	protected void initEvent() {

		duimamic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<Dynamic> innerList = adapte.getInnerList();
				if (innerList == null) {
					innerList = new LinkedList<Dynamic>();
				}
				Intent intent = new Intent();
				intent.putExtra("objectId", innerList.get(position-1).getObjectId());
			//	intent.putExtra("dynamic", innerList.get(position-1));
				intent.setClass(getApplicationContext(),
						CommentsActivity.class);
				startActivity(intent);
			}
		});
	}

	protected void init() {
		// TODO Auto-generated method stub
		duimamic = (XListView) findViewById(R.id.wyattcirleList);
		list = new LinkedList<Dynamic>();
		DynamicDao dynamicDao = new DynamicDao(mContext);
		List<Dynamic> loaclList = dynamicDao.findAll();
		showProgress();
		adapte = new WyattcirleAdapte();
		duimamic.setAdapter(adapte);
		getData(true, 1,CachePolicy.NETWORK_ELSE_CACHE);
		List<Dynamic> innerList = adapte.getInnerList();
		if (innerList == null) {
			innerList = new LinkedList<Dynamic>();
		}
		innerList.addAll(loaclList);
		adapte.setInnerList(innerList);
		adapte.notifyDataSetChanged();
		setRefresh(duimamic, new SetRefreshThings() {

			@Override
			public void pullUpToRefresh() {
				// TODO Auto-generated method stub
				currentPage++;
				getData(false, currentPage,null);
			}

			@Override
			public void pullDownToRefresh() {

				getData(true, 1,CachePolicy.NETWORK_ELSE_CACHE);


			}
		}, true, true);

	}

	private int currentPage = 1;

	// 获取数据
	public void getData(final boolean isPullDown, final int page,CachePolicy cachePolicy) {
		WyattcirleManager.getDuimamic(mContext,cachePolicy, page, new FindListener<Dynamic>() {
			
			@Override
			public void onSuccess(final List<Dynamic> curentPageList) {
				if (curentPageList != null && !curentPageList.isEmpty()) {
					if (list.contains(curentPageList.get(0))
							&& isPullDown) {
						list.clear();
					}
					DynamicDao dao = new DynamicDao(mContext);
					dao.delete(null, null);
					for (Dynamic dynamic : curentPageList) {
						dao.insert(dynamic);
						list.addLast(dynamic);
					}
				}
						List<Dynamic> innerList = adapte.getInnerList();
						if (innerList == null) {
							innerList = new LinkedList<Dynamic>();
						}
						if (curentPageList.isEmpty()) {
							makeShortToast("没有更多数据了哦");
						} else {
							if (isPullDown && !innerList.isEmpty()) {
								Dynamic d = curentPageList.get(0);
								Dynamic dd = innerList.get(0);
								if (d.getId() == dd.getId()) {
									makeShortToast("没有新的数据了哦");
								}
							}
						}
						duimamic.stopRefresh();
						duimamic.stopLoadMore();

						innerList.clear();
						innerList.addAll(list);
						adapte.setInnerList(innerList);
						adapte.notifyDataSetChanged();
						dismissProgress();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.e("wyattcirleActivity", arg1);
				makeShortToast("获取数据失败");
				duimamic.stopRefresh();
				duimamic.stopLoadMore();
				dismissProgress();
				
			}
		});

	}

	// 更新数据
	public void UpdatePonint(final int id, final String points) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!NetMangaer.checkNetworkAvailable(mContext)) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(mContext, "网络异常", 0).show();

						}
					});
					return;
				}
				WyattcirleManager.updateDuimamic(id, points, new CallBack() {

					@Override
					public void success(String result) {
						// TODO Auto-generated method stub
						adapte.notifyDataSetChanged();
					}

					@Override
					public void fail(String errorMessage) {
						// TODO Auto-generated method stub

					}

					@Override
					public void before() {
						// TODO Auto-generated method stub

					}
				});

			}
		}).start();
	}

	protected void initView() {
		// TODO Auto-generated method stub
		initActionBar("悦动圈", true);
	}

	private class WyattcirleAdapte extends BaseAdapter {

		private List<Dynamic> innerList;

		public List<Dynamic> getInnerList() {
			return innerList;
		}

		public void setInnerList(List<Dynamic> innerList) {
			this.innerList = innerList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return innerList != null ? innerList.size() : 0;
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
				ViewGroup parent) {
			// TODO Auto-generated method stub
			viewHolder holder = null;
			if (convertView == null) {
				holder = new viewHolder();
				convertView = View.inflate(mContext, R.layout.duimamic_item,
						null);
				holder.avatar = (RoundedImageView) convertView
						.findViewById(R.id.avatar);
				holder.username = (TextView) convertView
						.findViewById(R.id.username);
				holder.dtime = (TextView) convertView.findViewById(R.id.dtime);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				holder.map = (WTImageView) convertView.findViewById(R.id.map);
				holder.picture = (WTImageView) convertView
						.findViewById(R.id.picture);
				holder.forwarding = (ImageView) convertView
						.findViewById(R.id.forwarding);
				holder.comments = (ImageView) convertView
						.findViewById(R.id.comments);
				holder.praise = (ImageView) convertView
						.findViewById(R.id.praise);
				holder.praiseNum = (TextView) convertView
						.findViewById(R.id.praise_num);
				convertView.setTag(holder);
			} else {
				holder = (viewHolder) convertView.getTag();
			}

			// 设置用户信息
			String avatarPath = innerList.get(position).getAvatarPath();
			if (avatarPath != null && !"".equals(avatarPath)) {
				if (avatarPath.startsWith("http")) {
					holder.avatar.showImage(avatarPath, false);
				} else {
					holder.avatar.showImage(avatarPath, true);
				}
			} else {
				String path = WTContant.SERVER_IP + "/test1.jpg";
				holder.avatar.showImage(path, false);
			}
			// userlist.get(0).getUserName()
			holder.username.setText(innerList.get(position).getUserName());
			holder.avatar.setImageResource(R.drawable.avatar_default);
			holder.dtime.setText(WTUtil.getSKTimePattern(innerList
					.get(position).getDtime(), mContext));
			holder.content.setText(innerList.get(position).getRunFeel());
			holder.praiseNum.setText(innerList.get(position).getBrand()==null?"Android":innerList.get(position).getBrand());
			String mapUri = innerList.get(position).getRunImage();
			holder.map.showImage(mapUri, false);
			String pictureUri = innerList.get(position).getPicture();
			holder.picture.showImage(pictureUri, false);
			final viewHolder nholder = holder;
			holder.comments.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("objectId", innerList.get(position).getObjectId());
					//intent.putExtra("dynamic", innerList.get(position));
					intent.setClass(getApplicationContext(),
							CommentsActivity.class);
					startActivity(intent);
				}
			});

			/*holder.praise.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					String pointsNum = String.valueOf(list.get(position)
							.getPoints() + 1);
					nholder.praiseNum.setText(pointsNum);
					list.get(position).setPoints(
							list.get(position).getPoints() + 1);
					;

					// adapte.notifyDataSetChanged();
					UpdatePonint(innerList.get(position).getId(), pointsNum);
				}
			});*/
			return convertView;
		}

		class viewHolder {
			RoundedImageView avatar;
			TextView username;
			TextView dtime;
			TextView content;
			WTImageView map;
			WTImageView picture;
			ImageView forwarding;
			ImageView comments;
			ImageView praise;
			TextView praiseNum;
		}

	}

}
