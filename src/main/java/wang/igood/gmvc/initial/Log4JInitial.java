package wang.igood.gmvc.initial;

import java.io.File;
import java.io.FileInputStream;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.common.AppInit;

/************************************************************
* <a>日志初始化</a>
* @author sunliang
* @since 2017-11-17
* @mail 1130437154@qq.com
* *********************************************************
* 1：方法摘要
* 		1.1：init   						初始化
* 		1.2：initVelocityProperties   	初始化Velocity参数
*/
public class Log4JInitial implements AppInit{

	private static final Logger LOG = LoggerFactory.getLogger(AppInitial.class);
	
	/**
	 * <a>1.1:初始化</a>
	 * **/
	@Override
	public void init() {
		try {
			LOG.info("log4j initial start...");
			File file = new File(this.getClass().getClassLoader().getResource("").getPath()+"/log4j.properties");
			LOG.info("log4j配置文件地址："+file.getAbsolutePath());
			if(file.exists())
				PropertyConfigurator.configure(new FileInputStream(file));
			else
				BasicConfigurator.configure();
			LOG.info("log4j initial complete");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
