package wang.igood.gmvc.action;

import wang.igood.gmvc.action.result.ActionResult;

/************************************************************
 * <a>请求动作</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * ***********************************************************
 * 1：方法摘要
 * 		1.1:path						URI
 * 		1.2:invoke					实现
 * 		1.3:	matchHttpMethod			请求方式	
 */
public interface Action {

	/**
	 * <a>1.1:URI</a>
	 * */
	public String path();

	/**
	 * <a>1.2:请求具体业务实现</a>
	 * */
	public ActionResult invoke() throws Exception;

	/**
	 * <a>1.3:请求方式</a>
	 * */
	public boolean matchHttpMethod();
}