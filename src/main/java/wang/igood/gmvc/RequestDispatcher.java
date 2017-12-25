package wang.igood.gmvc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.action.Action;
import wang.igood.gmvc.action.MethodAction;
import wang.igood.gmvc.action.ResourceAction;
import wang.igood.gmvc.action.result.ActionResult;
import wang.igood.gmvc.common.Interceptor;
import wang.igood.gmvc.context.RequestContext;
import wang.igood.gmvc.initial.RequestInitial;

/************************************************************
* <a>请求分发器</a>
* @author sunliang
* @since 2017-11-17
* @mail 1130437154@qq.com
* *********************************************************
* 1：方法摘要
* 		1.1：service					处理请求
* 		1.2：beforeService   		请求之前
* 		1.3：afterService   			请求之后
* 		1.4：completeService			请求完成
* 		1.5：findAction				获取Action
*/
public class RequestDispatcher {

	private Logger logger = LoggerFactory.getLogger(RequestDispatcher.class);

	/**
	 * <a>1.1:处理请求</a>
	 * @param context
	 * */
	public ActionResult service(RequestContext context) throws Exception {
		Action action = (Action) findAction(context);
		action = action == null ? findResourceAction(context):action;
		return action == null ? null:action.invoke();
	}
	
	/**
	 * <a>1.2:请求之前</a>
	 * @param context
	 * */
	public ActionResult beforeService(RequestContext context) {
		ActionResult result = null;
		MethodAction action = (MethodAction) findAction(context);
		if(action == null)
			return result;
		List<Interceptor> interceptors = action.getInterceptors();
		for(Interceptor interceptor : interceptors) {
			result = interceptor.before();
			if(null != result) {
				break;
			}
		}
		return result;
	}
	
	/**
	 * <a>1.3:请求之后</a>
	 * @param context
	 * */
	public ActionResult afterService(RequestContext context) {
		ActionResult result = null;
		MethodAction action = (MethodAction) findAction(context);
		if(action == null)
			return result;
		List<Interceptor> interceptors = action.getInterceptors();
		for(Interceptor interceptor : interceptors) {
			result = interceptor.after();
			if(null != result) {
				break;
			}
		}
		return result;
	}
	
	/**
	 * <a>1.4:请求完成</a>
	 * @param context
	 * */
	public ActionResult completeService(RequestContext context) {
		ActionResult result = null;
		MethodAction action = (MethodAction) findAction(context);
		if(action == null)
			return result;
		List<Interceptor> interceptors = action.getInterceptors();
		for(Interceptor interceptor : interceptors) {
			result = interceptor.complete();
			if(null != result) {
				break;
			}
		}
		return result;
	}

	/**
	 * <a>1.5:获取Action</a>
	 * @param context
	 * */
	private MethodAction findAction(RequestContext context) {
		String uri = context.getRequest().getRequestURI();
		logger.debug("uri:{}",uri);
		MethodAction methodAction = null; 
		MethodAction methodAction_ = RequestInitial.getActionmap().get(uri);
		if(methodAction_ != null  && methodAction_.matchHttpMethod()) {
			methodAction = methodAction_;
		}
		return methodAction;
	}
	
	/**
	 * <a>1.6:获取Action</a>
	 * @param context
	 * */
	private ResourceAction findResourceAction(RequestContext context) {
		String uri = context.getRequest().getRequestURI();
		logger.debug("uri:{}",uri);
		return new ResourceAction(uri, null);
	}
}
