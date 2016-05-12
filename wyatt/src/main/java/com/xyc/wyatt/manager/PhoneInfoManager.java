package com.xyc.wyatt.manager;

import java.io.PrintStream;
import java.lang.reflect.Field;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class PhoneInfoManager {  
  
    private TelephonyManager telephonyManager;  
   
    private Context context;  
    
    public PhoneInfoManager(Context context) {  
        this.context=context;  
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
    }  
  
    
    /** 
     * 获取电话号码 
     */  
    public String getNativePhoneNumber() {  
        String NativePhoneNumber=null;  
        NativePhoneNumber=telephonyManager.getLine1Number();  
        return NativePhoneNumber;  
    }  
  
    
    /** 
     * 获取手机服务商信息 
     */  
    public String getProvidersName() {  
        String ProvidersName = "N/A";  
        try{  
			/**
			 * 国际移动用户识别码
			 */
        String IMSI = telephonyManager.getSubscriberId();  
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。  
        System.out.println(IMSI);  
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {  
            ProvidersName = "中国移动";  
        } else if (IMSI.startsWith("46001")) {  
            ProvidersName = "中国联通";  
        } else if (IMSI.startsWith("46003")) {  
            ProvidersName = "中国电信";  
        }  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        return ProvidersName;  
    }  

	/**
	 * IMEI（International Mobile Equipment
	 * Identity）是移动设备国际身份码的缩写，移动设备国际辨识码，是由15位数字组成的
	 * "电子串号"，它与每台手机一一对应，而且该码是全世界唯一的。每一部手机在组装完成后都将被赋予一个全球唯一的一组号码
	 * ，这个号码从生产到交付使用都将被制造生产的厂商所记录。手机用户可以在手机中查到自己手机的IMEI码。
	 */
    public String getIMEI(){
    	return telephonyManager.getDeviceId();
    }
    
    /**
     * 电话号码
     */
    public String getLine1Number(){
    	return telephonyManager.getLine1Number();
    }
    
    /**
     * sim串号
     */
    public String getSimSerialNumber(){
    	return telephonyManager.getSimSerialNumber();
    }
    
    
    public interface DeviceInfoKey{
    	String DEVICE = "DEVICE";//手机型号 GT-B9062
    	String BRAND = "BRAND";// 手机品牌 samsung
    }
    
    /**
     * 获得设备的信息<br/>
     * eg : 获得设备型号：那么key就应该是DeviceInfoKey.DEVICE 。 比如说Samsung GT-B9062 <br/>
     * @param key  获得的设备信息
     * @see com.xy.sk.manager.PhoneInfoManager.DeviceInfoKey
     * @return
     */
    public String getDeviceInfo(String key){
    	String value = null;
    	Field[] fields = Build.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String name = fields[i].getName();
			if (name.equals(key)){
				try {
					value = fields[i].get(null).toString();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
    	return value;
    }
    
    
    public String getPhoneInfo(){  
        StringBuilder sb = new StringBuilder();  
		sb.append("\nDeviceId(IMEI) = " + telephonyManager.getDeviceId());
		sb.append("\nDeviceSoftwareVersion = " + telephonyManager.getDeviceSoftwareVersion());
		sb.append("\nLine1Number = " + telephonyManager.getLine1Number());
		sb.append("\nNetworkCountryIso = " + telephonyManager.getNetworkCountryIso());
		sb.append("\nNetworkOperator = " + telephonyManager.getNetworkOperator());
		sb.append("\nNetworkOperatorName = " + telephonyManager.getNetworkOperatorName());
		sb.append("\nNetworkType = " + telephonyManager.getNetworkType());
		sb.append("\nPhoneType = " + telephonyManager.getPhoneType());
		sb.append("\nSimCountryIso = " + telephonyManager.getSimCountryIso());
		sb.append("\nSimOperator = " + telephonyManager.getSimOperator());
		sb.append("\nSimOperatorName = " + telephonyManager.getSimOperatorName());
		sb.append("\nSimSerialNumber = " + telephonyManager.getSimSerialNumber());
		sb.append("\nSimState = " + telephonyManager.getSimState());
		sb.append("\nSubscriberId(IMSI) = " + telephonyManager.getSubscriberId());
		sb.append("\nVoiceMailNumber = " + telephonyManager.getVoiceMailNumber());
		return sb.toString();
    }  
}  