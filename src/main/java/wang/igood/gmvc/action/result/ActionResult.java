package wang.igood.gmvc.action.result;

/************************************************************
 * <a>所有Action的返回结果</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * ***********************************************************
 * 1：方法摘要
 * 		1.1:render			用于生成显示页面
 */
public interface ActionResult {

	/**
	 * <a>1.1:用于生成显示页面</a>
	 * @throws Exception
	 */
	public void render() throws Exception;

}