package wang.igood.gmvc.action.result;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import wang.igood.gmvc.Constant;
import wang.igood.gmvc.context.RequestContext;


public class TemplateActionResult  implements ActionResult{
	private final String template;

	/**
	   * <a>1.1:构造函数</a>
	   * @param 模板内容 		Html标签
	   * */
	public TemplateActionResult(String template) {
		this.template = template;
	}
	
	/**
	 * <a>1.2:Velocity渲染业务</a>
	 * */
	@Override
	public void render(){
		
		 RequestContext beat = RequestContext.current();

		HttpServletResponse response = beat.getResponse();
		response.setContentType("text/html;charset=\"" + Constant.ENCODING + "\"");
		response.setCharacterEncoding(Constant.ENCODING);

		Map<String, Object> data = beat.getModel().getModel();
		Context context = new VelocityContext(data);
		
		StringWriter stringWriter = new StringWriter();
		Velocity.evaluate(context, stringWriter, "TemplateActionResult", template);
		PrintWriter pw = null;
		try {
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