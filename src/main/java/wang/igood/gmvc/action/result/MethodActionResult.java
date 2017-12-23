package wang.igood.gmvc.action.result;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.runtime.resource.ResourceManager;

import wang.igood.gmvc.Constant;
import wang.igood.gmvc.context.RequestContext;

/************************************************************
 * <a>正常HTML结果</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * ***********************************************************
 * 1：方法摘要
 * 		1.1:MethodActionResult		构造
 * 		1.2:render					渲染业务		
 */
public class MethodActionResult implements ActionResult {

	private String viewName;
	private static final String suffix = Constant.PAGESUFFIX;

	/**
	   * <a>1.1:构造函数</a>
	   * @param viewName 		Html文件名
	   * */
	public MethodActionResult(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * <a>1.2:Velocity渲染业务</a>
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * */
	@Override
	public void render() throws FileNotFoundException, IOException {
		if(viewName.startsWith("/")) {
			viewName = viewName.substring(1);
		}
		String path = "/templates/"+ viewName + suffix;
		
		RequestContext requestContext = RequestContext.current();
		if(!Velocity.resourceExists(path)) {
			InputStream is = requestContext.getRequest().getServletContext().getResourceAsStream(path);
			String html = IOUtils.toString(is, Constant.ENCODING);
			new TemplateActionResult(html).render();
		}else {
			HttpServletResponse response = requestContext.getResponse();
			response.setContentType("text/html;charset=\"" + Constant.ENCODING + "\"");
			response.setCharacterEncoding(Constant.ENCODING);
			Map<String, Object> data = requestContext.getModel().getModel();
			Context context = new VelocityContext(data);
			
			VelocityWriter vw = null;
			try {
				vw = new VelocityWriter(response.getWriter());
				Template template = Velocity.getTemplate(path);
				template.merge(context, vw);
				vw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(vw != null)
					vw.recycle(null);
			}
		}
		
	}
}
