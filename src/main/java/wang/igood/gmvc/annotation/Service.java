package wang.igood.gmvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/************************************************************
 * <a>标识服务类</a>
 * @author sunliang
 * @since 2017-12-06
 * @mail 1130437154@qq.com
 * ***********************************************************/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
}