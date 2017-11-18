package wang.igood.gmvc.common;

import java.io.Serializable;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.action.result.ActionResult;
import wang.igood.gmvc.annotation.Interceptor.Scope;

/************************************************************
 * <a>拦截器</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * *********************************************************
 * 1：方法摘要
 * 		1.1：order   	索引
 * 		1.2：before		请求之前
 * 		1.3：after		请求之后
 * 		1.4：complete	请求结束
 * 		1.5：scope		作用范围
 * **/
public abstract class Interceptor {

  protected Logger _WFLOG = LoggerFactory.getLogger(this.getClass());

  /**
   * <a>1.1:索引</a>
   * */
  public abstract float order();

  /**
   * <a>1.2:请求之前</a>
   * */
  public abstract ActionResult before();

  /**
   * <a>1.3:请求之后</a>
   * */
  public abstract ActionResult after();

  /**
   * <a>1.4：请求结束</a>
   * */
  public abstract ActionResult complete();
  
  /**
   * <a>1.5：作用范围</a>
   * */
  public abstract Scope scope();

  public static final InteceptorSorter INTERCEPTOR_SORTER = new InteceptorSorter();

  /**
   * 拦截器实例的排序比较器
   */
  static final class InteceptorSorter implements Comparator<Interceptor>, Serializable {

    private static final long serialVersionUID = -1352842431969073707L;

    InteceptorSorter() {
    }

    @Override
    public int compare(Interceptor o1, Interceptor o2) {
      return o1.order() > o2.order() ? 1 : 0;
    }

  }
}
