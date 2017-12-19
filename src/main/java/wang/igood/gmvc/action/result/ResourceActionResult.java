package wang.igood.gmvc.action.result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.app.Velocity;
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
	public void render() {
		RequestContext beat = RequestContext.current();
		HttpServletRequest request = beat.getRequest();
		HttpServletResponse response = beat.getResponse();
		if(resourceFile == null) {
			return ;
		}
		PrintWriter pw = null;
		try {
			StringWriter stringWriter = new StringWriter();
			IOUtils.copy(new FileInputStream(resourceFile), stringWriter, StandardCharsets.UTF_8.name());
			pw = response.getWriter();
			pw.write(stringWriter.toString());
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
}
