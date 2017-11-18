package wang.igood.gmvc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.context.RequestContext;

/************************************************************
 * <a>控制器</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * *********************************************************
 * 1：方法摘要
 * 		1.1：currentRequest   当前请求类
 * **/
public abstract class Controller {

	/**
	 * 日志系统
	 */
	protected Logger _WFLOG = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * <a>1.1:当前请求类</a>
	 * */
	public RequestContext currentRequest() {
		return RequestContext.current();
	}
}
