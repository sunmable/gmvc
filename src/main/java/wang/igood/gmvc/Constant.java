package wang.igood.gmvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;


public class Constant {
	
	public static final String ENCODING = "UTF-8";
	public static final String PAGESUFFIX = ".html";
	public static String  WEBAPPPATH = "";
	public static boolean DEBUG = false;
	public static ArrayList<String> scanBasePackage = new ArrayList<String>();
	
	public static void  init() {
		WEBAPPPATH =Constant.class.getClassLoader().getResource("").getPath()+"META-INF";
		InputStream in = Constant.class.getResourceAsStream("application.properties");
		scanBasePackage.add("wang.igood.gmvc");
		if(in != null) {
			 try {
				 Properties pps = new Properties();
				pps.load(in);
				String path = (String) pps.get("gmvc.scan-base-package");
				scanBasePackage.addAll(Arrays.asList(path.split(",")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
