package wang.igood.gmvc.action.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wang.igood.gmvc.context.RequestContext;

/************************************************************
 * <a>处理静态文件</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * ***********************************************************
 * 1：方法摘要
 * 		1.1:ResourceActionResult		构造
 * 		1.2:render					渲染业务		
 */
public class ResourceActionResult implements ActionResult {

	private String path;
	/**
	 * <a>1.1:构造函数</a>
	 * @param viewName 		Html文件名
	 * */
	public ResourceActionResult(String path) {
		this.path = path;
	}
	
	@Override
	public void render() {
		RequestContext beat = RequestContext.current();
		HttpServletRequest request = beat.getRequest();
		HttpServletResponse response = beat.getResponse();
		try {
			System.out.println("new path"+request.getRealPath("/a.png"));
			request.getRequestDispatcher("/resources/a.png");
			request.getRequestDispatcher(path).forward(request, response);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
