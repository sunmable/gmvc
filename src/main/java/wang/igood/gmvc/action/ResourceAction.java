package wang.igood.gmvc.action;

import java.io.File;
import java.util.Set;

import wang.igood.gmvc.action.result.ActionResult;
import wang.igood.gmvc.action.result.ResourceActionResult;
import wang.igood.gmvc.common.State.HttpMethod;
import wang.igood.gmvc.context.RequestContext;

/**
 * 对静态文件处理，把所有静态文件名保存在set中，如何精确匹配，表明当前请求就是静态文件
 */
public class ResourceAction implements Action {

	private final String path;
	private Set<HttpMethod> supportMethods = HttpMethod.suportHttpMethods();
	private File resourceFile;

	public ResourceAction(String path,File resourceFile) {
		this.path = path;
		this.resourceFile = resourceFile;
		initHttpMethods();
		
	}

	private void initHttpMethods() {
		supportMethods.add(HttpMethod.GET);
	}

	@Override
	public String path() {
		return path;
	}

	@Override
	public ActionResult invoke() {
		return new ResourceActionResult(path,resourceFile);
	}

	@Override
	public boolean matchHttpMethod() {
		String requestMethod = RequestContext.current().getRequest().getMethod();
		String currentMethod = HttpMethod.parse(requestMethod);
		Boolean result = false;
		try {
			HttpMethod httpMethod = HttpMethod.valueOf(currentMethod);
			result = supportMethods.contains(httpMethod);
		} catch (Exception e) {
			result = false;
		}

		return result;
	}

}
