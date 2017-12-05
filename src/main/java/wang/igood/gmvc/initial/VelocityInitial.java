package wang.igood.gmvc.initial;

import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.Constant;
import wang.igood.gmvc.common.AppInit;

/************************************************************
* <a>模板引擎初始化</a>
* @author sunliang
* @since 2017-11-17
* @mail 1130437154@qq.com
* *********************************************************
* 1：方法摘要
* 		1.1：init   						初始化
* 		1.2：initVelocityProperties   	初始化Velocity参数
*/
public class VelocityInitial implements AppInit{

	private static final Logger LOG = LoggerFactory.getLogger(AppInitial.class);
	
	/**
	 * <a>1.1:初始化</a>
	 * **/
	@Override
	public void init() {
		try {
			LOG.info("velocity initial start...");
			Velocity.init(initVelocityProperties());
			LOG.info("velocity initial complete");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * <a>1.2:初始化Velocity参数</a>
	 * @return Properties
	 * **/
	private  Properties initVelocityProperties() {
		Properties p =new Properties();
		p.put("resource.loader", "file");
	    p.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
	    p.put("input.encoding", "utf-8");
	    p.put("output.encoding", "utf-8");
	    p.put("default.contentType", "text/html; charset=" + Constant.ENCODING);
	    p.put("velocimarco.library.autoreload", "true");
	    p.put("runtime.log.error.stacktrace", "false");
	    p.put("runtime.log.warn.stacktrace", "false");
	    p.put("runtime.log.info.stacktrace", "false");
	    p.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
	    p.put("runtime.log.logsystem.log4j.category", "velocity_log");
	    p.put("file.resource.loader.cache", "true");
	    p.put("file.resource.loader.modificationCheckInterval", "0");
	    p.put("file.resource.loader.path", Constant.WEBAPPPATH);
	    return p;
	}
}
