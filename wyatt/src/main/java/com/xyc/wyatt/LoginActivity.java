package com.xyc.wyatt;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xyc.wyatt.dao.UserDao;
import com.xyc.wyatt.domain.User;
import com.xyc.wyatt.manager.NetMangaer;
import com.xyc.wyatt.manager.UserManager;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends BaseSherlockActivity implements
		OnClickListener, OnTouchListener, OnItemClickListener,
		OnDismissListener {
	protected static final int LOGIN_BEFORE = 0;
	protected static final int LOGIN_SUCCESS = 1;
	protected static final int LOGIN_FAIL = 2;
	protected static final int LOGIN_FAILS = 3;
	protected static final int LOGIN_NONET = 4;
	private ArrayList<String> mList = new ArrayList<String>();
	public Context mContext;
	private EditText et_login_username;
	private EditText et_login_password;
	private Button btn_login;
	private TextView tv_regesiter;
	private ImageView iv_see_password;

	private ImageView iv_login_choose;
	private PopupWindow mPopup;
	private boolean mInitPopup;
	private boolean mShowing;
	private ArrayAdapter<String> mAdapter;
	private ListView mListView;

	private boolean isRequesting = false;
	private boolean isOpen;

	private String where;
	private Handler handler = new Handler() {
		private AlertDialog dialog;

		public void dispatchMessage(android.os.Message msg) {

			switch (msg.what) {
			case LOGIN_BEFORE:
				// 登陆loading dialog 弹出框
				Builder builder = new AlertDialog.Builder(mContext);
				dialog = builder.create();
				View view = View.inflate(mContext, R.layout.dialog_loading,
						null);
				dialog.show();
				setDialogParams(dialog, view, 250, Gravity.CENTER);

				break;
			case LOGIN_SUCCESS:
				Toast.makeText(getApplicationContext(), "登陆成功...", 0).show();
				dialog.dismiss();
				if("runningActivity".equals(where) || "trainingActivity".equals(where)){
					Intent intent = new Intent();
					intent.setClass(mContext, LoginActivity.class);
					setResult(WTContant.REQUE_CODE_LOGIN,intent);
					finish();
				}
//				if("trainingActivity".equals(where)){
//					Intent intent = new Intent();
//					intent.setClass(mContext, LoginActivity.class);
//					setResult(WTContant.REQUE_CODE_LOGIN,intent);
//					finish();
//				}
				break;
			case LOGIN_FAIL:

				Toast.makeText(getApplicationContext(), "连接出问题了，请稍后重试", 0)
						.show();
				dialog.dismiss();
				break;

			case LOGIN_FAILS:
				Toast.makeText(getApplicationContext(), "登陆失败...", 0).show();
				dialog.dismiss();
				break;
			case LOGIN_NONET:
				Toast.makeText(getApplicationContext(), "没有网络...", 0).show();

				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		Intent intent = getIntent();
		if(intent!=null){
			where = intent.getStringExtra("where");
		}
		initView();
		init();
		initEvent();
	}

	@Override
	protected void initView() {
		et_login_username = (EditText) findViewById(R.id.et_login_username);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_regesiter = (TextView) findViewById(R.id.tv_regesiter);
		iv_see_password = (ImageView) findViewById(R.id.iv_see_password);
		iv_login_choose = (ImageView) findViewById(R.id.iv_login_choose);
	}

	@Override
	protected void init() {
		initActionBar("登录", true);
		loginChoose();
	}

	@Override
	protected void initEvent() {
		btn_login.setOnClickListener(this);
		tv_regesiter.setOnClickListener(this);
		iv_see_password.setOnTouchListener(this);
		iv_login_choose.setOnClickListener(this);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		// 此处还可以做其他的bar响应，如果需要其他的，可以复写这个方法
		switch (id) {
		case android.R.id.home:
			Intent intent = new Intent();
			intent.setClass(mContext, TrainingActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_login:// 点击登录按钮
			// 登陆
			// 用户输入信息
			final User user = new User();
			user.setUserName(et_login_username.getText().toString());
			user.setPassWord(et_login_password.getText().toString());
			if (!NetMangaer.checkNetworkAvailable(mContext)) {
				Message message = new Message();
				message.what = LOGIN_NONET;// 没有网络.
				handler.sendMessage(message);
				return;
			}
			if (!isRequesting) {
				doLogin(user);
			}

			break;
		case R.id.tv_regesiter:// 点击注册按钮
			// 跳转到注册界面
			Intent intent = new Intent();
			intent.setClass(mContext, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_login_choose:
			toggle();
			break;
		default:
			break;
		}
	}

	/**
	 * 登陆
	 * 
	 * @param user
	 */
	private void doLogin(final User user) {
		UserManager.userLogin(user, mContext, new FindListener<User>() {
			@Override
			public void onSuccess(List<User> currentList) {
				if(currentList.isEmpty()){
					makeShortToast("用户不存在或密码错误");
					return;
				}
				for (User user : currentList) {
					GloableValue.user.setObjectId(user.getObjectId());
					GloableValue.user.setId(user.getId());
					GloableValue.user.setUserName(user.getUserName());
					GloableValue.user.setAvatar(user.getAvatar());
					GloableValue.user.setWeight(user.getWeight());
					GloableValue.user.setHeight(user.getHeight());
					GloableValue.user.setAge(user.getAge());
					GloableValue.user.setSex(user.getSex());
					GloableValue.user.setStatus(WTContant.LOGIN);
				}
				
				// 记录用户的登陆状态 和用户的信息
				RememberUserMessage(user);
				
				if(!"runningActivity".equals(where)){
					// 登陆成功 跳转到主界面
					Intent intent = new Intent();
					intent.setClass(mContext,TrainingActivity.class);
					startActivity(intent);
				}
				finish();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				makeShortToast("出问题了，登陆失败");
			}
		});
		
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				com.xyc.wyatt.manager.UserManager.userLogin(user, mContext,
						new CallBack() {
							
							 * 登陆请求前
							 
							@Override
							public void before() {
								isRequesting = true;
								Message message = new Message();
								message.what = LOGIN_BEFORE;// 正在登陆...
								handler.sendMessage(message);
							}

							*//**
							 * 登陆成功
							 *//*
							@Override
							public void success(String result) {
								isRequesting = false;
								// json解析
								Log.i("responseResult", result);
								Object jsonObject;
								try {
									// Log.i("responseResult", result);
									jsonObject = JSONObject.parse(result);
									JSONObject parseObject = JSONObject
											.parseObject((String) jsonObject);
									// Log.i("responseResult","----------"
									// +(jsonObject instanceof String));
									// 获取response
									String responseCode = parseObject.get(
											"response").toString();
									if (Integer.parseInt(responseCode) == 0) {
										// 是0就没有问题
										Message message = new Message();
										message.what = LOGIN_SUCCESS;// 登陆成功
										handler.sendMessage(message);
										
										List<User> currentList = JsonUtil.getList(result, "userList",User.class);
										for (User user : currentList) {
											GloableValue.user.setId(user.getId());
											GloableValue.user.setUserName(user.getUserName());
											GloableValue.user.setAvatar(user.getAvatar());
											GloableValue.user.setWeight(user.getWeight());
											GloableValue.user.setHeight(user.getHeight());
											GloableValue.user.setAge(user.getAge());
											GloableValue.user.setSex(user.getSex());
											GloableValue.user.setStatus(WTContant.LOGIN);
										}
										
										// 记录用户的登陆状态 和用户的信息
										RememberUserMessage(user);
//									
										RunRecordManager.getRunRrcord(mContext, GloableValue.user.getId(), new CallBack() {
											
											@Override
											public void success(String result) {
												Log.i("result", result);
												RunRecordDao dao = new RunRecordDao(mContext);
												List<RunRecord> clist = JsonUtil.getList(result, "recordList", RunRecord.class);
												for(RunRecord record:clist){
													dao.saveOrUpdata(record, "id=?", new String[]{String.valueOf(record.getId())}, "id=?", new String[]{String.valueOf(record.getId())});						
												}
												if(!"runningActivity".equals(where)){
													// 登陆成功 跳转到主界面
													Intent intent = new Intent();
													intent.setClass(mContext,TrainingActivity.class);
													startActivity(intent);
													finish();
												}
												
											}
											@Override
											public void fail(String errorMessage) {
											}
											@Override
											public void before() {
											}
										});
										//getRunRecord();
										
									} else {
										// 存在问题
										Message message = new Message();
										message.what = LOGIN_FAILS;// 登陆失败
										handler.sendMessage(message);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							*//**
							 * 登陆失败
							 *//*
							@Override
							public void fail(final String errorMessage) {

								isRequesting = false;
								Message message = new Message();
								message.what = LOGIN_FAIL;// 网络异常
								handler.sendMessage(message);
							}
						});
			}
		}).start();*/
	}


	/**
	 * 记录用户的登陆状态 和用户的信息 到数据库
	 * 
	 * @param user
	 */
	private void RememberUserMessage(User user) {

		UserDao dao = new UserDao(mContext);

		// 比对用户登陆的用和本地数据库的信息
		List<User> users = dao.findByCondition("where userName=?",
				new String[] { GloableValue.user.getUserName() });
		if (users!=null&&!users.isEmpty()) {
			dao.saveOrUpdata(GloableValue.user, "status=?",new String[] { WTContant.OFF + "" }, "status=?",new String[] { WTContant.OFF + "" });
			// dao.updata(GloableValue.user);
		} else {
			dao.insert(GloableValue.user);
		}

		// 记录用户名到accoun.obj文件中 用于用户的多用户登陆
		remeber2account();
	}

	/**
	 * 记录用户名到account 用户多用户登陆
	 */
	private void remeber2account() {
		String input = et_login_username.getText().toString();
		mList.remove(input);
		mList.add(input);
		if (mList.size() > 3) {
			mList.remove(0);
		}
		ObjectOutputStream out = null;
		try {
			FileOutputStream os = openFileOutput("account.obj", MODE_PRIVATE);
			out = new ObjectOutputStream(os);
			out.writeObject(mList);
		} catch (Exception e) {

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}


	
	/**
	 * pop消失
	 */
	@Override
	public void onDismiss() {
		mShowing = false;
		isOpen = false;
		iv_login_choose.setBackgroundResource(R.drawable.login_choose1);
	}

	/**
	 * pop的item点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		et_login_username.setText(mList.get(position));
		mPopup.dismiss();
		iv_login_choose.setBackgroundResource(R.drawable.login_choose1);

	}

	/**
	 * pop初始化 和设置
	 */
	public void toggle() {
		if (isOpen) {
			if (mPopup != null) {
				mPopup.dismiss();
			}else{
				iv_login_choose.setBackgroundResource(R.drawable.login_choose1);
				isOpen = false;
			}
			
		} else {
			// 初始化pop 指定显示pop的位置
			if (mList != null && mList.size() > 0 && !mInitPopup) {
				mInitPopup = true;
				initPopup();
			}
			if (mPopup != null) {
				mPopup.showAsDropDown(et_login_username, 0, 2);	
			}
		
			iv_login_choose.setBackgroundResource(R.drawable.login_choose2);
			isOpen = true;
			
		}
	}

	/**
	 * 初始化pop 和 事件
	 */
	private void initPopup() {
		mAdapter = new ArrayAdapter<String>(this, R.layout.poplist_item,
				R.id.pop_tv, mList);
		mListView = new ListView(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		int height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// int height = et_login_username.getHeight();
		int width = et_login_username.getWidth();
		System.out.println(width);
		mPopup = new PopupWindow(mListView, width, height, true);
		mPopup.setOutsideTouchable(true);
		mPopup.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.pop_bg));

		mPopup.setOnDismissListener(this);

	}

	/**
	 * 多用户登陆 读取文件中的记录
	 */
	private void loginChoose() {
		ObjectInputStream in = null;
		try {
			InputStream is = openFileInput("account.obj");
			in = new ObjectInputStream(is);
			mList = (ArrayList<String>) in.readObject();
			if (mList.size() > 0) {
				et_login_username.setText(mList.get(mList.size() - 1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 设置弹出框的参数
	 * 
	 * @param dialog
	 * @param view
	 * @param height
	 * @param gravity
	 */
	protected void setDialogParams(AlertDialog dialog, View view, int height,
			int gravity) {
		// 得到屏幕宽度
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		int dialogWidth = (int) (metrics.widthPixels * 0.75);// dialog的宽度是屏幕宽度的0.75

		Window dialogWindow = dialog.getWindow();
		android.view.WindowManager.LayoutParams params = dialogWindow
				.getAttributes();
		params.height = height;
		params.width = dialogWidth;
		dialogWindow.setGravity(gravity);
		dialog.addContentView(view, params);
	}

	/**
	 * 密码的查看和隐藏
	 */
	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指点下 去 密 码 可 见 背景变换
			iv_see_password.setBackgroundResource(R.drawable.password_close);
			et_login_password
					.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			break;

		case MotionEvent.ACTION_UP:
			// 手指拿上来 去 密 码不 可 见 背景变换
			iv_see_password.setBackgroundResource(R.drawable.password_open);
			et_login_password.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			break;

		default:
			break;
		}

		return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			if("runningActivity".equals(where) || "trainingActivity".equals(where)){
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				setResult(WTContant.REQUE_CODE_LOGIN,intent);
				finish();
			}else{
				Intent intent = new Intent();
				intent.setClass(mContext, TrainingActivity.class);
				startActivity(intent);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
