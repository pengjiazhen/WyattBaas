package com.xyc.wyatt.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xyc.wyatt.annotation.Column;
import com.xyc.wyatt.annotation.ID;
import com.xyc.wyatt.annotation.TableName;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 吴传龙
 * @date 2014年7月31日 下午12:21:40
 */
public abstract class WTDaoSupport<T> implements WTDAO<T> {

	private static final String TAG = "DaoSupport";
	protected WTDBHelper helper;
	protected Context context;
	protected static SQLiteDatabase db;

	public static SQLiteDatabase getDb() {
		return db;
	}

	public WTDaoSupport(Context context) {
		super();
		this.context = context;
		helper = new WTDBHelper(context);
		//WTLogger.e("--------------", context + "");
		db = helper.getWritableDatabase();
	}

	@Override
	public long insert(T t) {
		ContentValues values = new ContentValues();
		fillContentValues(t, values);
		return db.insert(getTableName(), null, values);
	}

	@Override
	public int delete(Serializable id) {
		return db.delete(getTableName(), WTDBHelper.TABLE_ID + "=?",
				new String[] { id.toString() });
	}

	@Override
	@Deprecated
	public int delete(String whereClause, String[] whereArgs, String selection,
			String[] selectionArgs) {
		return db.delete(getTableName(), whereClause, whereArgs);
	}

	@Override
	public int delete(String whereClause, String[] whereArgs) {
		return db.delete(getTableName(), whereClause, whereArgs);
	}

	@Override
	public int updata(T t) {
		ContentValues values = new ContentValues();
		fillContentValues(t, values);
		return db.update(getTableName(), values, WTDBHelper.TABLE_ID + "=?",
				new String[] { getId(t) });
	}

	/**
	 * whereClause 是update的条件 selection 是查询的条件
	 */
	public int saveOrUpdata(T t, String whereClause, String[] whereArgs,
			String selection, String[] selectionArgs) {
		// List<T> list = findByCondition(selection, selectionArgs, null);
		try {
			T obj = findOneByCondition("  where " + selection, selectionArgs);
			ContentValues values = new ContentValues();
			// if (list==null||list.size()==0){
			if (obj == null) {
				fillContentValues(t, values);
				// 执行保存操作

				db.insert(getTableName(), null, values);
				db.endTransaction();
				return 0;
			}
			// 执行更新操作
			fillContentValues(t, values);
			db.update(getTableName(), values, whereClause, whereArgs);
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * 
	 * @param skSql
	 *            从where开始
	 * @param selectionArgs
	 * @return
	 */
	public List<T> findByCondition(String skSql, String[] selectionArgs) {
		List<T> result;
		Cursor cursor = null;
		try {
			skSql = "select * from " + getTableName() + " " + skSql;
			cursor = db.rawQuery(skSql, selectionArgs);
			if (cursor != null) {
				result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					T t = getInstance();
					fillEntity(cursor, t);
					result.add(t);
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return null;
	}

	public T findOneByCondition(String skSql, String[] selectionArgs) {
		T t = null;
		Cursor cursor = null;
		try {
			skSql = "select * from " + getTableName() + " " + skSql;
			cursor = db.rawQuery(skSql, selectionArgs);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					t = getInstance();
					fillEntity(cursor, t);
					return t;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return null;
	}

	public List<T> findByCondition(String selection, String[] selectionArgs,
			String orderBy) {
		return findByCondition(null, selection, selectionArgs, null, null,
				orderBy);
	}

	public List<T> findByCondition(String[] columns, String selection,
			String[] selectionArgs, String orderBy) {
		return findByCondition(columns, selection, selectionArgs, null, null,
				orderBy);
	}

	public List<T> findByCondition(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {

		List<T> result;
		Cursor cursor = null;
		try {
			cursor = db.query(getTableName(), columns, selection,
					selectionArgs, groupBy, having, orderBy);
			if (cursor != null) {
				result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					T t = getInstance();
					fillEntity(cursor, t);
					result.add(t);
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return null;

	}

	public List<T> findByCondition() {

		List<T> result;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(null, null);
			if (cursor != null) {
				result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					T t = getInstance();
					fillEntity(cursor, t);
					result.add(t);
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return null;

	}

	@Override
	public List<T> findAll() {
		List<T> result;
		Cursor cursor = null;
		try {
			cursor = db.query(getTableName(), null, null, null, null, null,
					null);
			if (cursor != null) {
				result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					T t = getInstance();
					fillEntity(cursor, t);
					result.add(t);
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return null;
	}

	public String getTableName() {
		T t = getInstance();
		TableName tableName = t.getClass().getAnnotation(TableName.class);
		if (tableName != null) {
			return tableName.value();
		}
		return "";
	}

	private void fillContentValues(T t, ContentValues values) {
		Field[] fields = t.getClass().getDeclaredFields();
		Field[] fields2 = t.getClass().getSuperclass().getDeclaredFields();
		for (Field item : fields2) {
			item.setAccessible(true);
			if (item.getName().equals("objectId")) {
				try {
					String value = item.get(t) == null ? "" : item.get(t)
							.toString();
					values.put("objectId", value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (Field item : fields) {
			item.setAccessible(true);
			Column column = item.getAnnotation(Column.class);
			if (column != null) {
				String key = column.value();
				try {
					ID id = item.getAnnotation(ID.class);
					if (id != null) {
						if (id.autoincrement()) {
						} else {
							String value = item.get(t) == null ? "" : item.get(
									t).toString();
							values.put(key, value);
						}
					} else {
						String value = item.get(t) == null ? "" : item.get(t)
								.toString();
						values.put(key, value);
					}

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void fillEntity(Cursor cursor, T t) {

		Field[] fields = t.getClass().getDeclaredFields();
		Field[] fields2 = t.getClass().getSuperclass().getDeclaredFields();
		for (Field item : fields2) {
			item.setAccessible(true);
			if (item.getName().equals("objectId")) {
				int columnIndex = cursor.getColumnIndex("objectId");
				// 主键+自增 int long
				String value = cursor.getString(columnIndex);
				try {
					item.set(t, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (Field item : fields) {
			item.setAccessible(true);

			Column column = item.getAnnotation(Column.class);
			if (column != null) {
				int columnIndex = cursor.getColumnIndex(column.value());
				// 主键+自增 int long
				String value = cursor.getString(columnIndex);

				try {

					if (item.getType() == int.class) {
						item.set(t, Integer.parseInt(value));
					} else if (item.getType() == long.class) {
						item.set(t, Long.parseLong(value));
					} else if (item.getType() == double.class) {
						item.set(t, Double.parseDouble(value));
					} else {
						item.set(t, value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}
		}

	}

	private String getId(T t) {
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field item : fields) {
			item.setAccessible(true);
			ID id = item.getAnnotation(ID.class);
			if (id != null) {
				try {
					return item.get(t).toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	private T getInstance() {

		Type superclass = getClass().getGenericSuperclass();

		if (superclass instanceof ParameterizedType) {

			Type[] arguments = ((ParameterizedType) superclass)
					.getActualTypeArguments();
			Class target = (Class) arguments[0];
			try {

				return (T) target.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
