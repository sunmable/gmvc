package wang.igood.gmvc;

import java.io.File;
import java.io.FileInputStream;
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
	private static Constant constant;
	
	public void  init() {
		WEBAPPPATH =Constant.class.getClassLoader().getResource("").getPath();
		File file = new File(getClass().getClassLoader().getResource("").getPath()+"/application.properties");
		scanBasePackage.add("wang.igood");
		System.out.println(file.getAbsolutePath());
		if(file.exists()) {
			System.out.println("abc");
			 try {
				 InputStream in = new FileInputStream(file);
				 Properties pps = new Properties();
				pps.load(in);
				String path = (String) pps.get("gmvc.scan-base-package");
				System.out.println(path);
				scanBasePackage.addAll(Arrays.asList(path.split(",")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("SCANBASEPACKAGE is"+scanBasePackage);
	}
	
	public static Constant  shareInstance() {
		if(constant == null) {
			constant = new Constant();
		}
		return constant;
	}
	
}
