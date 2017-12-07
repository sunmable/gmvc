package wang.igood.gmvc.util.scan;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefaultClassFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClassFilter.class);
	protected static ClassLoader DefaultClassLoader = Thread.currentThread().getContextClassLoader();
	protected final String packageName;
	private JarFile jarFile;

	protected DefaultClassFilter(final String packageName) {
		this.packageName = packageName;
	}

	protected DefaultClassFilter(final String packageName, ClassLoader classLoader) {
		this.packageName = packageName;
		DefaultClassFilter.DefaultClassLoader = classLoader;
	}

	public final Set<Class<?>> getAllClassList() {
		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		try {
			clazzes.addAll(getClassList(DefaultClassLoader.getResources(packageName.replace(".", "/"))));
			clazzes.addAll(getLocalClass(getProjectPath()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazzes;
	}

	public final Set<Class<?>> getClassList(Enumeration<URL> urls) {
		// 收集符合条件的Class类容器
		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		try {
			// 遍历 URL 资源
			URL url;
			while (urls.hasMoreElements()) {
				url = urls.nextElement();

				if (url != null) {
					LOGGER.debug("scan url >> " + url.toString());
					// 获取协议名（分为 file 与 jar）
					String protocol = url.getProtocol();
					if (protocol.equals("file")) { // classPath下的.class文件
						String packagePath = url.getPath();
						addClass(clazzes, packagePath, packageName);
					} else if (protocol.equals("jar")) {// classPath下的.jar文件
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						JarFile jarFile = jarURLConnection.getJarFile();
						Enumeration<JarEntry> jarEntries = jarFile.entries();
						while (jarEntries.hasMoreElements()) {
							JarEntry jarEntry = jarEntries.nextElement();
							String jarEntryName = jarEntry.getName();
							// 判断该 entry 是否为 class
							if (jarEntryName.endsWith(".class")) {
								// 获取类名
								String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
										.replaceAll("/", ".");
								// 执行添加类操作
								doAddClass(clazzes, className);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("find class error！", e);
		}
		return clazzes;
	}

	public final Set<Class<?>> getClassList() {
		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		try {
			clazzes = getClassList(DefaultClassLoader.getResources(packageName.replace(".", "/")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clazzes;
	}

	private void addClass(Set<Class<?>> clazzes, String packagePath, String packageName) {
		try {
			// 获取包名路径下的 class 文件或目录
			File[] files = new File(packagePath).listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
				}
			});
			// 遍历文件或目录
			for (File file : files) {
				String fileName = file.getName();
				// 判断是否为文件或目录
				if (file.isFile()) {
					// 获取类名
					String className = fileName.substring(0, fileName.lastIndexOf("."));
					if (StringUtils.isNotEmpty(packageName)) {
						className = packageName + "." + className;
					}
					// 执行添加类操作
					doAddClass(clazzes, className);
				} else {
					// 获取子包
					String subPackagePath = fileName;
					if (StringUtils.isNotEmpty(packagePath)) {
						subPackagePath = packagePath + "/" + subPackagePath;
					}
					// 子包名
					String subPackageName = fileName;
					if (StringUtils.isNotEmpty(packageName)) {
						subPackageName = packageName + "." + subPackageName;
					}
					// 递归调用
					addClass(clazzes, subPackagePath, subPackageName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("find class error！", e);
		}
	}

	private void doAddClass(Set<Class<?>> clazzes, String className) throws ClassNotFoundException {
		// 加载类
		Class<?> cls = DefaultClassLoader.loadClass(className);
		// 判断是否可以添加类
		if (filterCondition(cls)) {
			// 添加类
			clazzes.add(cls);
			LOGGER.debug("add class:{}", cls.getName());
		}
	}
	
	
	private Set<Class<?>>  getLocalClass(String localPath) {
		try {
			Set<Class<?>> clazzes = new HashSet<Class<?>>();
			File files = new File(localPath);
			for (File file : files.listFiles()) {
				String fileName = file.getName();
				String protocol = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
				if("jar".equals(protocol)) {
					jarFile = new JarFile(file.getAbsolutePath());
					if (jarFile != null) {
						// 得到该jar文件下面的类实体
						Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
						while (jarEntryEnumeration.hasMoreElements()) {
							JarEntry entry = jarEntryEnumeration.nextElement();
							String jarEntryName = entry.getName();
							// 这里我们需要过滤不是class文件和不在basePack包名下的类
							if (jarEntryName.contains(".class")) {
								try {
									String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
									Class<?> cls = Class.forName(className);
									clazzes.add(cls);
								}catch(Exception e) {
									e.printStackTrace();
									continue;
								}
								
							}
						}
					}
				}
			}
			return clazzes;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getProjectPath() {

		java.net.URL url = DefaultClassFilter.class.getProtectionDomain().getCodeSource().getLocation();
		String filePath = null;
		try {
			filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filePath.endsWith(".jar"))
			filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		java.io.File file = new java.io.File(filePath);
		filePath = file.getAbsolutePath();
		return filePath;
	}

	/**
	 * 验证是否允许添加类
	 */
	public abstract boolean filterCondition(Class<?> clazz);
}
