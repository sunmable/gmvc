package wang.igood.gmvc.action.result;

import java.io.IOException;
import java.io.PrintWriter;

import wang.igood.gmvc.Constant;
import wang.igood.gmvc.context.RequestContext;

/************************************************************
 * <a>字符串渲染结果</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * ***********************************************************
 * 1：方法摘要
 * 		1.1:StringActionResult		构造
 * 		1.2:render					字符串渲染		
 */
public class StringActionResult implements ActionResult{

	private final String content;
	private final int httpErrorCode;
	
	/**
	   * <a>1.1:构造函数</a>
	   * @param content 		渲染内容
	   * */
	public StringActionResult(String content) {
		this(content, 0);
	}

	/**
	   * <a>1.1:构造函数</a>
	   * @param content 		渲染内容
	   * @param httpErrorCode 		渲染CODE
	   * */
	public StringActionResult(String content, int httpErrorCode) {
		this.content = content;
		this.httpErrorCode = httpErrorCode;
	}


	@Override
	public void render() throws Exception{
		
		RequestContext context = RequestContext.current();
		context.getResponse().setHeader("Pragma", "no-cache");
		context.getResponse().setHeader("Cache-Control", "no-cache");
		context.getResponse().setHeader("Content-Type", "text/html; charset=" + Constant.ENCODING);
		context.getResponse().setCharacterEncoding(Constant.ENCODING);
		context.getResponse().setDateHeader("Expires", -1);
		if (0 == httpErrorCode) {
			PrintWriter pw = null;
			try {
				pw = context.getResponse().getWriter();
				pw.write(content);
				pw.flush();
			} catch (Exception e) {
				throw e;
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		} else {
			try {
				context.getResponse().sendError(httpErrorCode, content);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
