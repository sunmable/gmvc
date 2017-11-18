package wang.igood.gmvc.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wang.igood.gmvc.common.Model;
import wang.igood.gmvc.context.client.ClientInfo;
import wang.igood.gmvc.context.servier.ServerInfo;
/************************************************************
 * <a>请求上下文</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * *********************************************************
 * 1：方法摘要
 * 		1.1：RequestContext   无参构造
 * 		1.2：RequestContext   构造
 * 		1.3：register		 注册
 * 		1.4：clear			 清空上下文
 * 		1.4：current			 前当上下文
 * **/
public class RequestContext implements Cloneable{

	private static final ThreadLocal<RequestContext> POOL = new ThreadLocal<RequestContext>();
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ClientInfo clientInfo;
	private ServerInfo serverInfo;
	protected Model model;
	
	/***
	 * <a>1.1：无参构造</a>
	 * */
	public RequestContext() {
		super();
	}

	/***
	 * <a>1.2：构造</a>
	 * @param request
	 * @param response
	 * */
	public RequestContext(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
		clientInfo = new ClientInfo(this);
		serverInfo = new ServerInfo(this);
		model = new Model();
	}
	
	/**
	 * <a>1.3:注册</a>
	 * @param request
	 * @param response
	 * */
	public static RequestContext register(HttpServletRequest request, HttpServletResponse response) {
		RequestContext context = new RequestContext(request, response);
		POOL.set(context);
		return context;
	}
	
	/**
	 * <a>1.4:清空</a>
	 * */
	public void clear() {
		POOL.remove();
	}
	/**
	 * <a>1.5:当前上下文</a>
	 * */
	public static RequestContext current() {
		return POOL.get();
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ClientInfo getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		RequestContext context = current();
		RequestContext newContext = new RequestContext();
		newContext.setClientInfo(context.getClientInfo());
		newContext.setRequest(context.getRequest());
		newContext.setResponse(context.getResponse());
		newContext.setServerInfo(context.getServerInfo());
		return newContext;
	}
}
