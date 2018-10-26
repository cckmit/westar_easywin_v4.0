package com.westar.base.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutor {
	//私有的默认构造
	private ThreadPoolExecutor(){}
	//已经自行实例化 
	private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();;
	//静态工厂方法 
	public static ExecutorService getInstance() { 
        return singleThreadExecutor;  
    }
	
}
