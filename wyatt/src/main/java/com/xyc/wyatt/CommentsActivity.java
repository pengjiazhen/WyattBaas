package com.xyc.wyatt;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;

import com.baidu.mapapi.SDKInitializer;
import com.xyc.wyatt.dao.UserDao;
import com.xyc.wyatt.domain.Comments;
import com.xyc.wyatt.domain.Dynamic;
import com.xyc.wyatt.domain.User;
import com.xyc.wyatt.domain.WTimePattern;
import com.xyc.wyatt.manager.CallBack;
import com.xyc.wyatt.manager.CommentManager;
import com.xyc.wyatt.manager.DynamicManager;
import com.xyc.wyatt.manager.NetMangaer;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.JsonUtil;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.util.WTUtil;
import com.xyc.wyatt.view.RoundedImageView;
import com.xyc.wyatt.view.WTImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommentsActivity extends BaseSherlockActivity {
	private View mRightView;
	private Dynamic dynamic;
	private RoundedImageView avatar;
	private TextView username;
	private TextView dtime;
	private TextView runFeel;
	private WTImageView map;
	private WTImageView picture;
	private ListView commentListView;
	private CommentsAdapte adapter;
	private View headerView;
	private EditText et_content;
	private ImageButton send;
	private List<Comments> list;
	private Comments comment;
	private String objectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duimamic_comments);
		mContext = this;
		Intent intent = getIntent();
		if (intent != null) {
			showProgress();
			objectId = intent.getStringExtra("objectId");
			DynamicManager.getDynamicByobjectId(mContext, objectId, new GetListener<Dynamic>() {
				
				@Override
				public void onFailure(int arg0, String arg1) {
					makeShortToast("动态信息加载失败");
					Log.i("commentsActivity", arg1);
					dismissProgress();
				}
				
				@Override
				public void onSuccess(Dynamic dynamic) {
					
					// 设置用户信息
					String avatarPath = dynamic.getAvatarPath();
					if (avatarPath != null && !"".equals(avatarPath)) {
							avatar.showImage(avatarPath, false);
					} else {
						String path = WTContant.SERVER_IP + "/test2.jpg";
						avatar.showImage(path, false);
					}
					
					username.setText(dynamic.getUserName());
					runFeel.setText(dynamic.getRunFeel());

					dtime.setText((WTUtil.getSKTimePattern(dynamic.getDtime(), mContext)));
					map.showImage(dynamic.getRunImage(), false);
					picture.showImage(dynamic.getPicture(), false);
					getData(dynamic.getObjectId());
				}

				
			});
		}
		initView();
		init();
		initEvent();
	}
	private void getData(String objectId) {
		CommentManager.getComments(mContext, objectId, new FindListener<Comments>() {
			
			@Override
			public void onSuccess(List<Comments> arg0) {
				list = arg0;
				adapter.notifyDataSetChanged();
				dismissProgress();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				makeShortToast("评论加载失败");
				Log.i("commentsActivity", arg1);
				dismissProgress();
				
			}
		});
		
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		initActionBar("悦动圈", true);
		headerView = getLayoutInflater().inflate(
				R.layout.duimamic_comment_head, null);
		avatar = (RoundedImageView) headerView.findViewById(R.id.c_avatar);
		username = (TextView) headerView.findViewById(R.id.c_username);
		dtime = (TextView) headerView.findViewById(R.id.c_dtime);
		runFeel = (TextView) headerView.findViewById(R.id.c_runfeel);
		map = (WTImageView) headerView.findViewById(R.id.c_map);
		picture = (WTImageView) headerView.findViewById(R.id.c_picture);

		commentListView = (ListView) findViewById(R.id.duimamic_comment);
		et_content = (EditText) findViewById(R.id.et_comment);
		send = (ImageButton) findViewById(R.id.btn_send);

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		list = new ArrayList<Comments>();
		commentListView.addHeaderView(headerView);
		adapter = new CommentsAdapte();
		commentListView.setAdapter(adapter);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String content = et_content.getText().toString().trim();
				if (!TextUtils.isEmpty(content)) {
					et_content.setText(null);

					if (GloableValue.user.getStatus() == WTContant.LOGIN) {
						comment = new Comments();
						comment.setContent(content);
						comment.setDobjectId(objectId);
						comment.setUserName(GloableValue.user.getUserName());
						comment.setAvatarPath(GloableValue.user.getAvatar());
                        comment.setCtime(System.currentTimeMillis() + "");
						comment.setUid(GloableValue.user.getId());
						saveComment(comment);
						list.add(comment);
						adapter.notifyDataSetChanged();
						commentListView.setSelection(list.size());
					} else {
						// siqu denglu
						Intent intent = new Intent();
						intent.setClass(CommentsActivity.this,
								LoginActivity.class);
						startActivityForResult(intent, 101);
					}
				} else {
					Toast.makeText(mContext, "内容为空", 0).show();
				}
			}
		});
	}

	// 保存评论

	public void saveComment(final Comments comment) {
		showProgress();
		CommentManager.saveComments(comment, mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				makeShortToast("评论成功");
				dismissProgress();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				makeShortToast("评论失败");
				Log.i("commentsActivity", arg1);
				dismissProgress();
				
			}
		});
				
	}


	private class CommentsAdapte extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			viewHolder holder = null;
			if (convertView == null) {
				holder = new viewHolder();
				convertView = View.inflate(mContext, R.layout.comments_row,
						null);
				holder.cname = (TextView) convertView.findViewById(R.id.cname);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				holder.ctime = (TextView) convertView.findViewById(R.id.ctime);
				holder.avatar = (RoundedImageView) convertView.findViewById(R.id.avatar);
				convertView.setTag(holder);
			} else {
				holder = (viewHolder) convertView.getTag();

			}
		
			holder.cname.setText(list.get(position).getUserName()+":");
			holder.content.setText(list.get(position).getContent());
			holder.ctime.setText(WTUtil.getSKTimePattern(list.get(position)
					.getCtime(), mContext));
			holder.avatar.showImage(list.get(position).getAvatarPath(), false);
			return convertView;
		}

	}

	class viewHolder {

		TextView cname;
		TextView ctime;
		TextView content;
		RoundedImageView avatar;

	}

}
