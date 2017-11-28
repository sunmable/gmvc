package wang.igood.gmvc.initial;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.action.AntPathMatcher;
import wang.igood.gmvc.action.MethodAction;
import wang.igood.gmvc.annotation.AutoWired;
import wang.igood.gmvc.annotation.GET;
import wang.igood.gmvc.annotation.POST;
import wang.igood.gmvc.annotation.Path;
import wang.igood.gmvc.common.AppInit;
import wang.igood.gmvc.common.Controller;
import wang.igood.gmvc.common.State.HttpMethod;
import wang.igood.gmvc.util.scan.DefaultClassScanner;
import wang.igood.gmvc.util.scan.DefaultMethodScanner;
import wang.igood.gmvc.util.AnnotationUtils;
import wang.igood.gmvc.util.ReflectionUtils;

/************************************************************
* <a>请求初始化</a>
* @author sunliang
* @since 2017-11-17
* @mail 1130437154@qq.com
* *********************************************************
* 1：方法摘要
* 		1.1：init   				初始化
* 		1.2：getControllers   	全项目扫描Controller的注解类
* 		1.3：getMethodActions	获取Controller内的MethodAction
* 		1.4：initAutoWired		自动装入
* 		1.5：getActionmap		获取ActionMap
*/
public class RequestInitial implements AppInit {

	private static final Logger LOG = LoggerFactory.getLogger(RequestInitial.class);
	private static final Map<String, Object> controllers = new HashMap<String, Object>();
	private static final AntPathMatcher pathMatcher = new AntPathMatcher();
	private static final Map<String,MethodAction> actionMap = new HashMap<String, MethodAction>();
	private static Map<String,Object> autoWiredMap = new HashMap<String,Object>();

	/**
	 * <a>1.1:初始化</a>
	 * **/
	@Override
	public void init() {
		LOG.info("initial action start...");
		//初始化控制器
		Set<Class<? extends Controller>> controllers = getControllers();
		LOG.info("controllerClasses size:" + controllers.size());
		
		//初始化方法
		for(Class<? extends Controller> controller : controllers) {
			List<MethodAction> actions = getMethodActions(controller);
			for(MethodAction action : actions) {
				actionMap.put(action.getPathPattern(), action);
			}
		}
		LOG.info("actions size:" + actionMap.size());
		LOG.info("initial action complete");
	}

	/***
	 * <a>1.2：获取项目中所有继承Controller的class</a>
	 * @return List<Class<? extends Controller>>
	 * */
	@SuppressWarnings("unchecked")
	private Set<Class<? extends Controller>> getControllers() {
		Set<Class<?>> sets = DefaultClassScanner.getInstance().getClassList("", ".*\\..*");
		Set<Class<? extends Controller>> initSet = new HashSet<Class<? extends Controller>>();
		for (Class<?> clazz : sets) {
			if (Controller.class.isAssignableFrom(clazz) && !Modifier.isInterface(clazz.getModifiers())&& !Modifier.isAbstract(clazz.getModifiers()) && Modifier.isPublic(clazz.getModifiers())) {
				initSet.add((Class<? extends Controller>) clazz);
			}else if(clazz instanceof Object){
				/*初始化注入类文件********************************************************/
				Class<?>[] interfaces = clazz.getInterfaces();
				try {
					Object object = clazz.newInstance();
					for(Class<?> interface_ : interfaces) {
						autoWiredMap.put(interface_.getName(), object);
					}
					autoWiredMap.put(clazz.getName(), object);
				}catch(Exception e) {
					continue;
				}
				/*初始化注入类文件********************************************************/
			}
		}
		return initSet;
	}

	/***
	 * <a>1.3：获取Controller内的MethodAction</a>
	 * @param Class<? extends Controler>
	 * @return List<Class<? extends MethodAction>>
	 * */
	private List<MethodAction> getMethodActions(Class<? extends Controller> clazz) {
		List<MethodAction> actions = new ArrayList<MethodAction>();
		Path onControllerPath = AnnotationUtils.findAnnotation(clazz, Path.class);
		if(onControllerPath == null)
			return actions;
		String onControllerUrl = (onControllerPath == null) ? "/" : onControllerPath.value();
		List<Method> mList = DefaultMethodScanner.getInstance().getMethodListByAnnotation(clazz, Path.class);// 获得Path注解的方法
		LOG.info("controllerPATH:"+onControllerUrl);
		for (Method method : mList) {
			try {
				String key = clazz.getPackage() + clazz.getName();
				if(!controllers.containsKey(key)) {
					Object object = initAutoWired(clazz);
					if(object != null)
						controllers.put(key, object);
				}
				Path onMethodPath = AnnotationUtils.findAnnotation(method, Path.class);
				GET onMethodGet = AnnotationUtils.findAnnotation(method, GET.class);
				POST onMethodPost = AnnotationUtils.findAnnotation(method, POST.class);
				
				boolean isGet = ( onMethodGet == null && onMethodPost == null )?true:(onMethodGet != null?true:false);
				boolean isPost =  ( onMethodGet == null && onMethodPost == null )?true:(onMethodPost != null?true:false);
				
				String onMethodUrl = (onControllerPath == null) ? "/" : onMethodPath.value();
				String url = pathMatcher.combine(onControllerUrl, onMethodUrl);
				MethodAction methodAction = new MethodAction(controllers.get(key),method,url);
				if(isGet)
					methodAction.getSupportMethods().add(HttpMethod.GET);
				if(isPost)
					methodAction.getSupportMethods().add(HttpMethod.POST);
				LOG.info("methodUri >>> " + url + " >> methodName" + method.getName() + " get : " + isGet  + ", post : " + isPost);
				actions.add(methodAction);
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return actions;
	}
	
	/***
	 * <a>1.4：自动装入</a>
	 * @return Map<String, MethodAction>
	 * */
	public static Object initAutoWired(Class<?> clazz){
		Object controller = null;
		try {
			controller = clazz.newInstance();
			List<Field> fields = ReflectionUtils.findFields(clazz, AutoWired.class);
			for(Field field : fields) {
				try {
					String key = field.getType().getName();
					if(!autoWiredMap.containsKey(key)) {
						System.out.println(field.getType().getName());
						autoWiredMap.put(key, field.getType().newInstance());
					}
					field.setAccessible(true);
					ReflectionUtils.setField(field, controller, autoWiredMap.get(key));
				}catch(Exception e) {
					LOG.error(controller.getClass().getName()+">>"+field.getClass().getName()+">>"+"没有非空构造函数或是基本数据类型无法实现注入");
					e.printStackTrace();
					continue;
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return controller;
	}
	
	/***
	 * <a>1.5：获取ActionMap</a>
	 * @return Map<String, MethodAction>
	 * */
	public static Map<String, MethodAction> getActionmap() {
		return actionMap;
	}
}
