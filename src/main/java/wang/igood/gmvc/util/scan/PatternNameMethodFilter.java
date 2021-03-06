package wang.igood.gmvc.util.scan;

public abstract class PatternNameMethodFilter extends DefaultMethodFilter {

	protected final String methodPattern;

	protected PatternNameMethodFilter(Class<?> clazz, String methodPattern) {
		super(clazz);
		this.methodPattern = methodPattern;
	}

}
