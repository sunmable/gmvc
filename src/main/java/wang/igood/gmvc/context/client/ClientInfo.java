package wang.igood.gmvc.context.client;

import wang.igood.gmvc.context.RequestContext;

public class ClientInfo {

	private final RequestContext context;

	private CookieHandler cookie = null;

	public ClientInfo(RequestContext context) {
		this.context = context;
	}

	public CookieHandler getCookies() {
		if (cookie == null) {
			cookie = new CookieHandler(context);
		}

		return cookie;
	}

//	public WFHttpServletRequestWrapper getUploads() {
//		HttpServletRequest request = context.getRequest();
//		return (request instanceof WFHttpServletRequestWrapper) ? (WFHttpServletRequestWrapper) request : null;
//	}
//
//	public boolean isUpload() {
//		return getUploads() == null ? false : true;
//	}

}
