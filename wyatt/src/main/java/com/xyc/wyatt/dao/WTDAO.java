package com.xyc.wyatt.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author 吴传龙
 * @date 2014年7月31日 下午12:21:45
 */
public interface WTDAO<T> {
	
	/**
	 * 添加
	 * 
	 * @return
	 */
	long insert(T t);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	int delete(Serializable id);

	/**
	 * 更新
	 * 
	 * @return
	 */
	int updata(T t);

	/**
	 * 查询
	 * 
	 * @return
	 */
	List<T> findAll();

	/**
	 * 按照条件查询
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @return
	 */
	List<T> findByCondition(String selection, String[] selectionArgs, String orderBy);



	int saveOrUpdata(T t, String whereClause, String[] whereArgs,
			String selection, String[] selectionArgs);

	@Deprecated
	int delete(String whereClause, String[] whereArgs, String selection,
			String[] selectionArgs);

	int delete(String whereClause, String[] whereArgs);
}
