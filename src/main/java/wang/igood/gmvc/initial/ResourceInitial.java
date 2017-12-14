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
public class ResourceInitial implements AppInit{
	
	private static final Logger LOG = LoggerFactory.getLogger(ResourceInitial.class);
	private static final Map<String,ResourceAction> resourceActionMap = new HashMap<String, ResourceAction>();
	private static String RESPATH;
	
	public ResourceInitial() {
		RESPATH = Constant.WEBAPPPATH+"/resources";
	}
	
	/**
	 * <a>1.1:初始化</a>
	 * **/
	@Override
	public void init() {
		LOG.info("initial resource start...");
		LOG.debug("RESPATH:{}",RESPATH);
		List<ResourceAction> actions = getResourceActions(RESPATH);
		for(ResourceAction action : actions) {
			String key = action.path().replace(RESPATH, "").replace(" ", "");
			LOG.debug("----------key:{},path:{}",key,action.path());
			resourceActionMap.put(key, action);
		}
	}
	
	private List<ResourceAction> getResourceActions(String basePath) {
		List<ResourceAction> actions = new ArrayList<ResourceAction>();
		File file = new File(basePath);
		List<String> fileNames = getFilesPath(file);
		for(String fileName : fileNames) {
			ResourceAction resourceAction = new ResourceAction(""+fileName);
			actions.add(resourceAction);
		}
		return actions;
	}
	
	private List<String> getFilesPath(File file){
		List<String> fileNames = new ArrayList<>();
		if(!file.isDirectory()) {
			String key = file.getAbsolutePath().replace(RESPATH, "").replaceAll(" ", "").trim();
			fileNames.add(key);
		}else {
			for(File file_ : file.listFiles()) {
				fileNames.addAll(getFilesPath(file_));
			}
		}
		return fileNames;
	}

	public static Map<String, ResourceAction> getResourceactionmap() {
		return resourceActionMap;
	}
}
