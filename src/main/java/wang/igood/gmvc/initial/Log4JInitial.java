package wang.igood.gmvc.initial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.PropertyConfigurator;

import wang.igood.gmvc.common.AppInit;
import wang.igood.gmvc.util.scan.DefaultClassFilter;

/************************************************************
* <a>日志初始化</a>
* @author sunliang
* @since 2017-11-20
* @mail 1130437154@qq.com
* *********************************************************
* 1：方法摘要
* 		1.1：init   			初始化
*/
public class Log4JInitial implements AppInit{

	@Override
	public void init() {
		try {
			java.net.URL url = DefaultClassFilter.class.getProtectionDomain().getCodeSource().getLocation();
			String filePath  = url.getPath() + "\\log4j.properties";
			File file = new File(filePath);
			if(file.exists())
				PropertyConfigurator.configure(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
