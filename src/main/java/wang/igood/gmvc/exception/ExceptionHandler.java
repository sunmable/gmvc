package wang.igood.gmvc.exception;

import wang.igood.gmvc.action.result.ActionResult;

/************************************************************
 * <a>异常处理类</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * *********************************************************
 * 1：方法摘要
 * 		1.1：handleException   一场处理
 */
public interface ExceptionHandler {
	
	public <T> ActionResult handleException(Exception exception);

}
