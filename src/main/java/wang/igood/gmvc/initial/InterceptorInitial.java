package wang.igood.gmvc.initial;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.annotation.Interceptor.Scope;
import wang.igood.gmvc.common.AppInit;
import wang.igood.gmvc.common.Interceptor;
import wang.igood.gmvc.util.scan.DefaultClassScanner;

/************************************************************
* <a>全局拦截器初始化</a>
* @author sunliang
* @since 2017-11-17
* @mail 1130437154@qq.com
* *********************************************************
* 1：方法摘要
* 		1.1：initial   			初始化
* 		1.2：getIntercepter   	全项目扫描Interceptor的注解类
*/
public class InterceptorInitial implements AppInit{

	private static final Logger LOG = LoggerFactory.getLogger(InterceptorInitial.class);
	private static List<Interceptor> globalInterceptors = new ArrayList<>();
	
	/**
	 * <a>1.1:初始化</a>
	 * **/
	@Override
	public void init() {
		LOG.info("initial interceptor start...");
		Set<Class<? extends Interceptor>> interceptorsClasses = getIntercepter();
		List<Interceptor> interceptors = new ArrayList<Interceptor>();
		
		for (Class<? extends Interceptor> clazz : interceptorsClasses) {
			Interceptor interceptor = null;
			try {
				interceptor = clazz.newInstance();
				if (Scope.GLOBAL == interceptor.scope()) {
					interceptors.add(interceptor);
				}
			} catch (Exception e) {
				LOG.error("Build global interceptor failed, Interceptor: " + clazz.getName());
				e.printStackTrace();
			}
		}

		Collections.sort(interceptors, Interceptor.INTERCEPTOR_SORTER);
		for (Interceptor interceptor : interceptors) {
			LOG.info("Load Global Interceptor : " + interceptor.getClass().getName());
		}
		globalInterceptors = interceptors;
		LOG.info("initial interceptor end...");
	}

	/**
	 * <a>1.2:全项目扫描Interceptor的拦截器</a>
	 * **/
	@SuppressWarnings("unchecked")
	private static Set<Class<? extends Interceptor>> getIntercepter() {
		LOG.info("scan initial start...");
		Set<Class<?>> sets = DefaultClassScanner.getInstance().getClassList("", ".*\\..*");
		Set<Class<? extends Interceptor>> initSet = new HashSet<Class<? extends Interceptor>>();
		for (Class<?> clazz : sets) {
			if (Interceptor.class.isAssignableFrom(clazz) 
					&& !Modifier.isInterface(clazz.getModifiers()) 
					&& !Modifier.isAbstract(clazz.getModifiers()) 
					&& Modifier.isPublic(clazz.getModifiers())) {
			  initSet.add((Class<? extends Interceptor>) clazz);
			}
		}
		LOG.info("scan initial complete!");
		return initSet;
	}
	
	public static List<Interceptor> getGlobalInterceptors() {
		return globalInterceptors;
	}
}
