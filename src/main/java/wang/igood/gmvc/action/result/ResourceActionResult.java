package wang.igood.gmvc.action.result;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private final static Logger logger = LoggerFactory.getLogger(ResourceActionResult.class);
	
	private String path;
	private File resourceFile;
	
	/**
	 * <a>1.1:构造函数</a>
	 * @param viewName 		Html文件名
	 * */
	public ResourceActionResult(String path,File resourceFile) {
		this.path = path;
		this.resourceFile = resourceFile;
	}
	
	@Override
	public void render() throws ServletException, IOException {
		logger.debug("path:"+path);
		RequestContext beat = RequestContext.current();
		beat.getRequest().getRequestDispatcher("/statics/a.png").forward(beat.getRequest(), beat.getResponse());
	}
}
