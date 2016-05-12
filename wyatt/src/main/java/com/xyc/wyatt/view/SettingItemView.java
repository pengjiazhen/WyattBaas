package com.xyc.wyatt.view;


import com.xyc.wyatt.R;
import com.xyc.wyatt.view.SlideButton.OnSlideButtonClickListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 我们自定义的组合控件，它里面有两个TextView ，还有一个CheckBox,还有一个View
 * 
 * @author Administrator
 * 
 */
public class SettingItemView extends RelativeLayout {

	private SlideButton sb_status;
	private TextView tv_desc;
	private TextView tv_title;
	private String desc_on;
	private String desc_off;
	private String title;

	/**
	 * 初始化布局文件
	 * 
	 * @param context
	 */
	private void iniView(Context context) {

		// 把一个布局文件---》View 并且加载在SettingItemView
		View.inflate(context, R.layout.setting_item_view, this);
		sb_status = (SlideButton) this.findViewById(R.id.sb_status);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
		tv_title = (TextView) this.findViewById(R.id.tv_title);

	}
	/**
	 * 获取属性值
	 * @param attrs
	 */
	private void initAttrs(AttributeSet attrs){
		title = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.xyc.wyatt",
				"setting_title");
		desc_on = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.xyc.wyatt",
				"setting_desc_on");
		desc_off = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.xyc.wyatt",
				"setting_desc_off");
		
	}

	/**
	 * 初始化标题
	 */
	private void init(){
		tv_title.setText(title);
		
		
	}
	
	public void setOnSlideButtonClickListener(OnSlideButtonClickListener on){
		sb_status.setOnSlideButtonClickListener(on);
	}
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}

	/**
	 * 创建view的时候默认调用的构造方法是两个参数的
	 * @param context
	 * @param attrs
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		initAttrs(attrs);
		init();
	}

	public SettingItemView(Context context) {
		super(context);
		iniView(context);
	}

	/**
	 * 校验组合控件是否选中
	 */

	public boolean isOpen() {
		return sb_status.isOpen();
	}

	/**
	 * 设置组合控件的状态
	 */

	public void setOpen(boolean checked) {
		if(checked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
		sb_status.setOpen(checked);
	}

	public void changeDesc(boolean checked){
		if(checked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
	}
	/**
	 * 设置 组合控件的描述信息
	 */

	public void setDesc(String text) {
		tv_desc.setText(text);
	}

}
