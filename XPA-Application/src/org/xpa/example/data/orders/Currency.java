package org.xpa.example.data.orders;

import org.xpaframework.ValueAdapter;
import org.xpaframework.ValueConversionException;


public enum Currency {

	EURO("EUR"),
	POUND("GBP"),
	DOLLAR("USD");
	
	private String value;
	
	public String getValue() {
		return value;
	}

	private Currency(String value) {
		this.value = value;
	}

	public static class Adapter implements ValueAdapter<Currency> {
		
		@Override
		public Class<?> getType() {
			return Currency.class;
		}
		
		@Override
		public Currency convertValue(String value) throws ValueConversionException {
			if(value == null || value.isEmpty()) {
				return null;
			}
			
			for(Currency currency : Currency.values()) {
				if(currency.getValue().equalsIgnoreCase(value)) {
					return currency;
				}
			}
			
			throw new ValueConversionException("No value found for value: " + value);
		}
		
		@Override
		public String toString(Currency target) {
			return target.getValue();
		}

	}
	
}
