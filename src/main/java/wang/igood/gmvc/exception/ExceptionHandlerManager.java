package wang.igood.gmvc.exception;

import java.util.ArrayList;
import java.util.List;

import wang.igood.gmvc.action.result.ActionResult;

/************************************************************
 * <a>异常分发类</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * *********************************************************
 * 1：方法摘要
 * 		1.1：register   注册异常管理器
 */

public class ExceptionHandlerManager {

	private final static List<ExceptionHandler> handlers = new ArrayList<>();
	
	/**
	 * <a>1.1:注册异常管理器</a>
	 * @param handler 异常处理器
	 * */
	public static void register(ExceptionHandler handler) {
		handlers.add(handler);
	}

	/**
	 * <a>1.2:异常处理</a>
	 * @param exception 异常信息
	 * */
	public static ActionResult handleException(Exception exception) {
		ActionResult result = null;
		for(ExceptionHandler hander : handlers) {
			result = hander.handleException(exception);
			if(result != null) {
				break;
			}
		}
		return result;
	}
	
}
