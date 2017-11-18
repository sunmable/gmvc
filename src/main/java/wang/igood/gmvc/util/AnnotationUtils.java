package wang.igood.gmvc.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtils {

    static final String VALUE = "value";

    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        if (clazz == null) {
          throw new NullPointerException("Class must not be null");
        }
        A annotation = clazz.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        for (Class<?> ifc : clazz.getInterfaces()) {
            annotation = findAnnotation(ifc, annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        if (!Annotation.class.isAssignableFrom(clazz)) {
            for (Annotation ann : clazz.getAnnotations()) {
                annotation = findAnnotation(ann.annotationType(), annotationType);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null || superClass == Object.class) {
            return null;
        }
        return findAnnotation(superClass, annotationType);
    }

    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
        
       return method.getAnnotation(annotationType);
    }

}
