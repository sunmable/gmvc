package wang.igood.gmvc.common;

import java.util.HashMap;
import java.util.Map;

/************************************************************
 * <a>数据结果</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * *********************************************************
 * 1：方法摘要
 * 		1.1：add   		增加一个属性
 * 		1.2：get			根据属性名得到属性值
 * 		1.3：getModel	获取结果集
 * 		1.4：addAll		批量添加结果集
 * 		1.5：contains	判断是否包含
 * 		1.6：merge		合并属性		
 * **/
public class Model {
	private final Map<String, Object> modelMap = new HashMap<String, Object>();

	  public Model() {
	  }

	  /**
	   * <a>1.1:增加一个属性</a>
	   * @param attributeName 属性名称
	   * @param attributeValue 属性值
	   */
	  public Model add(String attributeName, Object attributeValue) {
	    modelMap.put(attributeName, attributeValue);
	    return this;
	  }

	  /**
	   * <a>1.2:根据属性名得到属性值</a>
	   * @param attributeName 属性名称
	   * @return 对应的属性值
	   */
	  public Object get(String attributeName) {
	    return modelMap.get(attributeName);
	  }

	  /**
	   * <a>1.3:获取结果集</a>
	   */
	  public Map<String, Object> getModel() {
	    return modelMap;
	  }

	  /**
	   * <a>1.4:批量增加属性</a>
	   * 
	   * @param attributes
	   */
	  public Model addAll(Map<String, Object> attributes) {
	    modelMap.putAll(attributes);
	    return this;
	  }

	  /**
	   * <a>1.5:判断是否包含属性名</a>
	   * 
	   * @param attributeName 需要查找的属性
	   * @return
	   */
	  public boolean contains(String attributeName) {
	    return modelMap.containsKey(attributeName);
	  }

	  /**
	   * <a>1.6:合并属性</a>
	   * 
	   * @param attributes
	   */
	  public Model merge(Map<String, Object> attributes) {
	    if (attributes != null) {

	      for (Map.Entry<String, Object> entry : attributes.entrySet()) {
	        if (!modelMap.containsKey(entry.getKey())) {
	          modelMap.put(entry.getKey(), attributes.get(entry.getKey()));
	        }

	      }
	    }
	    return this;
	  }
}
