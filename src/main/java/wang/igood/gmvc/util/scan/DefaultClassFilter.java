package wang.igood.gmvc.util.scan;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

	protected DefaultClassFilter(final String packageName) {
		this.packageName = packageName;
	}

	protected DefaultClassFilter(final String packageName, ClassLoader classLoader) {
		this.packageName = packageName;
		DefaultClassFilter.DefaultClassLoader = classLoader;
	}

	public final Set<Class<?>> getClassList() {
		// 收集符合条件的Class类容器
		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		try {
			// 从包名获取 URL 类型的资源
			Enumeration<URL> urls = DefaultClassLoader.getResources(packageName.replace(".", "/"));
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
								String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
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
	
	public final Set<Class<?>> getAllClassList() {
		// 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = this.packageName;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    System.err.println("file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    addClass(classes, filePath, packageName);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
//                    System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection())
                                .getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx)
                                            .replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class")
                                            && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(
                                                packageName.length() + 1, name
                                                        .length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class
                                                    .forName(packageName + '.'
                                                            + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
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

	/**
	 * 验证是否允许添加类
	 */
	public abstract boolean filterCondition(Class<?> clazz);
}
