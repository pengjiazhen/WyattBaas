package com.xyc.wyatt.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 线程池<br>
 * 为了避免在进行网络请求时候开启过多的线程，在这里使用�?个线程池，默认大小是5�?<br>
 * Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded 
 * queue. At any point, at most nThreads threads will be active processing tasks.
 * If additional tasks are submitted when all threads are active, they will wait in the queue
 * until a thread is available. If any thread terminates due to a failure during execution prior
 * to shutdown, a new one will take its place if needed to execute subsequent tasks. The threads 
 * in the pool will exist until it is explicitly shutdown.
 * 
 * @author 吴传�?
 * @date 2014�?8�?12�? 下午9:36:19
 */
public class WThreadPool {
	protected  static WThreadPool mSKThreadPool;
	protected static int nThreads = 5;// 线程池的个数
	protected static ExecutorService executorService;
	private WThreadPool(){}
	public static WThreadPool getThreadPool(){
		if (mSKThreadPool==null){
			synchronized (WThreadPool.class) {
				if (mSKThreadPool == null){
					/**
					 * 初始化线程池
					 * Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded 
					 * queue. At any point, at most nThreads threads will be active processing tasks.
					 * If additional tasks are submitted when all threads are active, they will wait in the queue
					 * until a thread is available. If any thread terminates due to a failure during execution prior
					 * to shutdown, a new one will take its place if needed to execute subsequent tasks. The threads 
					 * in the pool will exist until it is explicitly shutdown.
					 */
					executorService = Executors.newFixedThreadPool(nThreads);
					mSKThreadPool = new WThreadPool();
				}
			}
		}
		return mSKThreadPool;
	}
	
	/**
	 * 设置线程池大�?
	 * @param size
	 */
	public void setThreadPoolSize(int size){
		nThreads = size;
	}
	
	/**
	 * �?始执行命�?
	 * @param command
	 */
	public void execute(Runnable command){
		executorService.execute(command);
	}
	
	/**
	 * 停止�?有在执行的任�?
	 */
	public void shutdownAllTask(){
		if(!executorService.isShutdown()){
			executorService.shutdownNow();
			executorService = null;
		}
	}
	
}
