package com.xyc.wyatt.domain;


/**
 * 时间格式。均采用24小时制<br/>
 * SKTimePattern.YMD  ：  yyyy-MM-dd <br/>
 * SKTimePattern.YMDHM  ：   yyyy-MM-dd HH:mm<br/>
 * SKTimePattern.YMDHMS  ：  yyyy-MM-dd HH:mm:ss<br/>
 * SKTimePattern.HM  ：  HH:mm<br/>
 * SKTimePattern.HMS  ：  HH:mm:ss<br/>
 * @author 吴传龙<br/>
 * @date 2014年7月30日 下午9:41:28
 */
public enum WTimePattern {
	YMD() {
		@Override
		public String pattern() {
			String d = "2014-07-08";
			return "yyyy-MM-dd";
		}
	},YMD2() {
		@Override
		public String pattern() {
			String d = "2014-07-08";
			return "yyyyMMdd";
		}
	},	
	YMDHM() {
		@Override
		public String pattern() {
			return "yyyy-MM-dd HH:mm";
		}
	},  
	YMDHMS() {
		@Override
		public String pattern() {
			return "yyyy-MM-dd HH:mm:ss";
		}
	}, 
	HM() {
		@Override
		public String pattern() {
			return "HH:mm";
		}
	},		
	HMS() {
		@Override
		public String pattern() {
			return "HH:mm:ss";
		}
	},MDHMS(){

		@Override
		public String pattern() {
			// TODO Auto-generated method stub
			return "MM-dd HH:mm:ss";
		}
		
	},MDHM(){

		@Override
		public String pattern() {
			// TODO Auto-generated method stub
			return "MM-dd HH:mm";
		}
		
	};	
	public abstract String pattern();
}
