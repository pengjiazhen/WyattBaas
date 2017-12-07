package com.xyc.wyatt;


public class NickNameEditActivity extends BaseSherlockActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}
/*
	private TextView tv_complete;
	private EditText et_edit_username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nick_name_edit);
		initView();
		init();
		initEvent();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		View actionbar_complete = LayoutInflater.from(mContext).inflate(
				R.layout.actionbar_complete, null);

		tv_complete = (TextView) actionbar_complete
				.findViewById(R.id.tv_complete);
		
		et_edit_username = (EditText) findViewById(R.id.et_edit_username);
		
	}

	@Override
	protected void init() {
		initActionBar("修改昵称", true);
	}

	@Override
	protected void initEvent() {
		tv_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				makeShortToast("完成");
			}
		});
	}
	@Override
	protected void onResume() {
		et_edit_username.setText(GloableValue.user.getUserName());
		super.onResume();
	}
	@Override
	protected int getOptionsMenu() {

		return R.menu.menu_compelete;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_complete:			
			String new_username = et_edit_username.getText().toString();
			//设置全局用户变量
			if(!new_username.equals(GloableValue.user.getUserName())&&!GloableValue.user.getUserName().isEmpty()){
				
				//用户名改变
				GloableValue.user.setUserName(new_username);
				//更新数据库 表
				UserDao dao = new UserDao(mContext);
				dao.saveOrUpdata(GloableValue.user, "id=?", new String[]{GloableValue.user.getId()+""}, "id=?", new String[]{GloableValue.user.getId()+""});
				//信息改变  标记
				GloableValue.changeUserInfo=WTContant.CHANGE;
				//访问网络服务器
				String updateType = "userName";
				UserManager.updateUser(new_username,GloableValue.user.getId(), updateType, mContext, new CallBack(
						) {
					
					@Override
					public void success(final String result) {
						
						new Thread(){
							public void run() {
								
								Log.i("responseResult", result);
								Object jsonObject;
								try {
									jsonObject = JSONObject.parse(result);
									JSONObject parseObject = JSONObject
											.parseObject((String) jsonObject);
									String responseCode = parseObject.get(
											"response").toString();
									if (Integer.parseInt(responseCode) == 0) {
										runOnUiThread(new  Runnable() {
											public void run() {
												
												Toast.makeText(mContext, "修改成功..........", 0).show();
											}
										});
										
									}else{
										runOnUiThread(new  Runnable() {
											public void run() {
												
												Toast.makeText(mContext, "修改失败..........", 0).show();
											}
										});
									}
									
								} catch (Exception e) {
									e.printStackTrace();
								}	
									
								
							};
							
						}.start();
						
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
				//跳转界面
				Intent intent = new Intent();
				intent.setClass(mContext, UserInfoActivity.class);
				startActivity(intent);
				makeShortToast("昵称修改成功");
			}else{
				//用户名没有改变 返回个人信息界面
				Intent intent = new Intent();
				intent.setClass(mContext, UserInfoActivity.class);
				startActivity(intent);	
				makeShortToast("昵称没有修改");
			}
			
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
*/
}
