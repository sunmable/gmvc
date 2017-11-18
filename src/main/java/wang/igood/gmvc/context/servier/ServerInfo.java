package wang.igood.gmvc.context.servier;

import wang.igood.gmvc.context.RequestContext;

public class ServerInfo {

	private final RequestContext beat;
	
	private SessionHandler session = null;
	
	public ServerInfo(RequestContext beat) {
		this.beat = beat;
	}
	
	/**
	 * session处理
	 * @return
	 */
	public SessionHandler getSessions(){
		
		if(session == null)
			session = new SessionHandler(beat);
		
		return session;
	}
	
	
}
