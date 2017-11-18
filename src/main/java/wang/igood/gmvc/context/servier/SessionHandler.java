package wang.igood.gmvc.context.servier;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import wang.igood.gmvc.context.RequestContext;


public class SessionHandler {
	
	private final RequestContext beat;
	
	private final HttpSession session;

	public SessionHandler(RequestContext beat) {
		this.beat = beat;
		this.session = this.beat.getRequest().getSession();
	}
	
	public Object get(String name){
		return session.getAttribute(name);
		
	}
	
	public Object getCreationTime(){
		return session.getCreationTime();
	}
	
	public Enumeration<String> getNames(){
		return session.getAttributeNames();
	}
	
	public String getId(){
		return session.getId();
	}
	
	public long getLastAccessedTime(){
		return session.getLastAccessedTime();
	}
	
	public int getMaxInactiveInterval(){
		return session.getMaxInactiveInterval();
	}
	
	public void invalidate(){
		session.invalidate();
	}
	
	public boolean isNew(){
		return session.isNew();
	}
	
	
	public void remove(String name){
		session.removeAttribute(name);
	}
	
	public void set(String name, Object value){
		session.setAttribute(name, value);
	}
	
	public void setMaxInactiveInterval(int value){
		session.setMaxInactiveInterval(value);
	}

	public void flush() {
	}
}
