/**
 *  Copyright 2014 ken.cai (http://www.shangpuyun.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */
package com.cndemoz.avalidations;

import com.cndemoz.avalidations.utils.ValidationUtil;

import android.content.Context;

/**
 * 
 * 校验执行者
 * 
 * @author ken.cai
 * 
 */
public abstract class ValidationExecutor extends ValidationUtil {

	/**
	 * 
	 * 这里做校验处理
	 * 
	 * @return 校验成功返回true 否则返回false
	 */
	public abstract boolean doValidate(Context context, String text);
	
}
