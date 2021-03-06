package wang.igood.gmvc.util.scan;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Set;

public class DefaultClassScanner implements ClassScanner {
	private static final DefaultClassScanner Instance = new DefaultClassScanner();
	private DefaultClassScanner() {
	}

	public static DefaultClassScanner getInstance() {
		return Instance;
	}

	@Override
	public Set<Class<?>> getClassList(final String packageName, final String pattern) {
		Set<Class<?>> clazzs = new DefaultClassFilter(packageName) {
			@Override
			public boolean filterCondition(Class<?> cls) {
				String className = cls.getName();
				String patternStr = (null == pattern || pattern.isEmpty()) ? ".*" : pattern;
				return className.startsWith(packageName) && className.matches(patternStr);

			}
		}.getAllClassList();
		return clazzs;
	}

	@Override
	public Set<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
		return new AnnotationClassFilter(packageName, annotationClass) {
			@Override
			public boolean filterCondition(Class<?> cls) {
				return cls.isAnnotationPresent(annotationClass);
			}
		}.getAllClassList();
	}

	@Override
	public Set<Class<?>> getClassListBySuper(String packageName, Class<?> superClass) {
		return new SupperClassFilter(packageName, superClass) {
			@Override
			public boolean filterCondition(Class<?> clazz) { // 这里去掉了内部类
				return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz) && !Modifier.isInterface(clazz.getModifiers()) && !Modifier.isAbstract(clazz.getModifiers())
						&& Modifier.isPublic(clazz.getModifiers());// &&
																	// !cls.getName().contains("$");
			}

		}.getAllClassList();
	}

	@Override
	public Set<Class<?>> getClassList(final String packageName, final String pattern, ClassLoader classLoader) {
		return new DefaultClassFilter(packageName, classLoader) {
			@Override
			public boolean filterCondition(Class<?> cls) {
				String className = cls.getName();
				String pkgName = className.substring(0, className.lastIndexOf("."));
				String patternStr = (null == pattern || pattern.isEmpty()) ? ".*" : pattern;
				return pkgName.startsWith(packageName) && pkgName.matches(patternStr);
			}
		}.getAllClassList();
	}

	@Override
	public Set<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass, ClassLoader classLoader) {
		return new AnnotationClassFilter(packageName, annotationClass, classLoader) {
			@Override
			public boolean filterCondition(Class<?> cls) { // 这里去掉了内部类
				return cls.isAnnotationPresent(annotationClass);
			}
		}.getAllClassList();
	}

	@Override
	public Set<Class<?>> getClassListBySuper(String packageName, Class<?> superClass, ClassLoader classLoader) {
		return new SupperClassFilter(packageName, superClass, classLoader) {
			@Override
			public boolean filterCondition(Class<?> cls) { // 这里去掉了内部类
				return superClass.isAssignableFrom(cls) && !superClass.equals(cls);// &&
																					// !cls.getName().contains("$");
			}

		}.getAllClassList();
	}

}
