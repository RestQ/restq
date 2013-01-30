/**
 * 
 */
package org.restq.server.utils;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author ganeshs
 *
 */
public final class SpringUtil {
	
	private static ConfigurableApplicationContext context;

	public static void init(ConfigurableApplicationContext configurableApplicationContext) {
		context = configurableApplicationContext;
		context.registerShutdownHook();
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return getContext().getBean(clazz);
	}
	
	public static <T> T getBean(String name, Object... args) {
		return (T) getContext().getBean(name, args);
	}
	
	private static ConfigurableApplicationContext getContext() {
		if (context == null) {
			synchronized (SpringUtil.class) {
				if (context == null) {
					context = new GenericXmlApplicationContext();
					System.out.println("Test " + System.getProperty("profile", "dum"));
					if (System.getProperty("profile") != null) {
						context.getEnvironment().setActiveProfiles(System.getProperty("profile"));
					}
					((GenericXmlApplicationContext)context).load(new String[]{"/META-INF/applicationContext.xml"});
					context.refresh();
				}
			}
		}
		return context;
	}
	
	
	public static void destroy() {
		context.close();
	}

}
