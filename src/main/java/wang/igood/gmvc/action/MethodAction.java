package wang.igood.gmvc.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.action.result.ActionResult;
import wang.igood.gmvc.common.Interceptor;
import wang.igood.gmvc.common.State.HttpMethod;
import wang.igood.gmvc.context.RequestContext;
import wang.igood.gmvc.util.AnnotationUtils;

/************************************************************
 * <a>请求方法实体</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * ***********************************************************
 * 1：方法摘要
 * 		1.1:MethodAction				构造函数
 * 		1.2:initInterceptor			初始化拦截器
 * 		1.3:	invoke					具体实现
 */
public class MethodAction implements Action {
	
	private final static Logger logger = LoggerFactory.getLogger(MethodAction.class);
	
	protected Object controller;
	protected Method method;
	protected String pathPattern;
	protected Set<HttpMethod> supportMethods = HttpMethod.suportHttpMethods();
	protected List<Interceptor> interceptors = new ArrayList<>();
	
	/**
	 * <a>1.1:构造函数</a>
	 * @param controller 控制器
	 * @param method		方法
	 * @param pathPatter 请求路径
	 * */
	public MethodAction(Object controller, Method method, String pathPattern) {
		this.controller = controller;
		this.method = method;
		this.pathPattern = pathPattern;
		interceptors = initInterceptor(controller.getClass(),method);
		Collections.sort(interceptors, Interceptor.INTERCEPTOR_SORTER);
	}
	
	/**
	 * <a>1.2:初始化拦截器</a>
	 * @param controller 控制器
	 * @param method		方法
	 * */
	private List<Interceptor> initInterceptor(Class<?> controllerClass,Method method) {
		List<Interceptor> interceptors = new ArrayList<>();
		Set<Annotation> annotations = new HashSet<Annotation>();
		annotations.addAll(Arrays.asList(controllerClass.getAnnotations()));
		annotations.addAll(Arrays.asList(method.getAnnotations()));
		for (Annotation annotation : annotations) {
			try {
				wang.igood.gmvc.annotation.Interceptor interAnnotation = (wang.igood.gmvc.annotation.Interceptor) ((annotation.annotationType() == wang.igood.gmvc.annotation.Interceptor.class) ? annotation : null);
				if (interAnnotation == null) {
					interAnnotation = AnnotationUtils.findAnnotation(annotation.getClass(), wang.igood.gmvc.annotation.Interceptor.class);
				}
				if(interAnnotation == null) {
					continue;
				}
				Interceptor interceptor = interAnnotation.value().newInstance();
				interceptors.add(interceptor);
			}catch(Exception e) {
				logger.error(e.getMessage());
			}
			
		}
		return interceptors;
	}
	
	/**
	 * <a>1.3:具体实现</a>
	 * */
	@Override
	public ActionResult invoke() throws Exception {
		Object result = getMethod().invoke(getController(), null);
		return (ActionResult) result;
	}

	@Override
	public boolean matchHttpMethod() {
		String requestMethod = RequestContext.current().getRequest().getMethod();
		String currentMethod = HttpMethod.parse(requestMethod);
		Boolean result = false;
		try {
			HttpMethod httpMethod = HttpMethod.valueOf(currentMethod);
			result = supportMethods.contains(httpMethod);
		} catch (Exception e) {
			result = false;
		}

		return result;
	}

	public Object getController() {
		return controller;
	}

	public void setController(Object controller) {
		this.controller = controller;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getPathPattern() {
		return pathPattern;
	}

	public void setPathPattern(String pathPattern) {
		this.pathPattern = pathPattern;
	}

	@Override
	public String path() {
		return pathPattern;
	}
	
	public Set<HttpMethod> getSupportMethods() {
		return supportMethods;
	}

	public void setSupportMethods(Set<HttpMethod> supportMethods) {
		this.supportMethods = supportMethods;
	}
	
	public List<Interceptor> getInterceptors() {
		return interceptors;
	}

	@Override
	public String toString() {
		return "MethodAction [controller=" + controller + ", method=" + method + ", pathPattern=" + pathPattern + "]";
	}
}
