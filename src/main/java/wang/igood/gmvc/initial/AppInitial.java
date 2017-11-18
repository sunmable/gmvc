package wang.igood.gmvc.initial;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.common.AppInit;
import wang.igood.gmvc.util.scan.DefaultClassScanner;

/************************************************************
* <a>APPInit扫码初始化</a>
* @author sunliang
* @since 2017-11-17
* @mail 1130437154@qq.com
* *********************************************************
* 1：方法摘要
* 		1.1：initial   	初始化
* 		1.2：getInits   	全项目扫描AppInit的注解类
*/
public class AppInitial {
	
	private static Set<Class<? extends AppInit>> inits;
	private static final Logger LOG = LoggerFactory.getLogger(AppInitial.class);
	
	/**
	 * <a>1.1:初始化</a>
	 * **/
	public static void initial() {
		inits = getInits();
		for (Class<?> init : inits) {
			try {
				Method initMethod = init.getMethod("init");
				initMethod.invoke(init.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <a>1.2:全项目扫描AppInit的注解类</a>
	 * **/
	@SuppressWarnings("unchecked")
	private static Set<Class<? extends AppInit>> getInits() {
		LOG.info("scan initial start...");
		Set<Class<?>> sets = DefaultClassScanner.getInstance().getClassList("", ".*\\..*");
		Set<Class<? extends AppInit>> initSet = new HashSet<Class<? extends AppInit>>();
		for (Class<?> clazz : sets) {
			if (AppInit.class.isAssignableFrom(clazz) 
					&& !Modifier.isInterface(clazz.getModifiers()) 
					&& !Modifier.isAbstract(clazz.getModifiers()) 
					&& Modifier.isPublic(clazz.getModifiers())) {
			  initSet.add((Class<? extends AppInit>) clazz);
			}
		}
		LOG.info("scan initial complete!");
		return initSet;
	}
}
