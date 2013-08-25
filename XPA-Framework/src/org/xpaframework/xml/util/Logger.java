package org.xpaframework.xml.util;

import android.util.Log;

/**
 * 
 * @author Jan Janickovic
 */
public class Logger {

	private static Configuration configuration = new Configuration();
	
	/**
	 * Enumeration containing logging levels. The enumerated values correspond
	 * to the values from {@link Log} values:
	 * <ul>
	 * <li>{@link Log#VERBOSE}</li>
	 * <li>{@link Log#DEBUG}</li>
	 * <li>{@link Log#INFO}</li>
	 * <li>{@link Log#WARN}</li>
	 * <li>{@link Log#ERROR}</li>
	 * </ul>
	 * 
	 * @author Jan Janickovic
	 * 
	 * @see Log#VERBOSE
	 * @see Log#DEBUG
	 * @see Log#INFO
	 * @see Log#WARN
	 * @see Log#ERROR
	 */
	public enum Level {
		VERBOSE(2),
		DEBUG(3),
		INFO(4),
		WARNING(5),
		ERROR(6);
		
		private int level;
		
		/**
		 * @return numeric value of logging level.
		 */
		public int getLevel() {
			return level;
		}

		private Level(int level) {
			this.level = level;
		}
	}
	
	public static Configuration getConfig() {
		return configuration;
	}

	private Class<?> logClass;
	
	protected Logger(Class<?> cls) {
		this.logClass = cls;
	}
	
	public static Logger getLogger(Class<?> cls) {
		return new Logger(cls);
	}
	
	/**
	 * <p>Method prints verbose message by {@link Log
	 * #v(String, String)} by setting the simple name of the target class</p>. 
	 * 
	 * @param msg - message string to be printed.
	 * 
	 * @see Log#v(String, String)
	 */
	public void v(String msg) {
		print(Level.VERBOSE, msg);
	}
	
	/**
	 * <p>Method prints debug message by {@link Log
	 * #d(String, String)} by setting the simple name of the target class</p>. 
	 * 
	 * @param msg - message string to be printed.
	 * 
	 * @see Log#d(String, String)
	 */
	public void d(String msg) {
		print(Level.DEBUG, msg);
	}
	
	/**
	 * <p>Method prints info message by {@link Log
	 * #w(String, String)} by setting the simple name of the target class</p>. 
	 * 
	 * @param msg - message string to be printed.
	 * 
	 * @see Log#i(String, String)
	 */
	public void i(String msg) {
		print(Level.INFO, msg);
	}
	
	/**
	 * <p>Method prints warning message by {@link Log
	 * #w(String, String)} by setting the simple name of the target class</p>. 
	 * 
	 * @param msg - message string to be printed.
	 * 
	 * @see Log#w(String, String)
	 */
	public void w(String msg) {
		print(Level.WARNING, msg);
	}
	
	/**
	 * <p>Method prints error message by {@link Log
	 * #e(String, String)} by setting the simple name of the target class</p>. 
	 * 
	 * @param msg - message string to be printed.
	 * 
	 * @see Log#e(String, String)
	 */
	public void e(String msg) {
		print(Level.ERROR, msg);
	}
	
	/**
	 * <p>Method prints error message by {@link Log
	 * #e(String, String, Throwable)} and the {@link Throwable}'s stack trace by setting
	 * the simple name of the target class.</p>
	 * <p>Note, that this level of logging is printed always, even if the
	 * logging level is set to smaller value or is completely disabled.</p>
	 * 
	 * @param msg - message string to be printed.
	 * @param t - {@link Throwable} cause.
	 * 
	 * @see Log#e(String, String)
	 */
	public void e(String msg, Throwable t) {
		Log.e(this.logClass.getSimpleName(), msg, t);
	}
	
	protected void print(Level level, String msg) {
		if(configuration.isLoggingEnabled() && configuration.getLevel().getLevel() <= level.getLevel()) {
			Log.println(level.level, this.logClass.getSimpleName(), msg);
		}
	}
	
	/**
	 * <p>Logger configuration class.</p>
	 * 
	 * @author Jan Janickovic
	 */
	public static class Configuration {
		
		private boolean loggingEnabled;
		private Level level = Level.WARNING;

		public boolean isLoggingEnabled() {
			return loggingEnabled;
		}

		public void setLoggingEnabled(boolean enabled) {
			this.loggingEnabled = enabled;
		}
		
		public Level getLevel() {
			return level;
		}

		public void setLevel(Level level) {
			this.level = level;
		}

	}
	
}
