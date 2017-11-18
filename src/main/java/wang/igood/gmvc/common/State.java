package wang.igood.gmvc.common;

import java.util.HashSet;
import java.util.Set;

/************************************************************
 * <a>状态类</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * *********************************************************
 * 1：内部类摘要
 * 		1.1：HttpMethod   	请求方式
 */
public interface State {
	
	/**
	 * <a>请求类型</a>
	 * */
	public enum HttpMethod {

	    GET,
	    POST,
	    HEAD;

	    public static String parse(String method) {
	      if (method == null || method.isEmpty())
	        return null;

	      return method.toUpperCase();
	    }

	    public static Set<HttpMethod> suportHttpMethods() {
	      Set<HttpMethod> methods = new HashSet<State.HttpMethod>();
	      methods.add(HEAD);
	      return methods;
	    }
	}
}
