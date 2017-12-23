package wang.igood.gmvc.initial;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.Constant;
import wang.igood.gmvc.action.ResourceAction;
import wang.igood.gmvc.common.AppInit;

/************************************************************
* <a>资源初始化</a>
* @author sunliang
* @since 2017-11-17
* @mail 1130437154@qq.com
* *********************************************************
* 1：方法摘要
* 		1.1：init   				初始化
*/
public class ResourceInitial{
	
	private static final Logger LOG = LoggerFactory.getLogger(ResourceInitial.class);
	private static final Map<String,ResourceAction> resourceActionMap = new HashMap<String, ResourceAction>();
	private static String RESPATH;
	
	public ResourceInitial() {
		RESPATH = Constant.WEBAPPPATH+"META-INF/resources";
	}
	
	/**
	 * <a>1.1:初始化</a>
	 * **/
//	@Override
	public void init() {
		LOG.info("initial resource start...");
		LOG.debug("RESPATH:{}",RESPATH);
		List<ResourceAction> actions = getResourceActions(RESPATH);
		for(ResourceAction action : actions) {
			LOG.debug("resource key:{}",action.path());
			resourceActionMap.put(action.path(), action);
		}
	}
	
	private List<ResourceAction> getResourceActions(String basePath) {
		List<ResourceAction> actions = new ArrayList<ResourceAction>();
		File file = new File(basePath);
		HashMap<String,File>  fileMap = getFilesPath(file);
		for (Map.Entry<String, File> entry : fileMap.entrySet()) {
			ResourceAction resourceAction = new ResourceAction(entry.getKey(),entry.getValue());
			actions.add(resourceAction);
		}
		return actions;
	}
	
	private HashMap<String,File> getFilesPath(File file){
		HashMap<String,File> fileNames = new HashMap<String,File>();
		if(!file.isDirectory()) {
			String key = file.getAbsolutePath().replace(RESPATH+"/statics", "").replaceAll(" ", "").trim();
			key = key.replace(RESPATH+"/templates", "").replace(" ", "");
			fileNames.put(key,file);
		}else {
			for(File file_ : file.listFiles()) {
				fileNames.putAll(getFilesPath(file_));
			}
		}
		return fileNames;
	}

//	public static Map<String, ResourceAction> getResourceactionmap() {
//		return resourceActionMap;
//	}
}
