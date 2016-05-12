package com.xyc.wyatt;

import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.platform.comapi.map.e;
import com.cndemoz.avalidations.EditTextValidator;
import com.cndemoz.avalidations.ValidationModel;
import com.xyc.wyatt.dao.RunRecordDao;
import com.xyc.wyatt.dao.UserDao;
import com.xyc.wyatt.domain.RunRecord;
import com.xyc.wyatt.domain.User;
import com.xyc.wyatt.manager.CallBack;
import com.xyc.wyatt.manager.NetMangaer;
import com.xyc.wyatt.manager.UserManager;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.JsonUtil;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.validations.PasswordValidation;
import com.xyc.wyatt.validations.UserNameValidation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseSherlockActivity implements
		OnClickListener {

	private static final int PASSWORD_ERROR = 0;
	private static final int REGISTER_NONET = 1;
	private static final int REGISTER_FAIL = 2;
	private static final int REGISTER_BEFORE = 3;
	private static final int REGISTER_SUCCESS = 4;
	private static final int REGISTER_FAILS = 5;
	private static final int VALID_NAME = 6;

	private Context mContext;
	private EditText et_register_username;
	private EditText et_register_password;
	private EditText et_register_password2;
	private Button btn_register;
	private boolean isRequesting = false;
	private EditTextValidator editTextValidator;

	private Handler handler = new Handler() {

		public void dispatchMessage(Message msg) {

			switch (msg.what) {
			case PASSWORD_ERROR:
				Toast.makeText(mContext, "两次密码输入不一致", 0).show();
				break;
			case REGISTER_NONET:
				Toast.makeText(mContext, "没有网络", 0).show();
				break;
			case REGISTER_FAIL:
				Toast.makeText(mContext, "连接出问题了，请稍后重试", 0).show();
				break;
			case VALID_NAME:
				Toast.makeText(mContext, "用户名已存在", 0).show();
				break;
			case REGISTER_SUCCESS:
				Toast.makeText(mContext, "注册成功", 0).show();
				break;
			case REGISTER_FAILS:
				Toast.makeText(mContext, "注册失败", 0).show();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(R.style.MySherockTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mContext = this;
		initView();
		init();
		initEvent();
		// 表单验证
		validate();
	}

	@Override
	protected void initView() {
		et_register_username = (EditText) findViewById(R.id.et_register_username);
		et_register_password = (EditText) findViewById(R.id.et_register_password);
		et_register_password2 = (EditText) findViewById(R.id.et_register_password2);
		btn_register = (Button) findViewById(R.id.btn_register);

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		initActionBar("注册", true);

	}

	@Override
	protected void initEvent() {

		btn_register.setOnClickListener(this);

	}

	private void validate() {
		// 表单验证
		editTextValidator = new EditTextValidator(this)
				.setButton(btn_register)
				.add(new ValidationModel(et_register_username,
						new UserNameValidation()))
				.add(new ValidationModel(et_register_password,
						new PasswordValidation()))
				.add(new ValidationModel(et_register_password2,
						new PasswordValidation())).execute();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击注册按钮
		case R.id.btn_register:
			// 注册
			if (editTextValidator.validate()) {
				// Toast.makeText(this, "通过校验", Toast.LENGTH_SHORT).show();

				if (et_register_password.getText().toString()
						.equals(et_register_password2.getText().toString())) {
					User user = new User();
					user.setUserName(et_register_username.getText().toString());
					user.setPassWord(et_register_password.getText().toString());
					user.setPassWord2(et_register_password2.getText()
							.toString());

					if (!NetMangaer.checkNetworkAvailable(mContext)) {
						Message message = new Message();
						message.what = REGISTER_NONET;// 没有网络.
						handler.sendMessage(message);
						return;
					}

					if (!isRequesting) {
						doRegister(user);
						// UserManager.userRegister(user, RegisterActivity.this,
						// null);
					}

				} else {
					Message message = new Message();
					message.what = PASSWORD_ERROR;
					handler.sendMessage(message);
				}
			}
			break;

		default:
			break;
		}

	}

	private void doRegister(final User user) {
		UserManager.validUserName2one(user, mContext, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				makeShortToast(arg1);
			}

			@Override
			public void onSuccess(List<User> arg0) {
				if (!arg0.isEmpty()) {
					makeShortToast("用户名已经存在");
					isRequesting = false;
				} else {
					isRequesting = true;
					// 注册操作
					UserManager.userRegister(user, mContext,
							new SaveListener() {
								@Override
								public void onSuccess() {
									UserManager.getUserByUserName(
											user.getUserName(), mContext,
											new FindListener<User>() {

												@Override
												public void onError(int arg0,
														String arg1) {
												}

												@Override
												public void onSuccess(
														List<User> currentList) {
													if (!currentList.isEmpty()) {
														save2Local(currentList);
														for (int i = 0; i < 30; i++) {
															RunRecord rr = new RunRecord();
															rr.setIsCompleted("n");
															rr.setUserName(user
																	.getUserName());
															rr.save(mContext);
														}
														Intent intent = new Intent();
														intent.setClass(
																mContext,
																TrainingActivity.class);
														startActivity(intent);
														finish();
														makeShortToast("注册成功");
													}

												}

											});

								}

								@Override
								public void onFailure(int arg0, String arg1) {
									//makeShortToast(arg1);
									Log.e("RegisterActivity", arg1);
									makeShortToast("用户名已经存在");
									isRequesting = false;
								}
							});
				}
			}
		});
	}

	private void save2Local(List<User> currentList) {
		// 用户信息
		GloableValue.user.setObjectId(currentList.get(0).getObjectId());
		GloableValue.user.setId(currentList.get(0).getId());
		GloableValue.user.setUserName(currentList.get(0).getUserName());
		GloableValue.user.setAvatar(currentList.get(0).getAvatar());
		GloableValue.user.setWeight(currentList.get(0).getWeight());
		GloableValue.user.setHeight(currentList.get(0).getHeight());
		GloableValue.user.setAge(currentList.get(0).getAge());
		GloableValue.user.setSex(currentList.get(0).getSex());
		GloableValue.user.setStatus(WTContant.LOGIN);
		// 记录用户的注册状态 和用户的信息
		UserDao dao = new UserDao(mContext);
		dao.insert(GloableValue.user);
	}
	/*
	 * private void doRegisters(final User user) {
	 * 
	 * new Thread(new Runnable() {
	 * 
	 * @Override public void run() { // 检测用户名是否 唯一
	 * UserManager.validUserName2one(user, mContext, new CallBack() {
	 * 
	 * @Override public void before() { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void success(String result) { // json解析
	 * Log.i("registerResult", result); Object jsonObject; try { //
	 * Log.i("responseResult", result); jsonObject = JSONObject.parse(result);
	 * JSONObject parseObject = JSONObject .parseObject((String) jsonObject);
	 * String responseCode = parseObject.get("response") .toString(); if
	 * (Integer.parseInt(responseCode) == 0) { // 用户名合法 // 是唯一的
	 * UserManager.userRegister(user, mContext, new CallBack() {
	 * 
	 * @Override public void before() { isRequesting = true; }
	 * 
	 * @Override public void success(String result) { isRequesting = false;
	 * 
	 * // json解析 Log.i("responseResult", result); Object jsonObject;
	 * 
	 * try { jsonObject = JSONObject .parse(result); JSONObject parseObject =
	 * JSONObject .parseObject((String) jsonObject); String responseCode =
	 * parseObject .get("response") .toString(); if (Integer
	 * .parseInt(responseCode) == 0) {// 是0就没有问题 Message message = new
	 * Message(); message.what = REGISTER_SUCCESS;// 注册成功
	 * handler.sendMessage(message);
	 * 
	 * List<User> currentList = JsonUtil .getList( result, "userList",
	 * User.class); // 用户信息 GloableValue.user .setId(currentList .get(0)
	 * .getId()); GloableValue.user .setUserName(currentList .get(0)
	 * .getUserName()); GloableValue.user .setAvatar(currentList .get(0)
	 * .getAvatar()); GloableValue.user .setWeight(currentList .get(0)
	 * .getWeight()); GloableValue.user .setHeight(currentList .get(0)
	 * .getHeight()); GloableValue.user .setAge(currentList .get(0) .getAge());
	 * GloableValue.user .setSex(currentList .get(0) .getSex());
	 * GloableValue.user .setStatus(WTContant.LOGIN); // 记录用户的注册状态 和用户的信息
	 * UserDao dao = new UserDao( mContext); dao.insert(GloableValue.user);
	 * 
	 * List<RunRecord> list = JsonUtil .getList( result, "runRecordList",
	 * RunRecord.class); for (RunRecord record : list) { RunRecordDao dao2 = new
	 * RunRecordDao( mContext); dao2.saveOrUpdata( record, "id=?", new String[]
	 * { String .valueOf(record .getId()) }, "id=?", new String[] { String
	 * .valueOf(record .getId()) }); }
	 * 
	 * Intent intent = new Intent(); intent.setClass( mContext,
	 * TrainingActivity.class); startActivity(intent); finish();
	 * 
	 * } else { Message message = new Message(); message.what =
	 * REGISTER_FAILS;// 存在问题 handler.sendMessage(message); } } catch (Exception
	 * e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * @Override public void fail(String errorMessage) { isRequesting = false;
	 * Message message = new Message(); message.what = REGISTER_FAIL;// 网络异常
	 * handler.sendMessage(message); } }); } else { Message message = new
	 * Message(); message.what = VALID_NAME;// 用户名已存在
	 * handler.sendMessage(message); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * @Override public void fail(String errorMessage) { } }); } }).start();
	 * 
	 * }
	 */
}
