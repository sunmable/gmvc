package wang.igood.gmvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.action.result.ActionResult;
import wang.igood.gmvc.action.result.StringActionResult;
import wang.igood.gmvc.context.RequestContext;
import wang.igood.gmvc.exception.ExceptionHandlerManager;
import wang.igood.gmvc.initial.AppInitial;

/************************************************************
* <a>Bootstrap项目引导</a>
* @author sunliang
* @since 2017-11-17
* @mail 1130437154@qq.com
* 
* *********************************************************
* 1：方法摘要
* 		1.1：init   		初始化
* 		1.2：doFilter   	请求过滤分发
* 		1.3：destroy		消亡
*/
@WebFilter(urlPatterns = { "/*" }, asyncSupported = true)
public class Bootstrap implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	private static final AtomicBoolean hasInit = new AtomicBoolean(false);
	private RequestDispatcher dispatcher = null;

	/**
	 * <a>1.1：初始化</a>
	 * @param	filterConfig
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		long start = System.currentTimeMillis();
		Constant.init();
		System.out.println("WEBAPPPATH:"+Constant.WEBAPPPATH);
		if (hasInit.compareAndSet(false, true)) {
			AppInitial.initial();
			dispatcher = new RequestDispatcher();
		}
		logger.info("GMVC initial complete. cost time : " + (System.currentTimeMillis() - start) + "ms");
	}

	/**
	 * <a>1.2：请求过滤分发</a>
	 * @param request
	 * @param response
	 * @param filterchain
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;

		if (request.getCharacterEncoding() == null) {
			request.setCharacterEncoding("UTF-8");
		}

		RequestContext context = RequestContext.register(httpReq, httpResp);
		try {
			ActionResult result = dispatcher.beforeService(context);
			if (result != null) {
				result.render();
				return;
			}
			result = dispatcher.service(context);
			if (result != null) {
				result.render();
			}
			result = dispatcher.afterService(context);
			if (result != null) {
				result.render();
			}
			if(result == null && Constant.DEBUG) {
				new StringActionResult("404",404).render();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				ActionResult result = ExceptionHandlerManager.handleException(e);
				if(result != null) {
					result.render();
				}else {
					if(Constant.DEBUG) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();  
						e.printStackTrace(new PrintStream(baos)); 
						String exception = baos.toString();  
						new StringActionResult(exception).render();
					}else {
						new StringActionResult("500",500).render();
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			logger.error(e.getMessage());
		} finally {
			ActionResult result = dispatcher.completeService(context);
			if (result != null) {
				try {
					result.render();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		context.clear();

	}

	/**
	 * <a>1.3:消亡</a>
	 **/
	@Override
	public void destroy() {
		logger.info("destroy");
	}

}
