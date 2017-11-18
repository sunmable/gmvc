package wang.igood.gmvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/************************************************************
 * <a>标识拦截器</a>
 * @author sunliang
 * @since 2017-11-17
 * @mail 1130437154@qq.com
 * ***********************************************************/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Interceptor {
	
    Class<? extends wang.igood.gmvc.common.Interceptor> value();

    Scope scope() default Scope.ACTION;
	
	public enum Scope {
		/**
		 * 作用于全局
		 */
		GLOBAL,
		
		/**
		 * 作用于某个Action
		 */
		ACTION;
	}
	
}