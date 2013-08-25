package org.xpaframework.xml;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.xpaframework.ValueAdapter;
import org.xpaframework.ValueAdapterRegistry;
import org.xpaframework.ValueConversionException;
import org.xpaframework.xml.annotation.XmlValueAdapter;



/**
 * <p>This resolver provides custom implementation of target object. The
 * adapters holding in this object are used for serialization and
 * deserialization of target object.The object implementing this value adapter
 * must follow rules:
 * <ul>
 * <li>must have public non-argument constructor</li>
 * <li>its type must represent element of simple type, e.g.
 * <code>&lt;name&gt;Bob&lt;/name&gt;</code></li>
 * </ul>
 * </p>
 * 
 * <p>The example of using this adapter can be:
 * <pre>@XmlValueAdapter(adapterClass = Currency.CurrencyValueAdapter.class)
 *public enum Currency {
 *
 *	EURO("EUR"),
 *	POUND("GBP"),
 *	DOLLAR("USD");
 *	
 *	private String value;
 *	
 *	public String getValue() {
 *		return value;
 *	}
 *
 *	private Currency(String value) {
 *		this.value = value;
 *	}
 *
 *	public class CurrencyValueAdapter implements ValueAdapter<Currency> {
 *
 *		public CurrencyValueAdapter() {
 *		}
 *		
 *		public String getTarget() {
 *			return getType().getCanonicalName();
 *		}
 *
 *		public Class<?> getType() {
 *			return getClass();
 *		}
 *
 *		public Currency convertValue(String value) throws ValueConversionException {
 *			if(value == null || value.isEmpty()) {
 *				return null;
 *			}
 *			
 *			for(Currency currency : Currency.values()) {
 *				if(currency.equals(value)) {
 *					return currency;
 *				}
 *			}
 *			
 *			throw new ValueConversionException("No value found for value: " + value);
 *		}
 *
 *		public String toString(Currency target) {
 *			return target.getValue();
 *		}
 *		
 *	}
 *}</pre>
 *</p>
 * <p>The code above awaits that the element mapped to the Currency object is
 * of simple type with restriction that only three values ("USD", "GBP" and
 * "EUR") are expected, any other values will throw
 * {@link ValueConversionException}.</p>
 * 
 * <p>This registry contains set of adapters that provides XML-to-Object and
 * Object-to-XML conversion. These types are defined by  XML-to-Java data types
 * specified by JAXB processing.</p>
 *
 * @param <T> - representing the type of the string conversion.
 * 
 * @see XmlValueAdapter
 * 
 * @author Jan Janickovic
 */
public class SimpleTypeRegistry extends ValueAdapterRegistry {
	
	private boolean initialized;
	
	protected SimpleTypeRegistry() {
		super();
	}
	
	@Override
	public void initialize() {
		registerAdapter(TYPE_BIG_DECIMAL);
		registerAdapter(TYPE_BIG_INTEGER);
		registerAdapter(TYPE_BOOLEAN);
		registerAdapter(TYPE_BYTE);
		registerAdapter(TYPE_BYTE_ARRAY);
		//registerAdapter(TYPE_DATE);
		registerAdapter(TYPE_CHAR);
		registerAdapter(TYPE_DOUBLE);
		registerAdapter(TYPE_DURATION);
		registerAdapter(TYPE_FLOAT);
		registerAdapter(TYPE_GREGORIAN_CALENDAR);
		registerAdapter(TYPE_INT);
		registerAdapter(TYPE_LONG);
		registerAdapter(TYPE_QNAME);
		registerAdapter(TYPE_SHORT);
		registerAdapter(TYPE_STRING);
		registerAdapter(TYPE_URL);
		
		this.initialized = true;
	}
	
	@Override
	public boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * <p>Registers the value adapter representing primitive type object.</p>
	 * <p>This method registers this <code>adapter</code> in two ways:
	 * <li>registering this adapter by this primitive type's class representation</li>
	 * <li>string representation of this <code>adapter</code>'s primitive type</li>
	 * </p>
	 * 
	 * @param adapter - adapter representing primitive type.
	 *  
	 * @throws NullPointerException if <code>adapter</code> is <code>null</code>
	 * or <code>adapter.getTarget()</code> result is <code>null</code>.
	 *
	 * @see #registerAdapter(ValueAdapter)
	 * @see PrimitiveValueAdapter
	 * @see PrimitiveValueAdapter#getTarget()
	 */
	void registerAdapter(PrimitiveValueAdapter<? extends Object> adapter)
			throws NullPointerException {
		
		registerAdapter((ValueAdapter<?>)adapter);
		
		if(adapter.getTarget() == null) {
			throw new NullPointerException("No target specified for adapter!");
		}
		
		getRegisteredAdapters().put(adapter.getTarget(), adapter);
	}
	
	/**
	 * <p>Recursively scans for appropriate adapter for this <code>target</code>.
	 * 
	 * @see ValueAdapterRegistry#getAdapter(Class)
	 */
	@Override
	public ValueAdapter<?> getAdapter(Class<?> target)
			throws NullPointerException, IllegalArgumentException {
		
		if(target == null) {
			throw new NullPointerException("null target class!");
		}

		ValueAdapter<?> adapter = getRegisteredAdapters().get(target.getCanonicalName());
		
		if(adapter == null && target.getSuperclass() != null) {
			return getAdapter(target.getSuperclass());
		} else if(adapter == null && target.getSuperclass() == null) {
			throw new IllegalArgumentException("No adapter registered for class: " + target.getName());
		}
		
		return adapter;
	}
	
	// ------
	// Value adapters implementations.
	// ------
	
	/**
	 * <p>Type representing {@link BigDecimal} object.</p>
	 */
	protected final ValueAdapter<BigDecimal> TYPE_BIG_DECIMAL = new AbstractValueAdapter<BigDecimal>() {

		@Override
		public BigDecimal convertValue(String value) {
			return new BigDecimal(value);
		}

		@Override
		public Class<?> getType() {
			return BigDecimal.class;
		}
	};
	
	/**
	 * <p>Type representing {@link BigInteger} object.</p>
	 */
	protected final ValueAdapter<BigInteger> TYPE_BIG_INTEGER = new AbstractValueAdapter<BigInteger>() {

		@Override
		public BigInteger convertValue(String value) {
			return new BigInteger(value);
		}

		@Override
		public Class<?> getType() {
			return BigInteger.class;
		}
	};
	
	/**
	 * <p>Type representing {@link Boolean} object.</p>
	 */
	protected final PrimitiveValueAdapter<Boolean> TYPE_BOOLEAN = new PrimitiveValueAdapter<Boolean>() {

		@Override
		public Boolean convertValue(String value) {
			return Boolean.getBoolean(value);
		}
		
		@Override
		public String getTarget() {
			return "boolean";
		}

		@Override
		public String toString(Boolean target) {
			return Boolean.toString(target);
		}

		@Override
		public Class<?> getType() {
			return Boolean.class;
		}
		
	};
	
	/**
	 * <p>Type representing {@link Byte} object.</p>
	 */
	protected final PrimitiveValueAdapter<Byte> TYPE_BYTE = new PrimitiveValueAdapter<Byte>() {

		@Override
		public Byte convertValue(String value) {
			return Byte.decode(value);
		}

		@Override
		public String getTarget() {
			return "byte";
		}

		@Override
		public String toString(Byte target) {
			return target == null ? null : Byte.toString(target);
		}

		@Override
		public Class<?> getType() {
			return Byte.class;
		}
	};

	@Deprecated
	protected final PrimitiveValueAdapter<byte[]> TYPE_BYTE_ARRAY = new PrimitiveValueAdapter<byte[]>() {

		@Override
		public byte[] convertValue(String value) throws ValueConversionException {
			try {
				return value.getBytes(DEFAULT_CHARSET);
			} catch (UnsupportedEncodingException e) {
				String message = "Conversion failed on target: " + getTarget() + " for value:" + value;
				throw new ValueConversionException(message, e);
			}
		}

		@Override
		public String getTarget() {
			return "[B";
		}

		@Override
		public String toString(byte[] target) {
			return target == null ? null : new String(target, Charset.defaultCharset());
		}

		@Override
		public Class<?> getType() {
			return ByteBuffer.class;
		}
	};
	
	/**
	 * <p>Type representing {@link Character} object.</p>
	 */
	protected final PrimitiveValueAdapter<Character> TYPE_CHAR = new PrimitiveValueAdapter<Character>() {

		@Override
		public Character convertValue(String value) throws ValueConversionException {
			if(value == null) {
				return null;
			} else if (value.length() != 1) {
				throw new ValueConversionException("Unable to convert char: " + value);
			}
			
			return value.charAt(0);
		}

		@Override
		public String getTarget() {
			return "char";
		}

		@Override
		public String toString(Character target) {
			return target == null ? null : Character.toString(target);
		}

		@Override
		public Class<?> getType() {
			return Character.class;
		}
	};
	
	/**
	 * <p>Type representing {@link Double} object.</p>
	 */
	protected final PrimitiveValueAdapter<Double> TYPE_DOUBLE = new PrimitiveValueAdapter<Double>() {

		@Override
		public Double convertValue(String value) {
			return Double.valueOf(value);
		}

		@Override
		public String getTarget() {
			return "double";
		}

		@Override
		public String toString(Double target) {
			return target == null ? null : Double.toString(target);
		}

		@Override
		public Class<?> getType() {
			return Double.class;
		}
	};
	
	/**
	 * <p>Type representing {@link Duration} object.</p>
	 */
	protected final ValueAdapter<Duration> TYPE_DURATION = new AbstractValueAdapter<Duration>() {

		@Override
		public Duration convertValue(String value) throws ValueConversionException {
			try {
				return DatatypeFactory.newInstance().newDuration(value);
			} catch (DatatypeConfigurationException e) {
				throw new ValueConversionException("Conversion failed for value:" + value, e);
			}
		}

		@Override
		public Class<?> getType() {
			return Duration.class;
		}
	};
	
	/**
	 * <p>Type representing {@link Float} object.</p>
	 */
	protected final PrimitiveValueAdapter<Float> TYPE_FLOAT = new PrimitiveValueAdapter<Float>() {

		@Override
		public Float convertValue(String value) {
			return Float.valueOf(value);
		}

		@Override
		public String getTarget() {
			return "float";
		}

		@Override
		public String toString(Float target) {
			return target == null ? null : Float.toString(target);
		}

		@Override
		public Class<?> getType() {
			return Float.class;
		}
	};
	
	/**
	 * <p>Type representing {@link XMLGregorianCalendar} object.</p>
	 */
	protected final ValueAdapter<XMLGregorianCalendar> TYPE_GREGORIAN_CALENDAR =
			new AbstractValueAdapter<XMLGregorianCalendar>() {

		@Override
		public XMLGregorianCalendar convertValue(String value) throws ValueConversionException {
			try {
				return DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
			} catch (DatatypeConfigurationException e) {
				throw new ValueConversionException("Conversion failed for value:" + value, e);
			}
		}

		@Override
		public Class<?> getType() {
			return XMLGregorianCalendar.class;
		}

	};
	
	/**
	 * <p>Type representing {@link Integer} object.</p>
	 */
	protected final PrimitiveValueAdapter<Integer> TYPE_INT = new PrimitiveValueAdapter<Integer>() {

		@Override
		public Integer convertValue(String value) {
			return Integer.valueOf(value);
		}

		@Override
		public String getTarget() {
			return "int";
		}

		@Override
		public String toString(Integer target) {
			return target == null ? null : Integer.toString(target);
		}

		@Override
		public Class<?> getType() {
			return Integer.class;
		}
	};
	
	/**
	 * <p>Type representing {@link Long} object.</p>
	 */
	protected final PrimitiveValueAdapter<Long> TYPE_LONG = new PrimitiveValueAdapter<Long>() {

		@Override
		public Long convertValue(String value) {
			return Long.valueOf(value);
		}

		@Override
		public String getTarget() {
			return "long";
		}

		@Override
		public String toString(Long target) {
			return target == null ? null : Long.toString(target);
		}

		@Override
		public Class<?> getType() {
			return Long.class;
		}
	};
	
	/**
	 * <p>Type representing {@link QName} object.</p>
	 */
	protected final ValueAdapter<QName> TYPE_QNAME = new AbstractValueAdapter<QName>() {

		@Override
		public QName convertValue(String value) {
			if(value == null || value.isEmpty()) {
				return null;
			}
			
			QName qName = null;
			
			if(value.contains(":")) {
				String[] valueArray = value.split(":");
				qName = new QName(valueArray[0], valueArray[1]);
			} else {
				qName = new QName(value);
			}
			
			return qName;
		}

		@Override
		public String toString(QName target) {
			return target == null ? null : target.toString();
		}

		@Override
		public Class<?> getType() {
			return QName.class;
		}
	};
	
	/**
	 * <p>Type representing {@link Short} object.</p>
	 */
	protected final PrimitiveValueAdapter<Short> TYPE_SHORT = new PrimitiveValueAdapter<Short>() {

		@Override
		public Short convertValue(String value) {
			return Short.valueOf(value);
		}

		@Override
		public String getTarget() {
			return "short";
		}

		@Override
		public String toString(Short target) {
			return target == null ? null : Short.toString(target);
		}

		@Override
		public Class<?> getType() {
			return Short.class;
		}
	};

	/**
	 * <p>Type representing {@link String} object.</p>
	 */
	protected final ValueAdapter<String> TYPE_STRING = new AbstractValueAdapter<String>() {

		@Override
		public String convertValue(String value) {
			return value;
		}

		@Override
		public Class<?> getType() {
			return String.class;
		}

	};
	
	/**
	 * <p>Type representing {@link URL} object.</p>
	 */
	protected final ValueAdapter<URL> TYPE_URL = new AbstractValueAdapter<URL>() {

		@Override
		public URL convertValue(String value) throws ValueConversionException {
			if(value == null || value.isEmpty()) {
				return null;
			}
			
			try {
				return new URL(value);
			} catch (MalformedURLException e) {
				String message = "Url convertion error! Converting value: " + value;
				throw new ValueConversionException(message, e);
			}
		}

		@Override
		public Class<?> getType() {
			return URL.class;
		}

	};
	
	/**
	 * <p>Abstract implementation of the value adapter interface. The
	 * implementation of this class contains only {@link #toString(Object)}
	 * method which creates String representation of the target object.</p>
	 * 
	 * @author Jan Janickovic
	 *
	 * @param <T>
	 * 
	 * @see ValueAdapter
	 */
	public abstract class AbstractValueAdapter<T> implements ValueAdapter<T> {

		@Override
		public String toString(T target) {
			if(target == null) {
				return null;
			}
			
			return target.toString();
		}
	}
	
	/**
	 * <p>Abstract class representing primitive types. Supported primitive types
	 * are show in table below.</p>
	 * 
	 * <table border="1">
	 * <tr><th>Primitive type <code>&lt;T&gt;</code></th><th>Field</th></tr>
	 * <tr><td>boolean</td><td>{@link SimpleTypeRegistry#TYPE_BOOLEAN}</td></tr>
	 * <tr><td>byte</td><td>{@link SimpleTypeRegistry#TYPE_BYTE}</td></tr>
	 * <tr><td>double</td><td>{@link SimpleTypeRegistry#TYPE_DOUBLE}</td></tr>
	 * <tr><td>float</td><td>{@link SimpleTypeRegistry#TYPE_FLOAT}</td></tr>
	 * <tr><td>int</td><td>{@link SimpleTypeRegistry#TYPE_INT}</td></tr>
	 * <tr><td>long</td><td>{@link SimpleTypeRegistry#TYPE_LONG}</td></tr>
	 * <tr><td>short</td><td>{@link SimpleTypeRegistry#TYPE_SHORT}</td></tr>
	 * </table>
	 * <br> 
	 * 
	 * @param <T> - type representing primitive type.
	 * 
	 * @author Jan Janickovic
	 */
	public abstract class PrimitiveValueAdapter<T> implements ValueAdapter<T> {

		/**
		 * <p>Method used as a key for the {@link ValueAdapter} object.</p>
		 * <p>By default, the canonical name of the class that is used as
		 * parameterized type is set as the returning result. For primitive
		 * types (long, short, int, etc.) the string representation of the
		 * primitive type is used.</p>
		 * 
		 * @return the representation of the target. The return value
		 * <b>must not</b> be <code>null</code>.
		 * 
		 * @see Class#getCanonicalName()
		 * @see #getType()
		 */
		abstract String getTarget();

	}

}
