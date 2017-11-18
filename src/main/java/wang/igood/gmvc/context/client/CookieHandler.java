package wang.igood.gmvc.context.client;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wang.igood.gmvc.context.RequestContext;

public class CookieHandler {

	private final HttpServletRequest request;

	private final HttpServletResponse response;

	private Cookie[] cookies = null;

	public CookieHandler(RequestContext context) {
		this.request = context.getRequest();
		this.response = context.getResponse();
	}

	public void add(String name, String value) {

		Cookie cookie = new Cookie(name, value);
		// 设置路径（默认）
		cookie.setPath("/");
		// 把cookie放入响应中
		addToResponse(cookie);
	}

	public void add(String name, String value, int cookieMaxAge) {

		Cookie cookie = new Cookie(name, value);
		// 设置有效日期
		cookie.setMaxAge(cookieMaxAge);
		// 设置路径（默认）
		cookie.setPath("/");

		addToResponse(cookie);

	}

	public void addToResponse(Cookie cookie) {
		response.addCookie(cookie);
	}

	public String get(String name) {

		Cookie cookie = getCookie(name);
		return cookie == null ? null : cookie.getValue();
	}

	public Cookie getCookie(String name) {

		Cookie[] cookies = getCookies();

		for (Cookie cookie : cookies) {
			if (name.equalsIgnoreCase(cookie.getName()))
				return cookie;
		}

		return null;
	}

	public Cookie[] getCookies() {

		if(cookies == null) {
			cookies = request.getCookies();
			if (cookies == null)
				cookies = new Cookie[0];
		}
		
		return this.cookies;
	}

	public void remove(String name) {
		Cookie cookie = getCookie(name);
		// 销毁
		if (cookie != null) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	public void set(String name, String value) {
		Cookie cookie = getCookie(name);

		if (cookie == null) {
			add(name, value);
		} else {
			cookie.setValue(value);
		}

	}

	public void set(String name, String value, int time) {
		Cookie cookie = getCookie(name);

		if (cookie == null) {
			add(name, value, time);
		} else {
			cookie.setValue(value);
			cookie.setMaxAge(time);
		}
	}

	public void delete(String name) {
		remove(name);
	}

}
