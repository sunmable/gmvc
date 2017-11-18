package wang.igood.gmvc.action.result;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.context.RequestContext;
import wang.igood.gmvc.util.StringUtils;

/************************************************************
 * <a>重定向结棍</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * ***********************************************************
 * 1：方法摘要
 * 		1.1:HttpActionResult			构造
 * 		1.2:render					重定向业务		
 */
public class HttpActionResult implements ActionResult {

  private static final Logger logger = LoggerFactory.getLogger(HttpActionResult.class);
  private String url = "";
  
  /**
   * <a>1.1:构造函数</a>
   * @param path 		重定向地址
   * */
  public HttpActionResult(String path) {
    this.url = path;
  }

  /**
   * <a>1.2:重定向业务</a>
   * */
  @Override
  public void render() throws Exception {
    if (!StringUtils.isEmpty(url)) {
      if (logger.isDebugEnabled()) {
        logger.debug("redirect to url : " + url);
      }
      try {
        RequestContext.current().getResponse().sendRedirect(url);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (logger.isWarnEnabled()) {
      logger.warn("URL is required");
    }
  }

}
