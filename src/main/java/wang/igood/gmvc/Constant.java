package wang.igood.gmvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Constant {

	public static final String ENCODING = "UTF-8";
	public static final String PAGESUFFIX = ".html";
	public static String WEBAPPPATH = "";
	public static boolean DEBUG = false;
	public static ArrayList<String> scanBasePackage = new ArrayList<String>();
	private static Constant constant;
	private Properties applictionPro;

	public void init() {
		WEBAPPPATH = Constant.class.getClassLoader().getResource("").getPath();
		File file = new File(getClass().getClassLoader().getResource("").getPath() + "/application.properties");
		scanBasePackage.add("wang.igood");
		if (file.exists()) {
			try {
				InputStream in = new FileInputStream(file);
				applictionPro = new Properties();
				applictionPro.load(new InputStreamReader(in, "utf-8"));
				String path = (String) applictionPro.get("gmvc.scan-base-package");
				System.out.println(path);
				scanBasePackage.addAll(Arrays.asList(path.split(",")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("SCANBASEPACKAGE is" + scanBasePackage);
	}
	
	public Properties getApplictionPro() {
		return applictionPro;
	}
	
	public static Constant shareInstance() {
		if (constant == null) {
			constant = new Constant();
		}
		return constant;
	}

}
